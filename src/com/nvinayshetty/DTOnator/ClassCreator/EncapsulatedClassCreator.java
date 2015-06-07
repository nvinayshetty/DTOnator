package com.nvinayshetty.DTOnator.ClassCreator;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.nvinayshetty.DTOnator.DtoCreators.PrivateFieldOptions;

import java.util.EnumSet;

/**
 * Created by vinay on 7/6/15.
 */
public class EncapsulatedClassCreator {
    EnumSet<PrivateFieldOptions> privateFieldOptions;


    public EncapsulatedClassCreator(EnumSet<PrivateFieldOptions> privateFieldOptions) {
        this.privateFieldOptions = privateFieldOptions;
    }

    public PsiClass getClassWithEncapsulatedFileds(PsiClass aClass) {
        if (isBothGetterSetterOptiosAreChecked())
            encapsulate(aClass);
        else if (isGetterOptionChecked())
            addAccesor(aClass);
        else if (isSetterOptionChecked())
            addMutator(aClass);
        return aClass;
    }

    private boolean isSetterOptionChecked() {
        return privateFieldOptions.contains(PrivateFieldOptions.PROVIDE_SETTER);
    }

    private boolean isGetterOptionChecked() {
        return privateFieldOptions.contains(PrivateFieldOptions.PROVIDE_GETTER);
    }

    private boolean isBothGetterSetterOptiosAreChecked() {
        return isGetterOptionChecked() && isSetterOptionChecked();
    }

    private PsiClass addMutator(PsiClass aClass) {
        PsiField[] psiFields = aClass.getAllFields();
        for (int i = 0; i <= psiFields.length - 1; i++) {
            PsiElementFactory factory = JavaPsiFacade.getElementFactory(aClass.getProject());
            String method = generateSetter(psiFields[i]);
            aClass.add(factory.createMethodFromText(method, aClass));
        }
        return aClass;
    }

    private String generateSetter(PsiField psiField) {
        return "public void" + " set" + psiField.getName() + "(" + psiField.getType().getPresentableText() + " " + psiField.getName() + ") { this." + psiField.getName() + "=" + psiField.getName() + ";} ";
    }

    private PsiClass addAccesor(PsiClass aClass) {
        PsiField[] psiFields = aClass.getAllFields();
        for (int i = 0; i <= psiFields.length - 1; i++) {
            PsiElementFactory factory = JavaPsiFacade.getElementFactory(aClass.getProject());
            String method = generateGetter(psiFields[i]);
            aClass.add(factory.createMethodFromText(method, aClass));
        }
        return aClass;
    }

    private String generateGetter(PsiField psiField) {
        return "public " + psiField.getType().getPresentableText() + " get" + psiField.getName() + "() { return " + psiField.getName() + ";} ";
    }

    private PsiClass encapsulate(PsiClass aClass) {
        PsiField[] psiFields = aClass.getAllFields();
        for (int i = 0; i <= psiFields.length - 1; i++) {
            PsiElementFactory factory = JavaPsiFacade.getElementFactory(aClass.getProject());
            String getter = generateGetter(psiFields[i]);
            aClass.add(factory.createMethodFromText(getter, aClass));
            String setter = generateSetter(psiFields[i]);
            aClass.add(factory.createMethodFromText(setter, aClass));
        }
        return aClass;
    }
}




