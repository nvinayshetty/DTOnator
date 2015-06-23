package com.nvinayshetty.DTOnator.FieldCreator;

/**
 * Created by vinay on 31/5/15.
 */
public class SimpleFieldCreator implements FieldCreationStrategy {
    @Override
    public String getFieldFor(FieldRepresentor fieldRepresentor, AccessModifier accessModifier, String key) {
        return fieldRepresentor.getSimpleFieldRepresentationFor(accessModifier, key);
    }
}

