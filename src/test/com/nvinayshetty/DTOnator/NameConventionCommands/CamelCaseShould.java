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

package com.nvinayshetty.DTOnator.NameConventionCommands;

import nvinayshetty.DTOnator.NameConventionCommands.CamelCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by vinay on 16/8/15.
 */
public class CamelCaseShould {

    @Test
    public void convertFieldSeparatedNamesToCamelCase() {
        assertEquals("valid", new CamelCase().parseFieldName("Valid"));
        assertEquals("valid", new CamelCase().parseFieldName("VALID"));
        assertEquals("validJavaIdentifier", new CamelCase().parseFieldName("VALID_JAVA_IDENTIFIER"));
        assertEquals("validJavaIdentifier", new CamelCase().parseFieldName("valid_java_identifier"));
        assertEquals("invalidIdentifier", new CamelCase().parseFieldName("INVALID IDENTIFIER"));
        assertEquals("invalidIdentifier", new CamelCase().parseFieldName("invalid identifier"));
        assertEquals("valid", new CamelCase().parseFieldName("valid"));
    }

    @Test
    public void shouldNotUndoCamelCasing() {
        assertEquals("valid", new CamelCase().undoParsing("valid"));
        assertEquals("validJavaIdentifier", new CamelCase().undoParsing("validJavaIdentifier"));
    }

}
