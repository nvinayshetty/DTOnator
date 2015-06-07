package com.nvinayshetty.DTOnator.ClassCreator;

import com.intellij.psi.PsiClass;

/**
 * Created by vinay on 7/6/15.
 */
public class ClassCreationFactory {
    public static ClassCreatorStrategy getFileCreatorFor(ClassType classType, PsiClass psiClass) {
        switch (classType) {
            case SEPARATE_FILE:
                return new PublicClassCreator(psiClass.getContainingFile().getContainingDirectory());
            case SINGLE_FILE_WITH_INNER_CLASS:
                return new StaticClassCreator(psiClass);
        }
        return null;
    }
}
