package com.nvinayshetty.DTOnator.FieldCreator;

import com.nvinayshetty.DTOnator.Utility.DtoHelper;

/**
 * Created by vinay on 9/5/15.
 */
public enum ObjectType implements primitiveConverter {
    BOOLEAN {
        @Override
        public String getSimpleFieldRepresentationFor(AccessModifier accessModifier, String key) {
            return new StringBuilder().append(accessModifier.getModifier()).append("boolean").append(suffix(key)).toString();
        }

        @Override
        public String getGsonFieldRepresentationFor(AccessModifier accessModifier, String key) {
            return getGsonAnnotationFor(key).append(getSimpleFieldRepresentationFor(accessModifier, key)).toString();
        }


    }, INTEGER {
        @Override
        public String getSimpleFieldRepresentationFor(AccessModifier accessModifier, String key) {
            return new StringBuilder().append(accessModifier.getModifier()).append("int").append(suffix(key)).toString();
        }

        @Override
        public String getGsonFieldRepresentationFor(AccessModifier accessModifier, String key) {
            return getGsonAnnotationFor(key).append(getSimpleFieldRepresentationFor(accessModifier, key)).toString();
        }
    }, DOUBLE {
        @Override
        public String getSimpleFieldRepresentationFor(AccessModifier accessModifier, String key) {
            return new StringBuilder().append(accessModifier.getModifier()).append("double").append(suffix(key)).toString();
        }

        @Override
        public String getGsonFieldRepresentationFor(AccessModifier accessModifier, String key) {
            return getGsonAnnotationFor(key).append(getSimpleFieldRepresentationFor(accessModifier, key)).toString();
        }
    }, JSON_OBJECT {
        @Override
        public String getSimpleFieldRepresentationFor(AccessModifier accessModifier, String key) {
            return new StringBuilder().append(accessModifier.getModifier()).append(DtoHelper.getSubClassName(key)).append(suffix(key)).toString();
        }

        @Override
        public String getGsonFieldRepresentationFor(AccessModifier accessModifier, String key) {
            return getGsonAnnotationFor(key).append(getSimpleFieldRepresentationFor(accessModifier, key)).toString();
        }
    }, JSON_ARRAY {
        @Override
        public String getSimpleFieldRepresentationFor(AccessModifier accessModifier, String key) {
            return new StringBuilder().append(accessModifier.getModifier()).append("java.util.List<").append(DtoHelper.getSubClassName(key)).append(">").append(suffix(key)).toString();
        }

        @Override
        public String getGsonFieldRepresentationFor(AccessModifier accessModifier, String key) {
            return getGsonAnnotationFor(key).append(getSimpleFieldRepresentationFor(accessModifier, key)).toString();
        }
    }, STRING {
        @Override
        public String getSimpleFieldRepresentationFor(AccessModifier accessModifier, String key) {
            return new StringBuilder().append(accessModifier.getModifier()).append("String").append(suffix(key)).toString();
        }

        @Override
        public String getGsonFieldRepresentationFor(AccessModifier accessModifier, String key) {
            return getGsonAnnotationFor(key).append(getSimpleFieldRepresentationFor(accessModifier, key)).toString();
        }
    };

    private static final String ANNOTATION_PREFIX = "@com.google.gson.annotations.SerializedName(\"";
    private static final String ANNOTATION_SUFFIX = "\")\n";
    private static final String SPACE = " ";
    private static final String CLASS_FIELD_SUFFIX = ";\n";

    private static StringBuilder getGsonAnnotationFor(String key) {
        return new StringBuilder().append(ANNOTATION_PREFIX).append(key.trim()).append(ANNOTATION_SUFFIX);
    }

    private static String suffix(String key) {
        return new StringBuilder().append(SPACE).append(key).append(CLASS_FIELD_SUFFIX).toString();
    }
}

