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

package com.nvinayshetty.DTOnator.FeedParser;

import com.intellij.psi.PsiClass;
import com.nvinayshetty.DTOnator.DtoCreationOptions.DtoCreationOptionsFacade;
import com.nvinayshetty.DTOnator.FieldCreator.LanguageType;
import com.nvinayshetty.DTOnator.NameConventionCommands.NameParserCommand;
import com.nvinayshetty.DTOnator.nameConflictResolvers.NameConflictResolverCommand;
import org.jetbrains.kotlin.psi.KtClass;

import java.util.HashSet;

public class JsonDtoBuilder {

    String json;
    PsiClass classUnderCaret;
    KtClass ktClass;
    DtoCreationOptionsFacade dtoCreationOptionsFacade;
    HashSet<NameConflictResolverCommand> nameConflictResolverCommands;
    HashSet<NameParserCommand> feildNameParser;
    LanguageType languageType;

    public JsonDtoBuilder setJson(String json) {
        this.json = json;
        return this;
    }

    public JsonDtoBuilder setClassUnderCaret(PsiClass classUnderCaret) {
        this.classUnderCaret = classUnderCaret;
        return this;
    }


    public JsonDtoBuilder setClassUnderCaret(KtClass classUnderCaret) {
        this.ktClass = classUnderCaret;
        return this;
    }

    public JsonDtoBuilder setDtoCreationOptionsFacade(DtoCreationOptionsFacade dtoCreationOptionsFacade) {
        this.dtoCreationOptionsFacade = dtoCreationOptionsFacade;
        return this;
    }

    public JsonDtoBuilder setFieldNameParser(HashSet<NameParserCommand> feildNameParser) {
        this.feildNameParser = feildNameParser;
        return this;
    }

    public JsonDtoBuilder setNameConflictResolver(HashSet<NameConflictResolverCommand> nameConflictResolverCommands) {
        this.nameConflictResolverCommands = nameConflictResolverCommands;
        return this;
    }


    public JsonDtoBuilder SetLanguage(LanguageType languageType) {
        this.languageType = languageType;
        return this;
    }

    public JsonDtoGenerator createJsonDtoGenerator() {
        return new JsonDtoGenerator(this);
    }

    public KotlinJsonDtoGenerator createKotlinDtoGenerator() {
        return new KotlinJsonDtoGenerator(this);
    }

}