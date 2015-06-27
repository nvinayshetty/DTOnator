package com.nvinayshetty.DTOnator.FeedParser;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.nvinayshetty.DTOnator.ClassCreator.EncapsulatedClassCreator;
import com.nvinayshetty.DTOnator.DtoCreators.DtoCreationOptions;
import com.nvinayshetty.DTOnator.DtoCreators.FieldEncapsulationOptions;
import com.nvinayshetty.DTOnator.FieldCreator.FieldRepresentor;
import com.nvinayshetty.DTOnator.FieldCreator.JavaFieldFactory;
import com.nvinayshetty.DTOnator.Ui.FeedProgressDialog;
import com.nvinayshetty.DTOnator.Utility.DtoHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


/**
 * Created by vinay on 19/4/15.
 */
public class JsonDtoGenerator extends WriteCommandAction.Simple {
    public final static String ARRARY_SPECIAL_FLAG = "-|||-";
    private static JsonDtoBuilder jsonDtoBuilder;
    private PsiClass classUnderCaret;
    private JSONObject json;
    private PsiElementFactory psiFactory;
    private DtoCreationOptions dtoCreationOptions;

    private JsonDtoGenerator(JsonDtoBuilder builder) {
        super(builder.classUnderCaret.getProject(), builder.classUnderCaret.getContainingFile());
        psiFactory = JavaPsiFacade.getElementFactory(builder.classUnderCaret.getProject());
        json = builder.json;
        classUnderCaret = builder.classUnderCaret;
        dtoCreationOptions = builder.dtoCreationOptions;
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
        while (keysIterator.hasNext()) {
            String filedStr = getClassFields(json, keysIterator) /*+ "\n\n"*/;
            classUnderCaret.add(psiFactory.createFieldFromText(filedStr, classUnderCaret));
        }
        if (dtoCreationOptions.getPrivateFieldOptionse().contains(FieldEncapsulationOptions.PROVIDE_PRIVATE_FIELD)) {
           /* classUnderCaret =*/
            new EncapsulatedClassCreator(dtoCreationOptions.getPrivateFieldOptionse()).getClassWithEncapsulatedFileds(classUnderCaret);
        }
    }

    private String getAllFieldsOf(JSONObject json) {
        //Todo:String builder is used as the compliler can't optimize the String to internally use string Builder,Verify
        StringBuilder subClassFields = new StringBuilder();
        Iterator<?> keysIterator = json.keys();
        while (keysIterator.hasNext()) {
            String filedStr = getClassFields(json, keysIterator);
            subClassFields.append(filedStr /*+ "\n"*/);
        }
        return subClassFields.toString();
    }

    private String getClassFields(JSONObject json, Iterator<?> keysIterator) {
        String key = (String) keysIterator.next();
        String keyType = key;
        String filedStr1 = "";
        try {
            Object object = json.get(key);
            String dataType = object.getClass().getSimpleName();
            FieldRepresentor fieldRepresentor = JavaFieldFactory.convert(dataType);
            if (object instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) object;
                Object obj = jsonArray.get(0);
                if (!(obj instanceof JSONObject) && !(obj instanceof JSONArray)) {
                    keyType = obj.getClass().getSimpleName().trim() + "111" + key;
                }
            }
            filedStr1 = dtoCreationOptions.getFieldCreationStrategy().getFieldFor(fieldRepresentor, dtoCreationOptions.getAccessModifier(), keyType);
            generateClassForObject(json, key, fieldRepresentor);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return filedStr1 + "\n";
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

    private void addClass(String key, JSONObject jsonObject) {
        String className = DtoHelper.getSubClassName(key);
        String classContent = getAllFieldsOf(jsonObject);
        PsiClass aClass = psiFactory.createClassFromText(classContent.trim(), classUnderCaret);
        aClass.setName(className.trim());
        if (dtoCreationOptions.getPrivateFieldOptionse().contains(FieldEncapsulationOptions.PROVIDE_PRIVATE_FIELD)) {
            aClass = new EncapsulatedClassCreator(dtoCreationOptions.getPrivateFieldOptionse()).getClassWithEncapsulatedFileds(aClass);
        }
        dtoCreationOptions.getClassAdderStrategy().addClass(aClass);
    }

    public static class JsonDtoBuilder {
        private JSONObject json;
        private PsiClass classUnderCaret;
        private DtoCreationOptions dtoCreationOptions;
        private FeedProgressDialog dlg;

        public JsonDtoBuilder setJson(JSONObject json) {
            this.json = json;
            return this;
        }

        public JsonDtoBuilder setClassUnderCaret(PsiClass classUnderCaret) {
            this.classUnderCaret = classUnderCaret;
            return this;
        }

        public JsonDtoBuilder setDtoCreationOptions(DtoCreationOptions dtoCreationOptions) {
            this.dtoCreationOptions = dtoCreationOptions;
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
