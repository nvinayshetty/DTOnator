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
import com.nvinayshetty.DTOnator.NameConventionCommands.FieldNameParser;
import com.nvinayshetty.DTOnator.Utility.DtoHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * Created by vinay on 12/7/15.
 */
public class JsonObjectRepresentor extends FieldRepresentor {
    FieldNameParser nameParser;
    private ClassNameOptions classNameOptions;

    @Override
    public String getFieldRepresentationFor(AccessModifier accessModifier, String key) {
        String fieldName = getSimpleNameForObject(accessModifier, key);
        return fieldName;

    }

    @NotNull
    private String getSimpleNameForObject(AccessModifier accessModifier, String key) {
        String Object = key;
        if (nameParser != null)
            Object = nameParser.undo(key);
        String clasNameOption = (classNameOptions != null) ? classNameOptions.getName() : "";
        return accessModifier.getModifier() + DtoHelper.firstetterToUpperCase(Object) + clasNameOption + suffix(key);
    }

    @Override
    protected String getKotlinValFieldRepresentationFor(AccessModifier accessModifier, String key) {
        String Object = key;
        if (nameParser != null)
            key = nameParser.undo(key);
        String clasNameOption = (classNameOptions != null) ? classNameOptions.getName() : "";
        return "val " + Object + ": " + DtoHelper.firstetterToUpperCase(key)+clasNameOption;
    }

    @Override
    protected String getKotlinVarFieldRepresentationFor(AccessModifier accessModifier, String key) {
        String Object = key;
        if (nameParser != null)
            key = nameParser.undo(key);
        String clasNameOption = (classNameOptions != null) ? classNameOptions.getName() : "";
        return "var " + Object + ": " + DtoHelper.firstetterToUpperCase(key)+clasNameOption;

    }



    public void setNameParser(FieldNameParser nameParser) {
        this.nameParser = nameParser;
    }

    public void setClassNameOption(ClassNameOptions classNameOptions) {
        this.classNameOptions = classNameOptions;
    }
}
