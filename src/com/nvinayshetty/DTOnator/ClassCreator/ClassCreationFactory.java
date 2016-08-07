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
