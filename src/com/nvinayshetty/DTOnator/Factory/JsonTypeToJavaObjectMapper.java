package com.nvinayshetty.DTOnator.Factory;

import com.nvinayshetty.DTOnator.Utility.DtoHelper;

/**
 * Created by vinay on 9/5/15.
 */
public enum JsonTypeToJavaObjectMapper implements DtoHelper.primitiveConverter {
    BOOLEAN {
        @Override
        public String getFieldRepresentationFor(String key) {
            return new StringBuilder().append(ANNOTATION_PREFIX).append(key).append(ANNOTATION_SUFFIX).append(CLASS_ACCESS_MODIFIER).append("boolean").append(SPACE).append(key).append(CLASS_FIELD_SUFFIX).toString();
        }
    }, INTEGER {
        @Override
        public String getFieldRepresentationFor(String key) {
            return new StringBuilder().append(ANNOTATION_PREFIX).append(key).append(ANNOTATION_SUFFIX).append(CLASS_ACCESS_MODIFIER).append("int").append(SPACE).append(key).append(CLASS_FIELD_SUFFIX).toString();
        }
    }, DOUBLE {
        @Override
        public String getFieldRepresentationFor(String key) {
            return new StringBuilder().append(ANNOTATION_PREFIX).append(key).append(ANNOTATION_SUFFIX).append(CLASS_ACCESS_MODIFIER).append("double").append(SPACE).append(key).append(CLASS_FIELD_SUFFIX).toString();
        }
    }, JSON_OBJECT {
        @Override
        public String getFieldRepresentationFor(String key) {
            String className = DtoHelper.getSubClassName(key);
            return new StringBuilder().append(ANNOTATION_PREFIX).append(key).append(ANNOTATION_SUFFIX).append(CLASS_ACCESS_MODIFIER).append(className).append(SPACE).append(key).append(CLASS_FIELD_SUFFIX).toString();
        }
    }, JSON_ARRAY {
        @Override
        public String getFieldRepresentationFor(String key) {
            String className = DtoHelper.getSubClassName(key);
            return new StringBuilder().append(ANNOTATION_PREFIX).append(key).append(ANNOTATION_SUFFIX).append(CLASS_ACCESS_MODIFIER).append("java.util.List<").append(className).append(">").append(SPACE).append(key).append(CLASS_FIELD_SUFFIX).toString();
        }
    }, STRING {
        @Override
        public String getFieldRepresentationFor(String key) {
            return new StringBuilder().append(ANNOTATION_PREFIX).append(key).append(ANNOTATION_SUFFIX).append(CLASS_ACCESS_MODIFIER).append("String").append(SPACE).append(key).append(CLASS_FIELD_SUFFIX).toString();
        }
    };

    private static final String ANNOTATION_PREFIX = "@SerializedName(\"";
    private static final String ANNOTATION_SUFFIX = "\")\n";
    private static final String SPACE = " ";
    private static final String CLASS_ACCESS_MODIFIER = "public" + SPACE;
    private static final String CLASS_FIELD_SUFFIX = ";\n";

}

