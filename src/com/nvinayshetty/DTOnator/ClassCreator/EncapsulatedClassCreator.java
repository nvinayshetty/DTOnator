package com.nvinayshetty.DTOnator.ClassCreator;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.nvinayshetty.DTOnator.DtoCreators.FieldEncapsulatopnOptions;

import java.util.EnumSet;

/**
 * Created by vinay on 7/6/15.
 */
public class EncapsulatedClassCreator {
    EnumSet<FieldEncapsulatopnOptions> fieldEncapsulationOptions;


    public EncapsulatedClassCreator(EnumSet<FieldEncapsulatopnOptions> fieldEncapsulationOptions) {
        this.fieldEncapsulationOptions = fieldEncapsulationOptions;
    }

    public PsiClass getClassWithEncapsulatedFileds(PsiClass aClass) {
        if (aClass != null)
        if (isBothGetterSetterOptiosAreChecked())
            encapsulate(aClass);
        else if (isGetterOptionChecked())
            addAccesor(aClass);
        else if (isSetterOptionChecked())
            addMutator(aClass);
        return aClass;
    }

    private boolean isSetterOptionChecked() {
        return fieldEncapsulationOptions.contains(FieldEncapsulatopnOptions.PROVIDE_SETTER);
    }

    private boolean isGetterOptionChecked() {
        return fieldEncapsulationOptions.contains(FieldEncapsulatopnOptions.PROVIDE_GETTER);
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
        return "public void" + " set" + firstLetterToUpperCase(psiField.getName()) + "(" + psiField.getType().getPresentableText() + " " + psiField.getName() + ") { this." + psiField.getName() + "=" + psiField.getName() + ";} ";
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

    public String generateGetter(PsiField psiField) {
        return "public " + psiField.getType().getPresentableText() + " get" + firstLetterToUpperCase(psiField.getName()) + "() { return " + psiField.getName() + ";} ";
    }

    private String firstLetterToUpperCase(String name) {
        return Character.toUpperCase(
                name.charAt(0)) + (name.length() > 1 ? name.substring(1) : "");
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




