package com.nvinayshetty.DTOnator.DtoGenerators;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.nvinayshetty.DTOnator.Factory.JsonTypeToJavaObjectMapper;
import com.nvinayshetty.DTOnator.Factory.stringToEnumConversionFactory;
import com.nvinayshetty.DTOnator.Utility.DtoHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


/**
 * Created by vinay on 19/4/15.
 */
public class JsonDtoGenerator extends WriteCommandAction.Simple {
    private PsiClass psiClass;
    private JSONObject json;
    private PsiElementFactory psiFactory;
    private Project project;
    private PsiFile psiFile;
    private PsiDirectory psiDirectory;

    public JsonDtoGenerator(Project project, PsiFile file, JSONObject jsonString, PsiClass mClass) {
        super(project, file);
        this.project = project;
        this.psiFactory = JavaPsiFacade.getElementFactory(project);
        this.json = jsonString;
        this.psiClass = mClass;
        this.psiFile = file;
        psiDirectory = psiFile.getContainingDirectory();
    }


    @Override
    protected void run() {
        addFieldsToTheTopLevelClass(json);

    }

    private void addFieldsToTheTopLevelClass(JSONObject json) {
        Iterator<?> keysIterator = json.keys();
        String filedStr = null;
        while (keysIterator.hasNext()) {
            String nextKey = (String) keysIterator.next();
            filedStr = generateFieldAndClass(json, nextKey) + "\n";
            psiClass.add(psiFactory.createFieldFromText(filedStr, psiClass));
        }
    }

    private String generateFieldAndClass(JSONObject json, String nextKey) {
        String filedStr = null;
        try {

            JsonTypeToJavaObjectMapper type = getObjectTypeOfNextKey(json, nextKey);
            filedStr = type.getFieldRepresentationFor(nextKey);
            generateStaticClassForObject(json, nextKey, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return filedStr;
    }


    private String getAllFieldsOfStaticInnerClass(JSONObject json) {
        Iterator<?> keysIterator = json.keys();
        String filedStr = null;
        StringBuilder subClassFields = new StringBuilder();
        while (keysIterator.hasNext()) {
            String nextKey = (String) keysIterator.next();
            filedStr = generateFieldAndClass(json, nextKey);
            subClassFields.append(filedStr + "\n");
        }
        return subClassFields.toString();
    }


    private JsonTypeToJavaObjectMapper getObjectTypeOfNextKey(JSONObject json, String key) throws JSONException {
        Object object = json.get(key);
        String type = object.getClass().getSimpleName();
        return stringToEnumConversionFactory.convert(type);
    }


    private void generateStaticClassForObject(JSONObject json, String key, JsonTypeToJavaObjectMapper objectType) throws JSONException {
        switch (objectType) {
            case JSON_OBJECT: {
                JSONObject jsonObject = json.getJSONObject(key);
                addStaticInnerClass(key, jsonObject);
            }
            break;
            case JSON_ARRAY:
                JSONObject jsonArrayObject = (JSONObject) json.getJSONArray(key).get(0);
                addStaticInnerClass(key, jsonArrayObject);
                break;
        }
    }

    private void addStaticInnerClass(String key, JSONObject jsonObject) {
        String className = DtoHelper.getSubClassName(key);
        String classContent = getAllFieldsOfStaticInnerClass(jsonObject);
        PsiClass innerClass = psiFactory.createClassFromText(classContent.trim(), psiClass);
        innerClass.setName(className.trim());
        innerClass.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);
        JavaCodeStyleManager.getInstance(project).shortenClassReferences(innerClass);
        psiDirectory.add(innerClass);
    }


}
