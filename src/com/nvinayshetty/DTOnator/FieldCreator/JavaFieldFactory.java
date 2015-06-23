package com.nvinayshetty.DTOnator.FieldCreator;

/**
 * Created by vinay on 9/5/15.
 */
public class JavaFieldFactory {

    public static FieldRepresentor convert(String type) {
        if (type.equals("Boolean")) {
            return FieldRepresentor.BOOLEAN;
        } else if (type.equals("Integer")) {
            return FieldRepresentor.INTEGER;
        } else if (type.equals("Double")) {
            return FieldRepresentor.DOUBLE;
        } else if (type.equals("JSONObject")) {
            return FieldRepresentor.JSON_OBJECT;
        } else if (type.equals("JSONArray")) {
            return FieldRepresentor.JSON_ARRAY;
        } else {
            return FieldRepresentor.STRING;
        }
    }
}

