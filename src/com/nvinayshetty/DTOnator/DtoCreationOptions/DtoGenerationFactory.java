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
import com.intellij.psi.PsiClass;
import com.nvinayshetty.DTOnator.ClassCreator.ClassCreationFactory;
import com.nvinayshetty.DTOnator.ClassCreator.ClassType;
import com.nvinayshetty.DTOnator.FeedParser.JsonDtoGenerator;
import com.nvinayshetty.DTOnator.FeedParser.KotlinJsonDtoGenerator;
import com.nvinayshetty.DTOnator.FieldCreator.AccessModifier;
import com.nvinayshetty.DTOnator.FieldCreator.FieldCreationFactory;
import com.nvinayshetty.DTOnator.FieldCreator.LanguageType;
import com.nvinayshetty.DTOnator.NameConventionCommands.ClassName.ClassNameOptions;
import com.nvinayshetty.DTOnator.NameConventionCommands.NameParserCommand;
import com.nvinayshetty.DTOnator.nameConflictResolvers.NameConflictResolverCommand;
import org.jetbrains.kotlin.psi.KtClass;

import java.util.EnumSet;
import java.util.HashSet;

/**
 * Created by vinay on 17/5/15.
 */
public class DtoGenerationFactory {
    public static WriteCommandAction getDtoGeneratorFor(FeedType type, ClassType classType, FieldType fieldType, EnumSet<FieldEncapsulationOptions> fieldEncapsulationOptions, String validFeed, PsiClass psiClass, HashSet<NameConflictResolverCommand> nameConflictResolverCommands, HashSet<NameParserCommand> fieldNameParser, String cutomFiledPattern, boolean abstractClassWithAnnotation, ClassNameOptions classNameOptions) {
        AccessModifier accessModifier = null;
        if (fieldEncapsulationOptions.contains(FieldEncapsulationOptions.PROVIDE_PRIVATE_FIELD))
            accessModifier = AccessModifier.PRIVATE;
        else
            accessModifier = AccessModifier.PUBLIC;

        switch (type) {
            case JSON:
                DtoCreationOptionsFacade dtoCreationOptionsFacade = new DtoCreationOptionsFacade(FieldCreationFactory.getFieldCreatorFor(fieldType,cutomFiledPattern), ClassCreationFactory.getFileCreatorFor(classType, psiClass), accessModifier, nameConflictResolverCommands, fieldEncapsulationOptions);
                return JsonDtoGenerator.getJsonDtoBuilder()
                        .setClassUnderCaret(psiClass)
                        .setDtoCreationOptionsFacade(dtoCreationOptionsFacade)
                        .setJson(validFeed)
                        .setClassNameOptions(classNameOptions)
                        .setFieldNameParser(fieldNameParser)
                        .setNameConflictResolver(nameConflictResolverCommands)
                        .isAbstractClassWithAnnotation(abstractClassWithAnnotation)
                        .createJsonDtoGenerator();

        }
        return null;
    }

    public static WriteCommandAction getDtoGeneratorForKotlin(FeedType type, ClassType classType, FieldType fieldType, EnumSet<FieldEncapsulationOptions> fieldEncapsulationOptions, String validFeed, KtClass ktClass, HashSet<NameConflictResolverCommand> nameConflictResolverCommands, HashSet<NameParserCommand> fieldNameParser, LanguageType languageType, String customFieldName, boolean abstractClassWithAnnotation, ClassNameOptions classNameOptions) {
        AccessModifier accessModifier = null;
        if (fieldEncapsulationOptions.contains(FieldEncapsulationOptions.PROVIDE_PRIVATE_FIELD))
            accessModifier = AccessModifier.PRIVATE;
        else
            accessModifier = AccessModifier.PUBLIC;

        switch (type) {
            case JSON:
                DtoCreationOptionsFacade dtoCreationOptionsFacade = new DtoCreationOptionsFacade(FieldCreationFactory.getFieldCreatorFor(fieldType,customFieldName), ClassCreationFactory.getFileKotlinCreatorFor(classType, ktClass), accessModifier, nameConflictResolverCommands, fieldEncapsulationOptions);
                return KotlinJsonDtoGenerator.getJsonDtoBuilder()
                        .setClassUnderCaret(ktClass)
                        .setDtoCreationOptionsFacade(dtoCreationOptionsFacade)
                        .setJson(validFeed)
                        .setClassNameOptions(classNameOptions)
                        .SetLanguage(languageType)
                        .setFieldNameParser(fieldNameParser)
                        .isAbstractClassWithAnnotation(abstractClassWithAnnotation)
                        .setNameConflictResolver(nameConflictResolverCommands)
                        .createKotlinDtoGenerator();

        }
        return null;
    }
}
