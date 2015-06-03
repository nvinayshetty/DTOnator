package com.nvinayshetty.DTOnator.Factory;

/**
 * Created by vinay on 9/5/15.
 */
public class TypeToObjectTypeConverter {

    public static ObjectType convert(String type) {
        if (type.equals("Boolean")) {
            return ObjectType.BOOLEAN;
        } else if (type.equals("Integer")) {
            return ObjectType.INTEGER;
        } else if (type.equals("Double")) {
            return ObjectType.DOUBLE;
        } else if (type.equals("JSONObject")) {
            return ObjectType.JSON_OBJECT;
        } else if (type.equals("JSONArray")) {
            return ObjectType.JSON_ARRAY;
        } else {
            return ObjectType.STRING;
        }
    }
}

