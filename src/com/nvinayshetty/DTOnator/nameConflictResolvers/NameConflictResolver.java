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

package com.nvinayshetty.DTOnator.nameConflictResolvers;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by vinay on 25/7/15.
 */
public class NameConflictResolver {
    HashSet<NameConflictResolverCommand> nameConflictResolverCommandses;

    public NameConflictResolver(HashSet<NameConflictResolverCommand> nameConflictResolverCommandses) {
        this.nameConflictResolverCommandses = nameConflictResolverCommandses;
    }

    public String resolveNamingConflict(String fieldName) {
        String parsedKey = fieldName;
        Iterator<NameConflictResolverCommand> resolverIterator = nameConflictResolverCommandses.iterator();
        if (resolverIterator.hasNext()) {
            NameConflictResolverCommand nameConflictResolversCommands = resolverIterator.next();
            parsedKey = nameConflictResolversCommands.resolve(parsedKey);
        }
        return parsedKey;
    }
}
