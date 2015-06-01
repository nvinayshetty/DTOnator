package com.nvinayshetty.DTOnator.ClassAdder;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;

/**
 * Created by vinay on 31/5/15.
 */
public class AddToPackage implements ClassAdder {
    PsiDirectory pkgOfClassUnderCaret;

    public AddToPackage(PsiDirectory aPackage) {
        pkgOfClassUnderCaret = aPackage;
    }

    @Override
    public void addClass(PsiClass aClass) {
        pkgOfClassUnderCaret.add(aClass);

    }
}
