package com.nvinayshetty.DTOnator.Factory;

/**
 * Created by vinay on 9/5/15.
 */
public class stringToEnumConversionFactory {

    public static JAVA_TYPES convert(String type) {
        if (type.equals("Boolean")) {
            return JAVA_TYPES.BOOLEAN;
        } else if (type.equals("Integer")) {
            return JAVA_TYPES.INTEGER;
        } else if (type.equals("Double")) {
            return JAVA_TYPES.DOUBLE;
        } else if (type.equals("JSONObject")) {
            return JAVA_TYPES.JSON_OBJECT;
        } else if (type.equals("JSONArray")) {
            return JAVA_TYPES.JSON_ARRAY;
        } else {
            return JAVA_TYPES.STRING;
        }
    }
}

