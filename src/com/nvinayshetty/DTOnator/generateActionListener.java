package com.nvinayshetty.DTOnator;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * Created by vinay on 11/4/15.
 */
public class generateActionListener extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
    }

    @Override
    public void update(AnActionEvent event) {
        PsiClass psiClass = getPsiClassFromContext(event);
        Presentation presentation = event.getPresentation();
        presentation.setEnabled(isDtoGenerationEnabled(psiClass));
    }

    private boolean isDtoGenerationEnabled(PsiClass psiClass) {
        return psiClass != null;
    }


    private PsiClass getPsiClassFromContext(AnActionEvent e) {
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if (psiFile == null || editor == null) {
            return null;
        }
        int offset = editor.getCaretModel().getOffset();
        PsiElement elementAt = psiFile.findElementAt(offset);
        PsiClass psiClass = PsiTreeUtil.getParentOfType(elementAt, PsiClass.class);
        return psiClass;
    }
}
