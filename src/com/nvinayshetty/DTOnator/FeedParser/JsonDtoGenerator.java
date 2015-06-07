package com.nvinayshetty.DTOnator.FeedParser;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.nvinayshetty.DTOnator.ClassCreator.ClassCreatorStrategy;
import com.nvinayshetty.DTOnator.ClassCreator.EncapsulatedClassCreator;
import com.nvinayshetty.DTOnator.DtoCreators.PrivateFieldOptions;
import com.nvinayshetty.DTOnator.FieldCreator.AccessModifier;
import com.nvinayshetty.DTOnator.FieldCreator.FieldCreationStrategy;
import com.nvinayshetty.DTOnator.FieldCreator.ObjectType;
import com.nvinayshetty.DTOnator.FieldCreator.TypeToObjectTypeConverter;
import com.nvinayshetty.DTOnator.Utility.DtoHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.EnumSet;
import java.util.Iterator;


/**
 * Created by vinay on 19/4/15.
 */
public class JsonDtoGenerator extends WriteCommandAction.Simple {
    private PsiClass psiClass;
    private JSONObject json;
    private PsiElementFactory psiFactory;
    private FieldCreationStrategy fieldCreationStrategy;
    private ClassCreatorStrategy classAdderStrategy;
    private AccessModifier accessModifier;
    private EnumSet<PrivateFieldOptions> privateFieldOptionse;

    public JsonDtoGenerator(Project project, PsiFile file, JSONObject jsonString, PsiClass mClass, AccessModifier accessModifier, FieldCreationStrategy fieldCreationStrategy, ClassCreatorStrategy fileCreatorStrategy, EnumSet<PrivateFieldOptions> privateFieldOptionse) {
        super(project, file);
        this.psiFactory = JavaPsiFacade.getElementFactory(project);
        this.json = jsonString;
        this.psiClass = mClass;
        this.fieldCreationStrategy = fieldCreationStrategy;
        this.classAdderStrategy = fileCreatorStrategy;
        this.accessModifier = accessModifier;
        this.privateFieldOptionse = privateFieldOptionse;

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
                ObjectType type = TypeToObjectTypeConverter.convert(type1);
                filedStr1 = fieldCreationStrategy.getFieldFor(type, accessModifier, nextKey);
                generateClassForObject(json, nextKey, type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            filedStr = filedStr1 + "\n";
            if (privateFieldOptionse.contains(PrivateFieldOptions.PROVIDE_PRIVATE_FIELD)) {
                psiClass = new EncapsulatedClassCreator(privateFieldOptionse).getClassWithEncapsulatedFileds(psiClass);
            }
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
                ObjectType type = TypeToObjectTypeConverter.convert(type1);
                filedStr1 = fieldCreationStrategy.getFieldFor(type, accessModifier, nextKey);
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
        if (privateFieldOptionse.contains(PrivateFieldOptions.PROVIDE_PRIVATE_FIELD)) {
            aClass = new EncapsulatedClassCreator(privateFieldOptionse).getClassWithEncapsulatedFileds(aClass);
        }
        classAdderStrategy.addClass(aClass);
    }


}
