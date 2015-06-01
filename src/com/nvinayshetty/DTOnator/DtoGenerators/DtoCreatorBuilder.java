package com.nvinayshetty.DTOnator.DtoGenerators;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;

import javax.swing.*;

/**
 * Created by vinay on 31/5/15.
 */
public class DtoCreatorBuilder {
    private String jsonSTR;
    private Project project;
    private PsiFile psiFile;
    private PsiClass psiClass;
    private JLabel exceptionLabel;
    private DtoFileCreationPreference fileCreationOptions;

    public DtoCreatorBuilder setJsonSTR(String jsonSTR) {
        this.jsonSTR = jsonSTR;
        return this;
    }

    public DtoCreatorBuilder setProject(Project project) {
        this.project = project;
        return this;
    }

    public DtoCreatorBuilder setPsiFile(PsiFile psiFile) {
        this.psiFile = psiFile;
        return this;
    }

    public DtoCreatorBuilder setPsiClass(PsiClass psiClass) {
        this.psiClass = psiClass;
        return this;
    }

    public DtoCreatorBuilder setExceptionLabel(JLabel exceptionLabel) {
        this.exceptionLabel = exceptionLabel;
        return this;
    }

    public DtoCreatorBuilder setFileCreationPreference(DtoFileCreationPreference fileCreationOptions) {
        this.fileCreationOptions = fileCreationOptions;
        return this;
    }

    public DtoCreator build() {
        return new DtoCreator(jsonSTR, exceptionLabel, fileCreationOptions, project, psiFile, psiClass);
    }
}