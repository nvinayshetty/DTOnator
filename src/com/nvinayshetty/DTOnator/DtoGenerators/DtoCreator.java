package com.nvinayshetty.DTOnator.DtoGenerators;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.nvinayshetty.DTOnator.Factory.DtoGenerationFactory;
import com.nvinayshetty.DTOnator.validator.InputFeedValidationFactory;
import org.json.JSONObject;

import javax.swing.*;

/**
 * Created by vinay on 31/5/15.
 */
public class DtoCreator {
    private String inputFeed;
    private Project project;
    private PsiFile psiFile;
    private PsiClass psiClass;
    private JLabel exceptionLabel;
    private DtoFileCreationPreference filePreference;

    public DtoCreator(String jsonStr, JLabel exceptionLabel, DtoFileCreationPreference fileCreationPreference, Project project, PsiFile file, PsiClass psiClass) {
        this.inputFeed = jsonStr;
        this.project = project;
        this.psiFile = file;
        this.psiClass = psiClass;
        this.exceptionLabel = exceptionLabel;
        this.filePreference = fileCreationPreference;

    }

    public void createDto() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                InputFeedValidationFactory validator = new InputFeedValidationFactory(FeedType.JsonObject, exceptionLabel);
                if (validator.isValidFeed(inputFeed, exceptionLabel)) {
                    DtoGenerationFactory.getDtoGeneratorFor(FeedType.JsonObject, project, psiFile, (JSONObject) validator.getValidFeed(), psiClass);
                    //foo.generateDto(project,psiFile,validator.getValidFeed(),psiClass);
                    JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
                    styleManager.optimizeImports(psiFile);
                    styleManager.shortenClassReferences(psiClass);
                }
            }
        });
    }
}
