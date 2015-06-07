package com.nvinayshetty.DTOnator.ClassCreator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiModifier;

/**
 * Created by vinay on 31/5/15.
 */
public class StaticClassCreator extends ClassCreatorStrategy {
    private PsiClass classUnderCaret;

    public StaticClassCreator(PsiClass psiClass) {
        this.classUnderCaret = psiClass;
    }

    @Override
    public void addClass(PsiClass aClass) {
        aClass.getModifierList().setModifierProperty(PsiModifier.STATIC, true);
        aClass.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);
        organizeCodeStyle(aClass);
        classUnderCaret.add(aClass);

    }


}
