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

package test.com.nvinayshetty.DTOnator.FeedValidator.fieldRepresentors;

import com.nvinayshetty.DTOnator.FieldRepresentors.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by vinay on 16/8/15.
 */
public class FieldRepresentorFactoryShould {
    FieldRepresenterFactory fieldRepresenterFactory;
    @Before
    public void setUp() throws Exception {
        fieldRepresenterFactory=new FieldRepresenterFactory();

    }

    @Test
    public void giveObjectRepresentationForDataTypeRepresentedInString()
    {
        FieldRepresentor BooleanRepresentor= fieldRepresenterFactory.convert("Boolean");
        assertTrue(BooleanRepresentor instanceof BooleanFieldRepresentor);

        FieldRepresentor integerRepresentor= fieldRepresenterFactory.convert("Integer");
        assertTrue(integerRepresentor instanceof IntegerFieldRepresentor);

        FieldRepresentor doubleRepresentor= fieldRepresenterFactory.convert("Double");
        assertTrue(doubleRepresentor instanceof DoubleFieldRepresentor);

        FieldRepresentor jsonObjectRepresentor= fieldRepresenterFactory.convert("JSONObject");
        assertTrue(jsonObjectRepresentor instanceof JsonObjectRepresentor);

        FieldRepresentor jsonArrayRepresentor= fieldRepresenterFactory.convert("JSONArray");
        assertTrue(jsonArrayRepresentor instanceof JsonArrayRepresentor);

        FieldRepresentor stringRepresentor= fieldRepresenterFactory.convert("String");
        assertTrue(stringRepresentor instanceof StringFieldRepresentor);

    }
}
