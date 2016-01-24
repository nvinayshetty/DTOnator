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

package nvinayshetty.DTOnator.NameConventionCommands;

import org.apache.commons.lang.WordUtils;

/**
 * Created by vinay on 23/1/16.
 */
public class test {
    public static void main(String[] args) {
        // String string = WordUtils.capitalize("INVALID IDENTIFIER");
        String string = WordUtils.capitalizeFully("INVALID IDENTIFIER");
        string = WordUtils.capitalizeFully(string, new char[]{'_'});
        System.out.println(string);
    }
}
