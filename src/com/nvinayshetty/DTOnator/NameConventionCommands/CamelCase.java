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

import nvinayshetty.DTOnator.Utility.DtoHelper;
import org.apache.commons.lang.WordUtils;

/**
 * Created by vinay on 18/7/15.
 */
public class CamelCase implements NameParserCommand {
    @Override
    public String parseFieldName(String name) {
        String capitalized = name;
        if (name.contains(" ")) {
            capitalized = WordUtils.capitalizeFully(name);

        }
        if (name.contains("_"))
            capitalized = WordUtils.capitalizeFully(capitalized, new char[]{'_'});
        else
            capitalized = WordUtils.capitalizeFully(capitalized);
        String javaConvention = DtoHelper.firstetterToLowerCase(capitalized);
        String removedSpace = javaConvention.replaceAll(" ", "");
        return removedSpace.replaceAll("_", "");
    }

    @Override
    public String undoParsing(String name) {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        CamelCase that = (CamelCase) o;
        return o.getClass()
                .getName()
                .equals(that.getClass()
                            .getName());

    }

    @Override
    public int hashCode() {
        return this.getClass()
                   .getName()
                   .hashCode();
    }
}
