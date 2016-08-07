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

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by vinay on 12/7/15.
 */
public class FieldNameParser {
    HashSet<NameParserCommand> fieldNameParser;

    public FieldNameParser(HashSet<NameParserCommand> fieldNameParser) {
        this.fieldNameParser = fieldNameParser;
    }

    public String parseField(String field) {
        String parsedFieldName = field;//= field;
        Iterator<NameParserCommand> fieldParserIterator = fieldNameParser.iterator();
        while (fieldParserIterator.hasNext()) {
            NameParserCommand parser = fieldParserIterator.next();
            parsedFieldName = parser.parseFieldName(parsedFieldName);
        }
        return parsedFieldName;
    }

    public String undo(String fieldName) {
        String unparsed = fieldName;//= field;
        Iterator<NameParserCommand> fieldParserIterator = fieldNameParser.iterator();
        while (fieldParserIterator.hasNext()) {
            NameParserCommand parser = fieldParserIterator.next();
            unparsed = parser.undoParsing(unparsed);
        }
        return unparsed;
    }
}
