package com.nvinayshetty.DTOnator.DtoCreators;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.nvinayshetty.DTOnator.ClassCreator.ClassType;
import com.nvinayshetty.DTOnator.validator.InputFeedValidationFactory;
import org.json.JSONObject;

import javax.swing.*;
import java.util.EnumSet;

/**
 * Created by vinay on 31/5/15.
 */
public class DtoCreator {
    private String inputFeed;
    private PsiFile psiFile;
    private PsiClass psiClass;
    private Project project;
    private JLabel exceptionLabel;
    private ClassType classType;
    private FeedType feedType;
    private FieldType fieldType;
    private EnumSet<PrivateFieldOptions> privateFieldOptions;


    public DtoCreator(String inputFeed, JLabel exceptionLabel, FeedType feedType, ClassType filetype, FieldType fieldType, EnumSet<PrivateFieldOptions> privateFieldOptions, PsiClass psiClass) {
        this.inputFeed = inputFeed;
        this.psiClass = psiClass;
        this.psiFile = psiClass.getContainingFile();
        this.project = psiFile.getProject();
        this.exceptionLabel = exceptionLabel;
        this.classType = filetype;
        this.feedType = feedType;
        this.fieldType = fieldType;
        this.privateFieldOptions = privateFieldOptions;
    }

    public void createDto() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                InputFeedValidationFactory validator = new InputFeedValidationFactory(feedType, exceptionLabel);
                if (validator.isValidFeed(inputFeed, exceptionLabel)) {
                    WriteCommandAction writeAction = DtoGenerationFactory.getDtoGeneratorFor(feedType, classType, fieldType, privateFieldOptions, project, psiFile, (JSONObject) validator.getValidFeed(), psiClass);
                    writeAction.execute();
                }
            }
        });
    }
}
