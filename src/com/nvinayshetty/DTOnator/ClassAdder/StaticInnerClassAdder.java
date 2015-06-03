package com.nvinayshetty.DTOnator.ClassAdder;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiModifier;

/**
 * Created by vinay on 31/5/15.
 */
public class StaticInnerClassAdder implements ClassAdderStrategy {
    private PsiClass classUnderCaret;

    public StaticInnerClassAdder(PsiClass psiClass) {
        this.classUnderCaret = psiClass;
    }

    @Override
    public void addClass(PsiClass aClass) {
        aClass.getModifierList().setModifierProperty(PsiModifier.STATIC, true);
        classUnderCaret.add(aClass);

    }
}
