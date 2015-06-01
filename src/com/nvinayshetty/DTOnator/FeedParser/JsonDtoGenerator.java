package com.nvinayshetty.DTOnator.FeedParser;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.nvinayshetty.DTOnator.ClassAdder.ClassAdder;
import com.nvinayshetty.DTOnator.Factory.ObjectType;
import com.nvinayshetty.DTOnator.Factory.stringToEnumConversionFactory;
import com.nvinayshetty.DTOnator.FieldCreator.FieldCreator;
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
    private FieldCreator fieldCreator;
    private ClassAdder classAdder;

    public JsonDtoGenerator(Project project, PsiFile file, JSONObject jsonString, PsiClass mClass, FieldCreator fieldCreator, ClassAdder classAdder) {
        super(project, file);
        this.psiFactory = JavaPsiFacade.getElementFactory(project);
        this.json = jsonString;
        this.psiClass = mClass;
        this.fieldCreator = fieldCreator;
        this.classAdder = classAdder;

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
            String filedStr1 = "";
            try {
                Object object = json.get(nextKey);
                String type1 = object.getClass().getSimpleName();
                ObjectType type = stringToEnumConversionFactory.convert(type1);
                filedStr1 = fieldCreator.getFieldFor(type, nextKey);
                generateClassForObject(json, nextKey, type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            filedStr = filedStr1 + "\n";
            psiClass.add(psiFactory.createFieldFromText(filedStr, psiClass));
        }
    }


    private String getAllFieldsOf(JSONObject json) {
        Iterator<?> keysIterator = json.keys();
        String filedStr = null;
        StringBuilder subClassFields = new StringBuilder();
        while (keysIterator.hasNext()) {
            String nextKey = (String) keysIterator.next();
            String filedStr1 = null;
            try {
                Object object = json.get(nextKey);
                String type1 = object.getClass().getSimpleName();
                ObjectType type = stringToEnumConversionFactory.convert(type1);
                filedStr1 = fieldCreator.getFieldFor(type, nextKey);
                generateClassForObject(json, nextKey, type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            filedStr = filedStr1;
            subClassFields.append(filedStr + "\n");
        }
        return subClassFields.toString();
    }


    private void generateClassForObject(JSONObject json, String key, ObjectType objectType) throws JSONException {
        switch (objectType) {
            case JSON_OBJECT: {
                JSONObject jsonObject = json.getJSONObject(key);
                addClass(key, jsonObject);
            }
            break;
            case JSON_ARRAY:
                JSONObject jsonArrayObject = (JSONObject) json.getJSONArray(key).get(0);
                addClass(key, jsonArrayObject);
                break;
        }
    }

    private void addClass(String key, JSONObject jsonObject) {
        String className = DtoHelper.getSubClassName(key);
        String classContent = getAllFieldsOf(jsonObject);
        PsiClass aClass = psiFactory.createClassFromText(classContent.trim(), psiClass);
        aClass.setName(className.trim());
        classAdder.addClass(aClass);
    }


}
