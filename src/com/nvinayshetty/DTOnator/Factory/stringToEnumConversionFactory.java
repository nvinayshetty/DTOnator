package com.nvinayshetty.DTOnator.Factory;

/**
 * Created by vinay on 9/5/15.
 */
public class stringToEnumConversionFactory {

    public static JsonTypeToJavaObjectMapper convert(String type) {
        if (type.equals("Boolean")) {
            return JsonTypeToJavaObjectMapper.BOOLEAN;
        } else if (type.equals("Integer")) {
            return JsonTypeToJavaObjectMapper.INTEGER;
        } else if (type.equals("Double")) {
            return JsonTypeToJavaObjectMapper.DOUBLE;
        } else if (type.equals("JSONObject")) {
            return JsonTypeToJavaObjectMapper.JSON_OBJECT;
        } else if (type.equals("JSONArray")) {
            return JsonTypeToJavaObjectMapper.JSON_ARRAY;
        } else {
            return JsonTypeToJavaObjectMapper.STRING;
        }
    }
}

