package com.nvinayshetty.DTOnator.DtoCreators;

import com.nvinayshetty.DTOnator.ClassCreator.ClassCreatorStrategy;
import com.nvinayshetty.DTOnator.FieldCreator.AccessModifier;
import com.nvinayshetty.DTOnator.FieldCreator.FieldCreationStrategy;

import java.util.EnumSet;

/**
 * Created by vinay on 20/6/15.
 */
public class DtoCreater {
    private FieldCreationStrategy fieldCreationStrategy;
    private ClassCreatorStrategy classAdderStrategy;
    private AccessModifier accessModifier;
    private EnumSet<FieldEncapsulatopnOptions> privateFieldOptionse;

    public DtoCreater(FieldCreationStrategy fieldCreationStrategy, ClassCreatorStrategy classAdderStrategy, AccessModifier accessModifier, EnumSet<FieldEncapsulatopnOptions> privateFieldOptionse) {
        this.fieldCreationStrategy = fieldCreationStrategy;
        this.classAdderStrategy = classAdderStrategy;
        this.accessModifier = accessModifier;
        this.privateFieldOptionse = privateFieldOptionse;
    }

    public FieldCreationStrategy getFieldCreationStrategy() {
        return fieldCreationStrategy;
    }

    public void setFieldCreationStrategy(FieldCreationStrategy fieldCreationStrategy) {
        this.fieldCreationStrategy = fieldCreationStrategy;
    }

    public ClassCreatorStrategy getClassAdderStrategy() {
        return classAdderStrategy;
    }

    public void setClassAdderStrategy(ClassCreatorStrategy classAdderStrategy) {
        this.classAdderStrategy = classAdderStrategy;
    }

    public AccessModifier getAccessModifier() {
        return accessModifier;
    }

    public void setAccessModifier(AccessModifier accessModifier) {
        this.accessModifier = accessModifier;
    }

    public EnumSet<FieldEncapsulatopnOptions> getPrivateFieldOptionse() {
        return privateFieldOptionse;
    }

    public void setPrivateFieldOptionse(EnumSet<FieldEncapsulatopnOptions> privateFieldOptionse) {
        this.privateFieldOptionse = privateFieldOptionse;
    }
}
