package com.nvinayshetty.DTOnator.FieldCreator;

import com.nvinayshetty.DTOnator.DtoCreators.FieldType;

/**
 * Created by vinay on 7/6/15.
 */
public class FieldCreationFactory {
    public static FieldCreationStrategy getFieldCreatorFor(FieldType fieldType) {
        switch (fieldType) {
            case GSON:
                return new GsonFieldCreator();
            case POJO:
                return new SimpleFieldCreator();
        }
        return null;
    }
}
