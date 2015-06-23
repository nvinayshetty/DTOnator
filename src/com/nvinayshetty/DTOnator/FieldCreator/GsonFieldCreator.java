package com.nvinayshetty.DTOnator.FieldCreator;

/**
 * Created by vinay on 31/5/15.
 */
public class GsonFieldCreator implements FieldCreationStrategy {

    @Override
    public String getFieldFor(FieldRepresentor type, AccessModifier modifier, String key) {
        return type.getGsonFieldRepresentationFor(modifier, key);
    }
}
