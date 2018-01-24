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

import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.nvinayshetty.DTOnator.FieldCreator.ExposedGsonFieldCreator;
import com.nvinayshetty.DTOnator.FieldCreator.FieldCreationStrategy;
import org.jetbrains.kotlin.name.FqName;
import org.jetbrains.kotlin.psi.KtClass;
import org.jetbrains.kotlin.psi.KtImportDirective;
import org.jetbrains.kotlin.psi.KtPackageDirective;
import org.jetbrains.kotlin.psi.KtPsiFactory;
import org.jetbrains.kotlin.resolve.ImportPath;

public class publicKotlinClassCreator extends KotlinClassCreationStrategy {
    PsiDirectory pkgOfClassUnderCaret;


    public publicKotlinClassCreator(PsiDirectory aPackage) {
        pkgOfClassUnderCaret = aPackage;
    }

    @Override
    public void addClass(KtClass psiClass, FieldCreationStrategy fieldCreationStrategy) {
        String name = psiClass.getName() + ".kt";
        KtPsiFactory ktPsiFactory = new KtPsiFactory(psiClass.getProject());

        String qualifiedName = JavaDirectoryService.getInstance().getPackage(pkgOfClassUnderCaret).getQualifiedName();
        KtPackageDirective packageDirective = ktPsiFactory.createPackageDirective(new FqName(qualifiedName));
        PsiFile file = pkgOfClassUnderCaret.createFile(name);

        file.add(packageDirective);

      if(!fieldCreationStrategy.getImportDirective().isEmpty()) {
          FqName fqName = new FqName(fieldCreationStrategy.getImportDirective());
          ImportPath importPath = new ImportPath(fqName, false);
          KtImportDirective importDirective = ktPsiFactory.createImportDirective(importPath);
          psiClass.getContainingKtFile().getImportList().add(importDirective);
          if (fieldCreationStrategy instanceof ExposedGsonFieldCreator) {
              //Todo: make interface to return list of directives
              FqName serialized = new FqName("com.google.gson.annotations.SerializedName");
              ImportPath serializedPath = new ImportPath(serialized, false);
              KtImportDirective serializedDirective = ktPsiFactory.createImportDirective(serializedPath);
              psiClass.getContainingKtFile().getImportList().add(serializedDirective);

          }
          file.add(ktPsiFactory.createNewLine(2));
      }
        file.add(psiClass);
        organizeCodeStyle(psiClass);
    }
}
