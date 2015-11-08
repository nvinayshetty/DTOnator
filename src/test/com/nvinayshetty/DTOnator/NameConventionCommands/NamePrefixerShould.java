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

package test.com.nvinayshetty.DTOnator.FeedValidator.NameConventionCommands;

import com.nvinayshetty.DTOnator.NameConventionCommands.NamePrefixer;
import com.nvinayshetty.DTOnator.Utility.DtoHelper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by vinay on 16/8/15.
 */
public class NamePrefixerShould {
    String prefixString="m";

    @Test
    public void prefixFieldWithTheString()
    {
        String fieldName="valid";
        String actual= NamePrefixer.prefixWith(prefixString).parseFieldName(fieldName);
        String expected=prefixString+ DtoHelper.firstetterToUpperCase(fieldName);
        assertEquals(actual,expected);
    }

    @Test
    public void undoFieldPrefixing()
    {
        String parsedFieldName="mValid";
        String actual=NamePrefixer.prefixWith(prefixString).undoParsing(parsedFieldName);
        String expected="Valid";
        assertEquals(expected,actual);
    }

    @Test
    public void returnSameNameWhenFieldBecomesAnInvalidIdentifierAfterUndoingParsing(){
        String parsedFieldName="m1";
        String actual=NamePrefixer.prefixWith(prefixString).undoParsing(parsedFieldName);
        String expected="m1";
        assertEquals(actual,expected);
    }
}
