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

import com.intellij.json.JsonLanguage;
import com.intellij.json.formatter.JsonLanguageCodeStyleSettingsProvider;
import com.intellij.psi.PsiClass;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import org.jetbrains.kotlin.idea.util.ImportInsertHelper;

/**
 * Created by vinay on 31/5/15.
 */
public abstract class ClassCreatorStrategy {

    public abstract void addClass(PsiClass psiClass);

    public void organizeCodeStyle(PsiClass aClass) {
        JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(aClass.getProject());
        styleManager.optimizeImports(aClass.getContainingFile());
        styleManager.shortenClassReferences(aClass);
        CodeStyleManager codeStyleManager=CodeStyleManager.getInstance(aClass.getProject());
        codeStyleManager.reformat(aClass);

    }
}
