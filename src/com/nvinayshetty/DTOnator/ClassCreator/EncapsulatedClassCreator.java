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

package nvinayshetty.DTOnator.ClassCreator;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import nvinayshetty.DTOnator.DtoCreationOptions.FieldEncapsulationOptions;
import nvinayshetty.DTOnator.NameConventionCommands.FieldNameParser;

import java.util.EnumSet;

/**
 * Created by vinay on 7/6/15.
 */
public class EncapsulatedClassCreator {
    EnumSet<FieldEncapsulationOptions> fieldEncapsulationOptions;
    FieldNameParser nameParser;

    public EncapsulatedClassCreator(EnumSet<FieldEncapsulationOptions> fieldEncapsulationOptions, FieldNameParser nameParser) {
        this.fieldEncapsulationOptions = fieldEncapsulationOptions;
        this.nameParser = nameParser;
    }

    public PsiClass getClassWithEncapsulatedFileds(PsiClass aClass) {
        if (aClass != null) {
            if (isBothGetterSetterOptionsAreChecked())
                encapsulate(aClass);
            else if (isGetterOptionChecked())
                addAccesor(aClass);
            else if (isSetterOptionChecked())
                addMutator(aClass);
        }
        return aClass;
    }

    private boolean isSetterOptionChecked() {
        return fieldEncapsulationOptions.contains(FieldEncapsulationOptions.PROVIDE_SETTER);
    }

    private boolean isGetterOptionChecked() {
        return fieldEncapsulationOptions.contains(FieldEncapsulationOptions.PROVIDE_GETTER);
    }

    private boolean isBothGetterSetterOptionsAreChecked() {
        return isGetterOptionChecked() && isSetterOptionChecked();
    }

    private PsiClass addMutator(PsiClass aClass) {
        PsiField[] psiFields = aClass.getFields();
        for (PsiField psiField : psiFields) {
            PsiElementFactory factory = JavaPsiFacade.getElementFactory(aClass.getProject());
            String method = generateSetter(psiField);
            aClass.add(factory.createMethodFromText(method, aClass));
        }
        return aClass;
    }

    private String generateSetter(PsiField psiField) {
        String name = psiField.getName();
        name = nameParser.undo(name);
        return "public void" + " set" + firstLetterToUpperCase(name) + "(" + psiField.getType().getPresentableText() + " " + psiField.getName() + ") { this." + psiField.getName() + "=" + psiField.getName() + ";} ";
    }

    private PsiClass addAccesor(PsiClass aClass) {
        PsiField[] psiFields = aClass.getFields();
        for (PsiField psiField : psiFields) {
            PsiElementFactory factory = JavaPsiFacade.getElementFactory(aClass.getProject());
            String method = generateGetter(psiField);
            aClass.add(factory.createMethodFromText(method, aClass));
        }
        return aClass;
    }

    public String generateGetter(PsiField psiField) {
        String name = psiField.getName();
        name = nameParser.undo(name);
        return "public " + psiField.getType().getPresentableText() + " get" + firstLetterToUpperCase(name) + "() { return " + psiField.getName() + ";} ";
    }

    private String firstLetterToUpperCase(String name) {
        return Character.toUpperCase(
                name.charAt(0)) + (name.length() > 1 ? name.substring(1) : "");
    }

    private PsiClass encapsulate(PsiClass aClass) {
        PsiField[] psiFields = aClass.getFields();
        for (PsiField psiField : psiFields) {
            PsiElementFactory factory = JavaPsiFacade.getElementFactory(aClass.getProject());
            String getter = generateGetter(psiField);
            aClass.add(factory.createMethodFromText(getter, aClass));
            String setter = generateSetter(psiField);
            aClass.add(factory.createMethodFromText(setter, aClass));
        }
        return aClass;
    }
}




