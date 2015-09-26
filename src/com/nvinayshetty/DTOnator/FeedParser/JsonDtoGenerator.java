/*
 * Copyright (C) 2015 Vinaya Prasad N
 *
 *         This program is free software: you can redistribute it and/or modify
 *         it under the terms of the GNU General Public License as published by
 *         the Free Software Foundation, either version 3 of the License, or
 *         (at your option) any later version.
 *
 *         This program is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *         GNU General Public License for more details.
 *
 *         You should have received a copy of the GNU General Public License
 *         along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.nvinayshetty.DTOnator.FeedParser;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.nvinayshetty.DTOnator.ClassCreator.EncapsulatedClassCreator;
import com.nvinayshetty.DTOnator.DtoCreationOptions.DtoCreationOptionsFacade;
import com.nvinayshetty.DTOnator.DtoCreationOptions.FieldEncapsulationOptions;
import com.nvinayshetty.DTOnator.FeedValidator.KeywordClassifier;
import com.nvinayshetty.DTOnator.FieldCreator.AccessModifier;
import com.nvinayshetty.DTOnator.FieldCreator.FieldCreationStrategy;
import com.nvinayshetty.DTOnator.FieldRepresentors.FieldRepresenterFactory;
import com.nvinayshetty.DTOnator.FieldRepresentors.FieldRepresentor;
import com.nvinayshetty.DTOnator.FieldRepresentors.JsonArrayRepresentor;
import com.nvinayshetty.DTOnator.FieldRepresentors.JsonObjectRepresentor;
import com.nvinayshetty.DTOnator.NameConventionCommands.CamelCase;
import com.nvinayshetty.DTOnator.NameConventionCommands.FieldNameParser;
import com.nvinayshetty.DTOnator.NameConventionCommands.NameParserCommand;
import com.nvinayshetty.DTOnator.Utility.DtoHelper;
import com.nvinayshetty.DTOnator.nameConflictResolvers.NameConflictResolver;
import com.nvinayshetty.DTOnator.nameConflictResolvers.NameConflictResolverCommand;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;


/**
 * Created by vinay on 19/4/15.
 */
public class JsonDtoGenerator extends WriteCommandAction.Simple {


    private static JsonDtoBuilder jsonDtoBuilder;
    final CamelCase camelCase = new CamelCase();
    FieldRepresentor fieldRepresentor;
    FieldCreationStrategy fieldCreationStrategy;
    AccessModifier accessModifier;
    FieldNameParser nameParser;
    NameConflictResolver nameConflictResolver;
    private PsiClass classUnderCaret;
    private JSONObject json;
    private PsiElementFactory psiFactory;
    private DtoCreationOptionsFacade dtoCreationOptionsFacade;
    private HashSet<NameParserCommand> nameParserCommands;
    private HashSet<NameConflictResolverCommand> nameConflictResolverCommandses;

    private JsonDtoGenerator(JsonDtoBuilder builder) {
        super(builder.classUnderCaret.getProject(), builder.classUnderCaret.getContainingFile());
        psiFactory = JavaPsiFacade.getElementFactory(builder.classUnderCaret.getProject());
        json = builder.json;
        classUnderCaret = builder.classUnderCaret;
        dtoCreationOptionsFacade = builder.dtoCreationOptionsFacade;
        nameParserCommands = builder.feildNameParser;
        nameConflictResolverCommandses = builder.nameConflictResolverCommands;
        fieldCreationStrategy = dtoCreationOptionsFacade.getFieldCreationStrategy();
        accessModifier = dtoCreationOptionsFacade.getAccessModifier();
        nameParser = new FieldNameParser(nameParserCommands);
        nameConflictResolver = new NameConflictResolver(nameConflictResolverCommandses);

    }

    public static JsonDtoBuilder getJsonDtoBuilder() {
        return jsonDtoBuilder = new JsonDtoBuilder();
    }

    @Override
    protected void run() {
        addFieldsToTheClassUnderCaret(json);
    }

    private void addFieldsToTheClassUnderCaret(JSONObject json) {
        PsiClass psiClass = generateDto(json, classUnderCaret);
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(psiClass.getProject());
        styleManager.optimizeImports(psiClass.getContainingFile());
        styleManager.shortenClassReferences(psiClass.getContainingFile());
    }

    private void addClass(String name, JSONObject json) {
        String className = DtoHelper.firstetterToUpperCase(name);
        final KeywordClassifier keywordClassifier = new KeywordClassifier();
        if (!keywordClassifier.isValidJavaIdentifier(className)) {
            className = nameConflictResolver.resolveNamingConflict(name);
            className = DtoHelper.firstetterToUpperCase(className);
        }
        PsiClass aClass = psiFactory.createClass(className);
        generateDto(json, aClass);
        dtoCreationOptionsFacade.getClassAdderStrategy().addClass(aClass);
    }

    private PsiClass generateDto(JSONObject json, PsiClass aClass) {
        Iterator<?> keysIterator = json.keys();
        while (keysIterator.hasNext()) {
            String key = (String) keysIterator.next();
            String filedStr = getClassFields(json, key);
            PsiField fieldFromText = psiFactory.createFieldFromText(filedStr, aClass);
            aClass.add(fieldFromText);
        }
        if (dtoCreationOptionsFacade.getEncapsulationOptionses().contains(FieldEncapsulationOptions.PROVIDE_PRIVATE_FIELD)) {
            aClass = new EncapsulatedClassCreator(dtoCreationOptionsFacade.getEncapsulationOptionses(), nameParser).getClassWithEncapsulatedFileds(aClass);
        }
        return aClass;
    }


    private String getClassFields(JSONObject json, String key) {
        String fieldRepresentation = "";
        try {
            Object object = json.get(key);
            String dataType = object.getClass().getSimpleName();

            fieldRepresentor = new FieldRepresenterFactory().convert(dataType);
            fieldRepresentor.setProject(classUnderCaret.getProject());
            if (fieldRepresentor instanceof JsonObjectRepresentor) {
                ((JsonObjectRepresentor) fieldRepresentor).setNameParser(nameParser);
            }
            if (fieldRepresentor instanceof JsonArrayRepresentor) {
                Object jsonArrayObject = json.getJSONArray(key).get(0);
                String simpleName = jsonArrayObject.getClass().getSimpleName();
                if (jsonArrayObject instanceof JSONObject)
                    simpleName = key;
                if (nameParserCommands.contains(camelCase))
                    simpleName = camelCase.parseFieldName(simpleName);
                ((JsonArrayRepresentor) fieldRepresentor).setDataType(simpleName);
            }
            fieldRepresentation = fieldCreationStrategy.getFieldFor(fieldRepresentor, accessModifier, key, nameParser, nameConflictResolver);
            generateClassForObject(json, key, fieldRepresentor);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fieldRepresentation + "\n";
    }

    private void generateClassForObject(JSONObject json, String key, FieldRepresentor fieldRepresentor) throws JSONException {

        if (fieldRepresentor instanceof JsonObjectRepresentor) {
            JSONObject jsonObject = json.getJSONObject(key);
            if (nameParserCommands.contains(camelCase))
                key = camelCase.parseFieldName(key);
            addClass(key, jsonObject);
        } else if (fieldRepresentor instanceof JsonArrayRepresentor) {
            Object jsonArrayObject = json.getJSONArray(key).get(0);
            if (jsonArrayObject instanceof JSONObject) {
                if (nameParserCommands.contains(camelCase))
                    key = camelCase.parseFieldName(key);
                addClass(key, (JSONObject) jsonArrayObject);
            }

        }
    }


    public static class JsonDtoBuilder {
        private JSONObject json;
        private PsiClass classUnderCaret;
        private DtoCreationOptionsFacade dtoCreationOptionsFacade;
        private HashSet<NameConflictResolverCommand> nameConflictResolverCommands;
        private HashSet<NameParserCommand> feildNameParser;

        public JsonDtoBuilder setJson(JSONObject json) {
            this.json = json;
            return this;
        }

        public JsonDtoBuilder setClassUnderCaret(PsiClass classUnderCaret) {
            this.classUnderCaret = classUnderCaret;
            return this;
        }

        public JsonDtoBuilder setDtoCreationOptionsFacade(DtoCreationOptionsFacade dtoCreationOptionsFacade) {
            this.dtoCreationOptionsFacade = dtoCreationOptionsFacade;
            return this;
        }

        public JsonDtoBuilder setFeildNameParser(HashSet<NameParserCommand> feildNameParser) {
            this.feildNameParser = feildNameParser;
            return this;
        }

        public JsonDtoBuilder setNameConflictResolverr(HashSet<NameConflictResolverCommand> nameConflictResolverCommands) {
            this.nameConflictResolverCommands = nameConflictResolverCommands;
            return this;
        }

        public JsonDtoGenerator createJsonDtoGenerator() {
            return new JsonDtoGenerator(this);
        }

    }


}
