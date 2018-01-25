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
        int length = classUnderCaret.getFields().length - 1;
        length = (length == -1) ? -1 : length;

        if (length != -1) {
            classUnderCaret.addAfter(aClass, classUnderCaret.getFields()[length]);
        } else {
            int methosLength = classUnderCaret.getMethods().length;
            methosLength = (methosLength == -1) ? 0 : methosLength;
            if(methosLength!=-1) {
                classUnderCaret.addAfter(aClass, classUnderCaret.getMethods()[methosLength - 1]);
            }else{
                classUnderCaret.add(aClass);
            }
        }
        organizeCodeStyle(classUnderCaret);

    }


}
