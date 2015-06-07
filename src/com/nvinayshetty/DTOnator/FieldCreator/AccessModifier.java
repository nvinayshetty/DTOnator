package com.nvinayshetty.DTOnator.FieldCreator;

/**
 * Created by vinay on 7/6/15.
 */
public enum AccessModifier {
    PUBLIC("public "), PRIVATE("private ");

    private String modifier;

    AccessModifier(String s) {
        modifier = s;
    }

    public String getModifier() {
        return modifier;
    }
}
