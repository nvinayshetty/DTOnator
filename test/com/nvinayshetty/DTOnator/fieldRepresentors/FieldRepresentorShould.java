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

import com.nvinayshetty.DTOnator.FeedValidator.KeywordClasifier;
import com.nvinayshetty.DTOnator.FieldCreator.AccessModifier;
import com.nvinayshetty.DTOnator.FieldRepresentors.BooleanFieldRepresentor;
import com.nvinayshetty.DTOnator.FieldRepresentors.FieldRepresentor;
import com.nvinayshetty.DTOnator.NameConventionCommands.CamelCase;
import com.nvinayshetty.DTOnator.NameConventionCommands.FieldNameParser;
import com.nvinayshetty.DTOnator.NameConventionCommands.NameParserCommand;
import com.nvinayshetty.DTOnator.nameConflictResolvers.NameConflictResolver;
import com.nvinayshetty.DTOnator.nameConflictResolvers.NameConflictResolverCommand;
import com.nvinayshetty.DTOnator.nameConflictResolvers.PrefixingConflictResolverCommand;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

/**
 * Created by vinay on 1/8/15.
 */
public class FieldRepresentorShould {

    @Test
    public void shouldCreateSimpleFieldRepresentationByParsingTheGivenFieldName() {
        String fieldName = "valid";
        HashSet<NameParserCommand> nameParserCommands = new HashSet<>();
        nameParserCommands.add(new CamelCase());
        FieldNameParser fieldNameParser = new FieldNameParser(nameParserCommands);

        FieldRepresentor fieldRepresentor = new BooleanFieldRepresentor();

        HashSet<NameConflictResolverCommand> nameConflictResolverCommands = new HashSet<>();
        final PrefixingConflictResolverCommand prefixingConflictResolverCommand = new PrefixingConflictResolverCommand("m");
        nameConflictResolverCommands.add(prefixingConflictResolverCommand);
        NameConflictResolver nameConflictResolver = new NameConflictResolver(nameConflictResolverCommands);
        KeywordClasifier keywordClasifier = new KeywordClasifier();
        String expected = fieldRepresentor.simpleFieldCreationTemplate(AccessModifier.PUBLIC, fieldName, fieldNameParser, nameConflictResolver, keywordClasifier);
        String actual = "public boolean valid;" + "\n";
        assertEquals(expected, actual);
    }
}
