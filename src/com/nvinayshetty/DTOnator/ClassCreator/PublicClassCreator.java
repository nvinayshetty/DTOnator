package com.nvinayshetty.DTOnator.ClassCreator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiModifier;

/**
 * Created by vinay on 31/5/15.
 */
public class PublicClassCreator extends ClassCreatorStrategy {
    PsiDirectory pkgOfClassUnderCaret;

    public PublicClassCreator(PsiDirectory aPackage) {
        pkgOfClassUnderCaret = aPackage;
    }

    @Override
    public void addClass(PsiClass aClass) {
        aClass.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);
        organizeCodeStyle(aClass);
        pkgOfClassUnderCaret.add(aClass);

    }
}
