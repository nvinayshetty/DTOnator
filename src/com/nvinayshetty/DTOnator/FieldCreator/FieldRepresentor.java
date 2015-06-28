/*
 * Copyright (C) 2015 Vinaya Prasad N
 *
 *         This program is free software: you can redistribute it and/or modify
 *         it under the terms of the GNU General Public License as published by
 *         the Free Software Foundation, either version 3 of the License, or
 *         (at your option) any later version.
 *
 *         This program is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *         GNU General Public License for more details.
 *
 *         You should have received a copy of the GNU General Public License
 *         along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.nvinayshetty.DTOnator.FieldCreator;

import com.nvinayshetty.DTOnator.Utility.DtoHelper;

import static com.nvinayshetty.DTOnator.FeedParser.JsonDtoGenerator.SEPARATOR;

/**
 * Created by vinay on 9/5/15.
 */
public enum FieldRepresentor implements primitiveConverter {
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
            String dataType = getDataType(key);
            return new StringBuilder().append(accessModifier.getModifier()).append("java.util.List<").append(dataType).append(">").append(suffix(getFieldName(key))).toString();
        }

        @Override
        public String getGsonFieldRepresentationFor(AccessModifier accessModifier, String key) {
            return getGsonAnnotationFor(getFieldName(key)).append(getSimpleFieldRepresentationFor(accessModifier, key)).toString();
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

    private static String getDataType(String key) {
        String dataType;
        if (key.contains(SEPARATOR)) {
            String[] values = key.split(SEPARATOR);
            dataType = values[0];
        } else {
            dataType = DtoHelper.getSubClassName(key);
        }
        return dataType;
    }

    private static StringBuilder getGsonAnnotationFor(String key) {
        return new StringBuilder().append(ANNOTATION_PREFIX).append(key.trim()).append(ANNOTATION_SUFFIX);
    }

    private static String suffix(String key) {
        return new StringBuilder().append(SPACE).append(key).append(CLASS_FIELD_SUFFIX).toString();
    }

    private static String getFieldName(String key) {
        String fieldName;
        if (key.contains(SEPARATOR)) {
            String[] values = key.split(SEPARATOR);
            fieldName = values[1];
        } else {
            fieldName = key;
        }
        return fieldName;
    }
}

