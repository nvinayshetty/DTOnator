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

package com.nvinayshetty.DTOnator.DtoCreationOptions;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.nvinayshetty.DTOnator.ClassCreator.ClassCreationFactory;
import com.nvinayshetty.DTOnator.ClassCreator.ClassType;
import com.nvinayshetty.DTOnator.FeedParser.JsonDtoGenerator;
import com.nvinayshetty.DTOnator.FieldCreator.AccessModifier;
import com.nvinayshetty.DTOnator.FieldCreator.FieldCreationFactory;
import com.nvinayshetty.DTOnator.NameConventionCommands.NameParserCommand;
import com.nvinayshetty.DTOnator.nameConflictResolvers.NameConflictResolverCommand;
import org.json.JSONObject;

import java.util.EnumSet;
import java.util.HashSet;

/**
 * Created by vinay on 17/5/15.
 */
public class DtoGenerationFactory {
    public static WriteCommandAction getDtoGeneratorFor(FeedType type, ClassType classType, FieldType fieldType, EnumSet<FieldEncapsulationOptions> fieldEncapsulationOptions, Project project, PsiFile psiFile, JSONObject validFeed, PsiClass psiClass, HashSet<NameConflictResolverCommand> nameConflictResolverCommands, HashSet<NameParserCommand> fieldNameParser) {
        AccessModifier accessModifier = null;
        if (fieldEncapsulationOptions.contains(FieldEncapsulationOptions.PROVIDE_PRIVATE_FIELD))
            accessModifier = AccessModifier.PRIVATE;
        else
            accessModifier = AccessModifier.PUBLIC;

        switch (type) {
            case JSON:
                DtoCreationOptionsFacade dtoCreationOptionsFacade = new DtoCreationOptionsFacade(FieldCreationFactory.getFieldCreatorFor(fieldType), ClassCreationFactory.getFileCreatorFor(classType, psiClass), accessModifier, nameConflictResolverCommands, fieldEncapsulationOptions);
                return JsonDtoGenerator.getJsonDtoBuilder()
                                       .setClassUnderCaret(psiClass)
                                       .setDtoCreationOptionsFacade(dtoCreationOptionsFacade)
                                       .setJson(validFeed)
                                       .setFeildNameParser(fieldNameParser)
                                       .setNameConflictResolverr(nameConflictResolverCommands)
                                       .createJsonDtoGenerator();
        }
        return null;
    }
}
