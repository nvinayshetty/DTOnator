package com.nvinayshetty.DTOnator.ActionListener;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilBase;
import com.nvinayshetty.DTOnator.Ui.InputWindow;

/**
 * Created by vinay on 11/4/15.
 */
public class UserActionListener extends AnAction {

    private PsiClass mClass;
    private Project project;

    public UserActionListener() {
        super();
    }

    public void actionPerformed(AnActionEvent event) {
        project = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
        PsiFile mFile = PsiUtilBase.getPsiFileInEditor(editor, project);
        mClass = getPsiClassFromContext(event);

        InputWindow dialog = new InputWindow(mClass, project, mFile);
        dialog.setVisible(true);
    }

    @Override
    public void update(AnActionEvent event) {
        PsiClass psiClass = getPsiClassFromContext(event);
        Presentation presentation = event.getPresentation();
        presentation.setEnabled(isGenerateDtoOptionEnabled(psiClass));
    }

    private boolean isGenerateDtoOptionEnabled(PsiClass psiClass) {
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
