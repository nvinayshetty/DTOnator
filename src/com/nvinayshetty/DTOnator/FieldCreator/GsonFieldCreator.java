package com.nvinayshetty.DTOnator.FieldCreator;

import com.nvinayshetty.DTOnator.Factory.ObjectType;

/**
 * Created by vinay on 31/5/15.
 */
public class GsonFieldCreator implements FieldCreator {

    @Override
    public String getFieldFor(ObjectType type, String key) {
        return type.getFieldRepresentationFor(key);
    }
}
