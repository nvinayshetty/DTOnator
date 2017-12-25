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

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashSet;
import java.util.Iterator;


/**
 * Created by vinay on 19/4/15.
 */
public class JsonDtoGenerator extends WriteCommandAction.Simple {

    private static JsonDtoBuilder jsonDtoBuilder;
    final CamelCase camelCase = new CamelCase();
    final FieldCreationStrategy fieldCreationStrategy;
    final AccessModifier accessModifier;
    final FieldNameParser nameParser;
    final NameConflictResolver nameConflictResolver;
    private PsiClass classUnderCaret;
    private String json;
    private PsiElementFactory psiFactory;
    private DtoCreationOptionsFacade dtoCreationOptionsFacade;
    private HashSet<NameParserCommand> nameParserCommands;
    private HashSet<NameConflictResolverCommand> nameConflictResolverCommands;

    JsonDtoGenerator(JsonDtoBuilder builder) {
        super(builder.classUnderCaret.getProject(), builder.classUnderCaret.getContainingFile());
        psiFactory = JavaPsiFacade.getElementFactory(builder.classUnderCaret.getProject());
        json = builder.json;
        classUnderCaret = builder.classUnderCaret;
        dtoCreationOptionsFacade = builder.dtoCreationOptionsFacade;
        nameParserCommands = builder.feildNameParser;
        nameConflictResolverCommands = builder.nameConflictResolverCommands;
        fieldCreationStrategy = dtoCreationOptionsFacade.getFieldCreationStrategy();
        accessModifier = dtoCreationOptionsFacade.getAccessModifier();
        nameParser = new FieldNameParser(nameParserCommands);
        nameConflictResolver = new NameConflictResolver(nameConflictResolverCommands);
    }

    public static JsonDtoBuilder getJsonDtoBuilder() {
        return jsonDtoBuilder = new JsonDtoBuilder();
    }

    @Override
    protected void run() {
        addFieldsToTheClassUnderCaret(json);
    }

    private void addFieldsToTheClassUnderCaret(String json) {
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
        generateDto(json.toString(), aClass);
        dtoCreationOptionsFacade.getClassAdderStrategy().addClass(aClass);
    }

    private PsiClass generateDto(String input, PsiClass aClass) {
        JSONTokener jsonTokener = new JSONTokener(input);
        Object object = jsonTokener.nextValue();
        if (object instanceof JSONObject) {
            JSONObject json = (JSONObject) object;
            Iterator<String> keysIterator = json.keys();
            while (keysIterator.hasNext()) {
                String key = keysIterator.next();
                String filedStr = getFieldsForJson(json, key);
                PsiField fieldFromText = psiFactory.createFieldFromText(filedStr, aClass);
                aClass.add(fieldFromText);
                Object jsonObject = json.get(key);
                if (jsonObject instanceof JSONObject) {
                    generateClassForObject((JSONObject) jsonObject, key);
                }
            }
        }
        if (object instanceof JSONArray) {
            final Notification processingNotification = new Notification("DtoGenerator", "DtoGenerator Can't Process JsonArray!", "A top level json array can't be processed as it doesn't have suitable keys to map to. Please consider entering an JsonObject ", NotificationType.ERROR);
            processingNotification.notify(classUnderCaret.getProject());
           /* String fieldRepresentation = "";
            JSONArray jsonArray = (JSONArray) object;
            String dataTypeForList = "";
            if (jsonArray.length() != 0) {
                int depth = getDepth(jsonArray);
                String dataType = object.getClass().getSimpleName();
                FieldRepresentor fieldRepresentor = new FieldRepresenterFactory().convert(dataType);
                if (isPrimitiveList(jsonArray, depth)) {
                    dataTypeForList = getPrimitiveName(depth, jsonArray);
                } else {
                    JSONObject objectWithMostNumberOfKeys = getObjectWithMostNumberOfKeys(jsonArray);
                    generateClassForObject(objectWithMostNumberOfKeys, dataType);
                }
                ((JsonArrayRepresentor) fieldRepresentor).setDepth(depth);
                ((JsonArrayRepresentor) fieldRepresentor).setDataType(dataTypeForList);
                fieldRepresentation = fieldCreationStrategy.getFieldFor(fieldRepresentor, accessModifier, dataType, nameParser, nameConflictResolver) + "\n";
                PsiField fieldFromText = psiFactory.createFieldFromText(fieldRepresentation, aClass);
                aClass.add(fieldFromText);
            }*/

        }
        if (dtoCreationOptionsFacade.getEncapsulationOptionses().contains(FieldEncapsulationOptions.PROVIDE_PRIVATE_FIELD)) {
            aClass = new EncapsulatedClassCreator(dtoCreationOptionsFacade.getEncapsulationOptionses(), nameParser).getClassWithEncapsulatedFileds(aClass);
        }
        return aClass;
    }


    private String getFieldsForJson(JSONObject json, String key) {
        String fieldRepresentation = "";
        Object object = json.get(key);
        String dataType = object.getClass().getSimpleName();
        FieldRepresentor fieldRepresentor = new FieldRepresenterFactory().convert(dataType);
        fieldRepresentor.setProject(classUnderCaret.getProject());
        if (fieldRepresentor instanceof JsonObjectRepresentor) {
            ((JsonObjectRepresentor) fieldRepresentor).setNameParser(nameParser);
        }
        if (fieldRepresentor instanceof JsonArrayRepresentor) {
            String dataTypeForList = key;

            JSONArray object1 = (JSONArray) object;
            int depth = getDepth(object1);
            if (isPrimitiveList(object1, depth)) {
                dataTypeForList = getPrimitiveName(depth, object1);
            } else {
                //Todo:if depth==0  IT IS CURRNTLY CONSIDERED AS PRIMITIVE LIST
                JSONObject objectWithMostNumberOfKeys = getObjectWithMostNumberOfKeys(object1);
                generateClassForObject(objectWithMostNumberOfKeys, key);

            }
            ((JsonArrayRepresentor) fieldRepresentor).setDepth(depth);
            ((JsonArrayRepresentor) fieldRepresentor).setDataType(dataTypeForList);
        }
        fieldRepresentation = fieldCreationStrategy.getFieldFor(fieldRepresentor, accessModifier, key, nameParser, nameConflictResolver);
        return fieldRepresentation + "\n";
    }


    private String getPrimitiveName(int depth, JSONArray jsonArray) {
        if (jsonArray.length() == 0)
            return "String";
        Object object = jsonArray.get(0);
        for (int i = 1; i < depth; i++) {
            JSONArray jArray = (JSONArray) object;
            object = jArray.get(0);
        }
        if (object != null)
            return object.getClass().getSimpleName();
        else return "String";
    }


    private JSONObject getObjectWithMostNumberOfKeys(JSONArray jsonArray) {
       /* if (jsonArray.length() == 0)
            return new JSONObject("{}");*/
        Object object = jsonArray.get(0);
        if (object instanceof JSONObject) {
            JSONObject objectWithMaxKeys = (JSONObject) object;
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject objectAtIndexPosition = (JSONObject) jsonArray.get(i);
                if (objectAtIndexPosition.keySet().size() > objectWithMaxKeys.keySet().size())
                    objectWithMaxKeys = objectAtIndexPosition;

            }
            return objectWithMaxKeys;
        } else {
            JSONArray jArray = (JSONArray) object;
            return getObjectWithMostNumberOfKeys(jArray);
        }

    }

    private int getDepth(JSONArray jsonArray) {
        int depth = 0;
        if (jsonArray.length() == 0)
            return 0;
        while (true) {
            Object object = jsonArray.get(0);
            depth++;
            if (object instanceof JSONArray) {
                JSONArray jArray = (JSONArray) object;
                return depth + getDepth(jArray);
            } else {
                return depth;
            }
        }
    }

    private void generateClassForObject(JSONObject jsonObject, String key) throws JSONException {
        if (nameParserCommands.contains(camelCase))
            key = camelCase.parseFieldName(key);
        addClass(key, jsonObject);

    }

    public boolean isPrimitiveList(JSONArray jsonArray, int depth) {
        if (jsonArray.length() == 0)
            return true;
        else {
            Object object = jsonArray.get(0);
            for (int i = 1; i < depth; i++) {
                JSONArray jsonObject = (JSONArray) object;
                object = jsonObject.get(0);
            }
            if (object != null && !(object instanceof JSONObject) && !(object instanceof JSONArray))
                return true;

            return false;
        }
    }
}
