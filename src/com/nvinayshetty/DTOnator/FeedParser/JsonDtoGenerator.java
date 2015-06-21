package com.nvinayshetty.DTOnator.FeedParser;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.nvinayshetty.DTOnator.ClassCreator.EncapsulatedClassCreator;
import com.nvinayshetty.DTOnator.DtoCreators.DtoCreater;
import com.nvinayshetty.DTOnator.DtoCreators.FieldEncapsulationOptions;
import com.nvinayshetty.DTOnator.FieldCreator.ObjectType;
import com.nvinayshetty.DTOnator.FieldCreator.feedTypeToJavaTypeConverter;
import com.nvinayshetty.DTOnator.Ui.FeedProgressDialog;
import com.nvinayshetty.DTOnator.Utility.DtoHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


/**
 * Created by vinay on 19/4/15.
 */
public class JsonDtoGenerator extends WriteCommandAction.Simple {
    private static JsonDtoBuilder jsonDtoBuilder;
    private PsiClass psiClass;
    private JSONObject json;
    private PsiElementFactory psiFactory;
    private DtoCreater dtoCreater;

    private JsonDtoGenerator(JsonDtoBuilder builder) {
        super(builder.aClass.getProject(), builder.aClass.getContainingFile());
        psiFactory = JavaPsiFacade.getElementFactory(builder.aClass.getProject());
        json = builder.jsonString;
        psiClass = builder.aClass;
        dtoCreater = builder.dtoCreater;
    }

    public static JsonDtoBuilder getJsonDtoBuilder() {
        return jsonDtoBuilder = new JsonDtoBuilder();
    }

    @Override
    protected void run() {
        addFieldsToTheTopLevelClass(json);
    }

    private void addFieldsToTheTopLevelClass(JSONObject json) {
        Iterator<?> keysIterator = json.keys();
        String filedStr = null;
        while (keysIterator.hasNext()) {
            filedStr = getClassFields(json, keysIterator) + "\n";
            psiClass.add(psiFactory.createFieldFromText(filedStr, psiClass));
        }
        if (dtoCreater.getPrivateFieldOptionse().contains(FieldEncapsulationOptions.PROVIDE_PRIVATE_FIELD)) {
            psiClass = new EncapsulatedClassCreator(dtoCreater.getPrivateFieldOptionse()).getClassWithEncapsulatedFileds(psiClass);
        }
    }

    private String getAllFieldsOf(JSONObject json) {
        //Todo:String builder is used as the compliler can't optimize the String to internally use string Builder,Verify
        StringBuilder subClassFields = new StringBuilder();
        String filedStr = null;
        Iterator<?> keysIterator = json.keys();
        while (keysIterator.hasNext()) {
            filedStr = getClassFields(json, keysIterator);
            subClassFields.append(filedStr + "\n");
        }
        return subClassFields.toString();
    }

    private String getClassFields(JSONObject json, Iterator<?> keysIterator) {
        String nextKey = (String) keysIterator.next();
        String filedStr1 = "";
        try {
            Object object = json.get(nextKey);
            String type1 = object.getClass().getSimpleName();
            ObjectType type = feedTypeToJavaTypeConverter.convert(type1);
            filedStr1 = dtoCreater.getFieldCreationStrategy().getFieldFor(type, dtoCreater.getAccessModifier(), nextKey);
            generateClassForObject(json, nextKey, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return filedStr1;
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
        if (dtoCreater.getPrivateFieldOptionse().contains(FieldEncapsulationOptions.PROVIDE_PRIVATE_FIELD)) {
            aClass = new EncapsulatedClassCreator(dtoCreater.getPrivateFieldOptionse()).getClassWithEncapsulatedFileds(aClass);
        }
        dtoCreater.getClassAdderStrategy().addClass(aClass);
    }

    public static class JsonDtoBuilder {
        private JSONObject jsonString;
        private PsiClass aClass;
        private DtoCreater dtoCreater;
        private FeedProgressDialog dlg;

        public JsonDtoBuilder setJsonString(JSONObject jsonString) {
            this.jsonString = jsonString;
            return this;
        }

        public JsonDtoBuilder setaClass(PsiClass aClass) {
            this.aClass = aClass;
            return this;
        }

        public JsonDtoBuilder setDtoCreater(DtoCreater dtoCreater) {
            this.dtoCreater = dtoCreater;
            return this;
        }

        public JsonDtoBuilder setDlg(FeedProgressDialog dlg) {
            this.dlg = dlg;
            return this;
        }

        public JsonDtoGenerator createJsonDtoGenerator() {
            return new JsonDtoGenerator(this);
        }
    }


}
