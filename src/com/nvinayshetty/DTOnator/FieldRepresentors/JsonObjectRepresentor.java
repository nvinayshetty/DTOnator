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
import com.nvinayshetty.DTOnator.NameConventionCommands.FieldNameParser;
import com.nvinayshetty.DTOnator.Utility.DtoHelper;
import org.jetbrains.annotations.NotNull;

/**
 * Created by vinay on 12/7/15.
 */
public class JsonObjectRepresentor extends FieldRepresentor {
    FieldNameParser nameParser;

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
        return accessModifier.getModifier() + DtoHelper.firstetterToUpperCase(Object) + suffix(key);
    }

    @Override
    protected String getKotlinValFieldRepresentationFor(AccessModifier accessModifier, String key) {
        return "val " + key + ": " + DtoHelper.firstetterToUpperCase(key);
    }

    @Override
    protected String getKotlinVarFieldRepresentationFor(AccessModifier accessModifier, String key) {
        return "var " + key + ": " +DtoHelper.firstetterToUpperCase(key);

    }


    public void setNameParser(FieldNameParser nameParser) {
        this.nameParser = nameParser;
    }
}
