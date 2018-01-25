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

package com.nvinayshetty.DTOnator.FieldRepresentors;


import com.nvinayshetty.DTOnator.FieldCreator.AccessModifier;
import com.nvinayshetty.DTOnator.NameConventionCommands.ClassName.ClassNameOptions;
import com.nvinayshetty.DTOnator.Utility.DtoHelper;

import javax.annotation.Nonnull;

/**
 * Created by vinay on 12/7/15.
 */
public class JsonArrayRepresentor extends FieldRepresentor {
    private String dataType;
    private int depth;
    private ClassNameOptions classNameOptions;

    protected static String getGsonAnnotationFor(String key) {
        return GSON_ANNOTATION_PREFIX + key + ANNOTATION_SUFFIX;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String getFieldRepresentationFor(AccessModifier accessModifier, String key) {
        return accessModifier.getModifier() + "java.util.List<" + getSimpleNameForList(dataType) + ">" + suffix(key);
    }

    @Override
    protected String getKotlinValFieldRepresentationFor(AccessModifier accessModifier, String key) {
        String clasNameOption = (classNameOptions != null) ? classNameOptions.getName() : "";
        return "val " + key +clasNameOption+ " :List<" + getSimpleNameForList(dataType) + ">";
    }

    @Override
    protected String getKotlinVarFieldRepresentationFor(AccessModifier accessModifier, String key) {
        String clasNameOption = (classNameOptions != null) ? classNameOptions.getName() : "";
        return "var " + key+clasNameOption + " :List<" + getSimpleNameForList(dataType) + ">";
    }

    public void setClassNameOption(ClassNameOptions classNameOptions) {
        this.classNameOptions = classNameOptions;
    }

    private String getSimpleNameForList(String simpleName) {

        String name = "";
        for (int i = 1; i < depth; i++) {
            name += "java.util.List<";
        }
        String clasNameOption = (classNameOptions != null) ? classNameOptions.getName() : "";
        name += DtoHelper.firstetterToUpperCase(simpleName)+clasNameOption;
        for (int i = 1; i < depth; i++) {
            name += ">";
        }
        return name;
    }

    public String getGsonFieldRepresentationFor(AccessModifier AccessModifier, String key) {
        return getGsonAnnotationFor(key) + getFieldRepresentationFor(AccessModifier, key);
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
