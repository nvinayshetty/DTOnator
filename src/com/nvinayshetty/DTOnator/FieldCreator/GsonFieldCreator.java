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


import com.nvinayshetty.DTOnator.FieldRepresentors.FieldRepresentor;
import com.nvinayshetty.DTOnator.NameConventionCommands.FieldNameParser;
import com.nvinayshetty.DTOnator.nameConflictResolvers.NameConflictResolver;

/**
 * Created by vinay on 31/5/15.
 */
public class GsonFieldCreator implements FieldCreationStrategy {

    @Override
    public String getFieldFor(LanguageType languageType, FieldRepresentor type, AccessModifier modifier, String key, FieldNameParser parser, NameConflictResolver nameConflictResolver) {
        return type.gsonFieldRepresentationTemplate(languageType, modifier, key, parser, nameConflictResolver);
    }

    @Override
    public String getImportDirective() {
        return "com.google.gson.annotations.SerializedName";
    }

}
