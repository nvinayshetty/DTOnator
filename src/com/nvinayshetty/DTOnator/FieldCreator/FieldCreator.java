package com.nvinayshetty.DTOnator.FieldCreator;

import com.nvinayshetty.DTOnator.Factory.ObjectType;

/**
 * Created by vinay on 31/5/15.
 */
public interface FieldCreator {
    public String getFieldFor(ObjectType type, String key);
}
