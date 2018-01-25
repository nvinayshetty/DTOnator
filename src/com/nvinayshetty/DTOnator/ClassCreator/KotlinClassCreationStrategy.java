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

import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.nvinayshetty.DTOnator.FieldCreator.FieldCreationStrategy;
import org.jetbrains.kotlin.codegen.KotlinCodegenFacade;
import org.jetbrains.kotlin.idea.KotlinLanguage;
import org.jetbrains.kotlin.idea.core.formatter.KotlinCodeStyleSettings;
import org.jetbrains.kotlin.psi.KtClass;

public abstract class KotlinClassCreationStrategy {


    public abstract void addClass(KtClass psiClass, FieldCreationStrategy fieldCreationStrategy);

    public void organizeCodeStyle(KtClass aClass) {
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(aClass.getProject());
        styleManager.optimizeImports(aClass.getContainingKtFile());
        styleManager.shortenClassReferences(aClass.getContainingKtFile());

    }
}
