package com.nvinayshetty.DTOnator.FieldCreator;

/**
 * Created by vinay on 31/5/15.
 */
public class SimpleFieldCreator implements FieldCreationStrategy {
    @Override
    public String getFieldFor(ObjectType type, AccessModifier accessModifier, String key) {
        return type.getSimpleFieldRepresentationFor(accessModifier, key);
    }
}

