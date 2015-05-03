package com.nvinayshetty.DTOnator;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


/**
 * Created by vinay on 19/4/15.
 */
public class DtoGenerator extends WriteCommandAction.Simple {
    protected PsiClass psiClass;
    private String jsonString;
    private PsiElementFactory psiFactory;
    private Project project;
    private PsiFile psiFile;

    protected DtoGenerator(Project project, PsiFile file, String jsonString, PsiClass mClass, PsiFile... files) {
        super(project, files);
        this.project = project;
        this.psiFactory = JavaPsiFacade.getElementFactory(project);
        this.jsonString = jsonString;
        this.psiClass = mClass;
        this.psiFile = file;
    }

    @Override
    protected void run() throws Throwable {
        JSONObject json = new JSONObject(jsonString);
        createField(json, psiClass);
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
        styleManager.optimizeImports(psiFile);
        styleManager.shortenClassReferences(psiClass);

    }

    private void createField(JSONObject json, PsiClass klass) {
        Iterator<?> keysIterator = json.keys();
        while (keysIterator.hasNext()) {
            try {
                String key = (String) keysIterator.next();
                String type = json.get(key).getClass().getSimpleName();
                String typeStr;
                if (type.equals("Boolean")) {
                    typeStr = " boolean ";
                } else if (type.equals("Integer")) {
                    typeStr = " int ";
                } else if (type.equals("Double")) {
                    typeStr = " double ";
                } else if (type.equals("JSONObject")) {
                    typeStr = " " + createClassSubName(key) + " ";
                    createSubClass(typeStr, json.getJSONObject(key));

                } else if (type.equals("JSONArray")) {

                    typeStr = " java.util.List<" + createClassSubName(key) + "> ";
                    String className = createClassSubName(key);
                    JSONObject jsonObject = (JSONObject) json.getJSONArray(key).get(0);
                    createSubClass(className, jsonObject);

                } else {
                    typeStr = " String ";
                }
                String annotation = "@SerializedName(" + "\"" + key + "\"" + ")" + "\n";
                String filedStr = annotation + "private  " + typeStr + key + " ; " + "\n";

                klass.add(psiFactory.createFieldFromText(filedStr, klass));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void createSubClass(String className, JSONObject jsonObject) {
        StringBuilder classContent = new StringBuilder();
        classContent.append("public  class").append(className).append("{ }");

        PsiClass innerclass = psiFactory.createClass(className.trim());
        innerclass.getModifierList().setModifierProperty("static", true);
        createField(jsonObject, innerclass);

        psiClass.add(innerclass);
        // psiClass.addBefore(psiFactory.createKeyword("static", innerclass), psiClass);


        // innerclass.setName(className.trim());
        //PsiClass innerclass= psiFactory.createClassFromText(classContent.toString(), psiClass);
        // psiClass.addBefore(psiFactory.createKeyword("static", innerclass), psiClass);
        //mClass.addBefore(mFactory.createKeyword("static", mClass), mClass.findInnerClassByName(Utils.getViewHolderClassName(), true));
//psiFactory.createClass(classContent.toString());


    }

    private String createClassSubName(String key) {
        return key.substring(0, 1).toUpperCase() + key.substring(1);
    }

}
