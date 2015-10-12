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

package test.com.nvinayshetty.DTOnator.utility.FieldRepresentors;

import com.nvinayshetty.DTOnator.FieldCreator.AccessModifier;
import com.nvinayshetty.DTOnator.FieldRepresentors.IntegerFieldRepresentor;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by vinay on 1/8/15.
 */
public class IntegerFieldRepresentorShould {
    private String fieldName;

    @Test
    public void CreatePublicFieldWhenAcessModifierIsPublic() {
        fieldName = "valid";
        String actual = new IntegerFieldRepresentor().getSimpleFieldRepresentationFor(AccessModifier.PUBLIC, fieldName);
        String expected = "public int " + fieldName + ";" + "\n";
        assertEquals(expected, actual);
    }

    @Test
    public void CreatePrivateFieldWhenAcessModifierIsPrivate() {
        fieldName = "valid";
        String actual = new IntegerFieldRepresentor().getSimpleFieldRepresentationFor(AccessModifier.PRIVATE, fieldName);
        String expected = "private int " + fieldName + ";" + "\n";
        assertEquals(expected, actual);
    }
}
