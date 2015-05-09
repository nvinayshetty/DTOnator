package com.nvinayshetty.DTOnator.Utility;

/**
 * Created by vinay on 9/5/15.
 */
public class DtoHelper {
    public static String getSubClassName(String key) {
        return key.substring(0, 1).toUpperCase() + key.substring(1);
    }

    public interface primitiveConverter {
        public String getFieldRepresentationFor(String key);
    }


}
