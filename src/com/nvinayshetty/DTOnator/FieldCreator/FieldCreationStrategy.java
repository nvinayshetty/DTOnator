package com.nvinayshetty.DTOnator.FieldCreator;

/**
 * Created by vinay on 31/5/15.
 */
public interface FieldCreationStrategy {
    public String getFieldFor(ObjectType type, AccessModifier accessModifier, String key);
}
