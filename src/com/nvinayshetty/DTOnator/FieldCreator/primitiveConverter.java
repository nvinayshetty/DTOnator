package com.nvinayshetty.DTOnator.FieldCreator;

/**
 * Created by vinay on 6/6/15.
 */
public interface primitiveConverter {
    public String getSimpleFieldRepresentationFor(AccessModifier AccessModifier, String key);

    public String getGsonFieldRepresentationFor(AccessModifier AccessModifier, String key);
}
