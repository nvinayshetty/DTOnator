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

/**
 * Created by vinay on 9/5/15.
 */
public class FeedRepresenterFactory {

    public static FieldRepresentor convert(String type) {
        if (type.equals("Boolean")) {
            return new BooleanFieldRepresentor();
        } else if (type.equals("Integer")) {
            return new IntegerFieldRepresentor();
        } else if (type.equals("Double")) {
            return new DoubleFieldRepresentor();
        } else if (type.equals("JSONObject")) {
            return new JsonObjectRepresentor();
        } else if (type.equals("JSONArray")) {
            return new JsonArrayRepresentor();
        } else {
            return new StringFieldRepresentor();
        }
    }

}

