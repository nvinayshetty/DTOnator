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

package test.com.nvinayshetty.DTOnator.fieldRepresentors;


import com.nvinayshetty.DTOnator.FieldCreator.AccessModifier;
import com.nvinayshetty.DTOnator.FieldRepresentors.BooleanFieldRepresentor;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by vinay on 1/8/15.
 */
public class BooleanFieldRepresentorShould {

    private String fieldName = "valid";

    @Test
    public void CreatePublicFieldWhenAcessModifierIsPublic() {
        String actual = new BooleanFieldRepresentor().getFieldRepresentationFor(AccessModifier.PUBLIC, fieldName);
        String expected = "public boolean " + fieldName + ";";
        assertEquals(expected, actual);
    }

    @Test
    public void CreatePrivateFieldWhenAcessModifierIsPrivate() {
        String actual = new BooleanFieldRepresentor().getFieldRepresentationFor(AccessModifier.PRIVATE, fieldName);
        String expected = "private boolean " + fieldName + ";";
        assertEquals(expected, actual);
    }
}
