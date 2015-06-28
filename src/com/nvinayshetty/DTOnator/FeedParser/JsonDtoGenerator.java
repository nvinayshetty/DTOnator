package com.nvinayshetty.DTOnator.FeedParser;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.nvinayshetty.DTOnator.ClassCreator.EncapsulatedClassCreator;
import com.nvinayshetty.DTOnator.DtoCreators.DtoCreationOptionsFacade;
import com.nvinayshetty.DTOnator.DtoCreators.FieldEncapsulationOptions;
import com.nvinayshetty.DTOnator.FieldCreator.FieldRepresentor;
import com.nvinayshetty.DTOnator.FieldCreator.JavaFieldFactory;
import com.nvinayshetty.DTOnator.Utility.DtoHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


/**
 * Created by vinay on 19/4/15.
 */
public class JsonDtoGenerator extends WriteCommandAction.Simple {
    public static final String SEPARATOR = "JSON_ARRAY_SEPERATOR";
    private static JsonDtoBuilder jsonDtoBuilder;
    private PsiClass classUnderCaret;
    private JSONObject json;
    private PsiElementFactory psiFactory;
    private DtoCreationOptionsFacade dtoCreationOptionsFacade;

    private JsonDtoGenerator(JsonDtoBuilder builder) {
        super(builder.classUnderCaret.getProject(), builder.classUnderCaret.getContainingFile());
        psiFactory = JavaPsiFacade.getElementFactory(builder.classUnderCaret.getProject());
        json = builder.json;
        classUnderCaret = builder.classUnderCaret;
        dtoCreationOptionsFacade = builder.dtoCreationOptionsFacade;
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
        String className = DtoHelper.getSubClassName(name);
        PsiClass aClass = psiFactory.createClass(className);
        generateDto(json, aClass);
        dtoCreationOptionsFacade.getClassAdderStrategy().addClass(aClass);
    }

    private PsiClass generateDto(JSONObject json, PsiClass aClass) {
        Iterator<?> keysIterator = json.keys();
        while (keysIterator.hasNext()) {
            String key = (String) keysIterator.next();
            String filedStr = getClassFields(json, key);
            aClass.add(psiFactory.createFieldFromText(filedStr, aClass));
        }
        if (dtoCreationOptionsFacade.getPrivateFieldOptionse().contains(FieldEncapsulationOptions.PROVIDE_PRIVATE_FIELD)) {
            aClass = new EncapsulatedClassCreator(dtoCreationOptionsFacade.getPrivateFieldOptionse()).getClassWithEncapsulatedFileds(aClass);
        }
        return aClass;
    }

    private String getClassFields(JSONObject json, String key) {
        String fieldName = key;
        String fieldRepresentation = "";
        try {
            Object object = json.get(key);
            String dataType = object.getClass().getSimpleName();
            FieldRepresentor fieldRepresentor = JavaFieldFactory.convert(dataType);
            if (object instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) object;
                Object arrayElement = jsonArray.get(0);
                if (!(arrayElement instanceof JSONObject) && !(arrayElement instanceof JSONArray)) {
                    fieldName = arrayElement.getClass().getSimpleName().trim() + SEPARATOR + key;
                }
            }
            fieldRepresentation = dtoCreationOptionsFacade.getFieldCreationStrategy().getFieldFor(fieldRepresentor, dtoCreationOptionsFacade.getAccessModifier(), fieldName);
            generateClassForObject(json, key, fieldRepresentor);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fieldRepresentation + "\n";
    }

    private void generateClassForObject(JSONObject json, String key, FieldRepresentor fieldRepresentor) throws JSONException {
        switch (fieldRepresentor) {
            case JSON_OBJECT: {
                JSONObject jsonObject = json.getJSONObject(key);
                addClass(key, jsonObject);
            }
            break;
            case JSON_ARRAY:
                Object jsonArrayObject = json.getJSONArray(key).get(0);
                if (jsonArrayObject instanceof JSONObject)
                    addClass(key, (JSONObject) jsonArrayObject);
                break;
        }
    }


    public static class JsonDtoBuilder {
        private JSONObject json;
        private PsiClass classUnderCaret;
        private DtoCreationOptionsFacade dtoCreationOptionsFacade;

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


        public JsonDtoGenerator createJsonDtoGenerator() {
            return new JsonDtoGenerator(this);
        }
    }


}
