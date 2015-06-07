package com.nvinayshetty.DTOnator.DtoCreators;

import com.intellij.psi.PsiClass;
import com.nvinayshetty.DTOnator.ClassCreator.ClassType;

import javax.swing.*;
import java.util.EnumSet;

/**
 * Created by vinay on 31/5/15.
 */
public class DtoCreatorBuilder {
    private String jsonSTR;
    private PsiClass psiClass;
    private JLabel exceptionLabel;
    private ClassType classType;
    private FieldType fieldType;
    private FeedType feedType;
    private EnumSet<PrivateFieldOptions> privateFieldOptions;

    public DtoCreatorBuilder setJsonSTR(String jsonSTR) {
        this.jsonSTR = jsonSTR;
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

    public DtoCreatorBuilder setClassType(ClassType classType) {
        this.classType = classType;
        return this;
    }

    public DtoCreatorBuilder setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public DtoCreatorBuilder setFeedType(FeedType feedType) {
        this.feedType = feedType;
        return this;
    }

    public DtoCreatorBuilder setPrivateFieldOptions(EnumSet<PrivateFieldOptions> privateFieldOptions) {
        this.privateFieldOptions = privateFieldOptions;
        return this;
    }

    public DtoCreator build() {
        return new DtoCreator(jsonSTR, exceptionLabel, feedType, classType, fieldType, privateFieldOptions, psiClass);
    }
}