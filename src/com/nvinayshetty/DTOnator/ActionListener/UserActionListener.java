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

package com.nvinayshetty.DTOnator.ActionListener;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.nvinayshetty.DTOnator.ClassCreator.ClassType;
import com.nvinayshetty.DTOnator.DtoCreationOptions.DtoGenerationFactory;
import com.nvinayshetty.DTOnator.DtoCreationOptions.FeedType;
import com.nvinayshetty.DTOnator.DtoCreationOptions.FieldEncapsulationOptions;
import com.nvinayshetty.DTOnator.DtoCreationOptions.FieldType;
import com.nvinayshetty.DTOnator.NameConventionCommands.NameParserCommand;
import com.nvinayshetty.DTOnator.Ui.JsonInputEditorPane;
import com.nvinayshetty.DTOnator.Ui.TabbedInputWindow;
import com.nvinayshetty.DTOnator.nameConflictResolvers.NameConflictResolverCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.asJava.elements.KtLightElement;
import org.jetbrains.kotlin.idea.KotlinLanguage;
import org.jetbrains.kotlin.idea.internal.Location;
import org.jetbrains.kotlin.psi.KtClass;

import java.util.EnumSet;
import java.util.HashSet;

/**
 * Created by vinay on 11/4/15.
 */
public class UserActionListener extends AnAction implements JsonInputEditorPane.GenerateClickListener {
    private KtClass ktClass;
    private PsiClass mClass;
    private Editor editor;
    private PsiFile psiFile;

    public UserActionListener() {
        super();
    }

    public static KtClass getKtClassForElement(@NotNull PsiElement psiElement) {
        if (psiElement instanceof KtLightElement) {
            PsiElement origin = ((KtLightElement) psiElement).getKotlinOrigin();
            if (origin != null) {
                return getKtClassForElement(origin);
            } else {
                return null;
            }

        } else if (psiElement instanceof KtClass && !((KtClass) psiElement).isEnum() &&
                !((KtClass) psiElement).isInterface() &&
                !((KtClass) psiElement).isAnnotation() &&
                !((KtClass) psiElement).isSealed()) {
            return (KtClass) psiElement;

        } else {
            PsiElement parent = psiElement.getParent();
            if (parent == null) {
                return null;
            } else {
                return getKtClassForElement(parent);
            }
        }
    }

    public void actionPerformed(AnActionEvent event) {
        mClass = getPsiClassFromContext(event);
        ktClass = getKtClassFromContext(event);
        TabbedInputWindow tabbedInputWindow = new TabbedInputWindow(event.getProject(), psiFile, this);
        tabbedInputWindow.setVisible(true);
       /* if (ktClass != null) {
            psiFile = event.getData(LangDataKeys.PSI_FILE);
            editor = event.getData(PlatformDataKeys.EDITOR);
            final Location location = Location.fromEditor(editor, editor.getProject());
            final PsiElement psiElement = psiFile.findElementAt(location.getStartOffset());
            KotlinInputWindow kotlinInputWindow = new KotlinInputWindow(ktClass,psiElement);
            kotlinInputWindow.show();
        } else {
            InputWindow dialog = new InputWindow(mClass);
            dialog.setVisible(true);
        }*/
    }

    @Override
    public void update(AnActionEvent event) {
        PsiClass psiClass = getPsiClassFromContext(event);
        KtClass ktClass = getKtClassFromContext(event);
        Presentation presentation = event.getPresentation();
        if (psiClass != null) {
            presentation.setEnabled(isGenerateDtoOptionEnabled(psiClass));
        }

        if (ktClass != null) {
            presentation.setEnabled(isGenerateDtoOptionForKtEnabled(ktClass));
        }
    }

    private boolean isGenerateDtoOptionEnabled(PsiClass psiClass) {
        return psiClass != null;
    }

    private PsiClass getPsiClassFromContext(AnActionEvent e) {
        psiFile = e.getData(LangDataKeys.PSI_FILE);
        editor = e.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            return null;
        }
        int offset = editor.getCaretModel().getOffset();
        PsiElement elementAt = psiFile.findElementAt(offset);
        PsiClass psiClass = PsiTreeUtil.getParentOfType(elementAt, PsiClass.class);
        return psiClass;
    }

    private KtClass getKtClassFromContext(AnActionEvent e) {

        final PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        final Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            return null;
        }

        final Location location = Location.fromEditor(editor, editor.getProject());
        final PsiElement psiElement = psiFile.findElementAt(location.getStartOffset());
        if (psiFile.getLanguage() != KotlinLanguage.INSTANCE) return null;
        KtClass ktClass = getKtClassForElement(psiElement);
        return ktClass;

    }

    private boolean isGenerateDtoOptionForKtEnabled(KtClass ktClass) {
        return ktClass != null && !ktClass.isEnum() && !ktClass.isInterface() && !ktClass.isAnnotation() && !ktClass.isSealed();
    }


    @Override
    public void onGenerateButtonClick(FieldType fieldType, ClassType classType, EnumSet<FieldEncapsulationOptions> fieldEncapsulationOptions, String input) {
        WriteCommandAction writeAction = null;
        if (mClass != null) {
            writeAction = DtoGenerationFactory.getDtoGeneratorFor(FeedType.JSON, classType, fieldType, fieldEncapsulationOptions, input, mClass, new HashSet<NameConflictResolverCommand>(), new HashSet<NameParserCommand>());
        }
        if (ktClass != null) {
            writeAction = DtoGenerationFactory.getDtoGeneratorForKotlin(FeedType.JSON, classType, fieldType, fieldEncapsulationOptions, input, ktClass, new HashSet<NameConflictResolverCommand>(), new HashSet<NameParserCommand>());
        }
        if (writeAction != null)
            writeAction.execute();
    }
}
