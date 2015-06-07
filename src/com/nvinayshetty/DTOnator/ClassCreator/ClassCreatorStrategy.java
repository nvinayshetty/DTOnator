package com.nvinayshetty.DTOnator.ClassCreator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

/**
 * Created by vinay on 31/5/15.
 */
public abstract class ClassCreatorStrategy {

    public abstract void addClass(PsiClass psiClass);

    public void organizeCodeStyle(PsiClass aClass) {
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(aClass.getProject());
        styleManager.optimizeImports(aClass.getContainingFile());
        styleManager.shortenClassReferences(aClass);
    }
}
