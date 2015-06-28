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

package com.nvinayshetty.DTOnator.DtoCreators;

import com.nvinayshetty.DTOnator.ClassCreator.ClassCreatorStrategy;
import com.nvinayshetty.DTOnator.FieldCreator.AccessModifier;
import com.nvinayshetty.DTOnator.FieldCreator.FieldCreationStrategy;

import java.util.EnumSet;

/**
 * Created by vinay on 20/6/15.
 */
public class DtoCreationOptionsFacade {
    private FieldCreationStrategy fieldCreationStrategy;
    private ClassCreatorStrategy classAdderStrategy;
    private AccessModifier accessModifier;
    private EnumSet<FieldEncapsulationOptions> privateFieldOptionse;

    public DtoCreationOptionsFacade(FieldCreationStrategy fieldCreationStrategy, ClassCreatorStrategy classAdderStrategy, AccessModifier accessModifier, EnumSet<FieldEncapsulationOptions> privateFieldOptionse) {
        this.fieldCreationStrategy = fieldCreationStrategy;
        this.classAdderStrategy = classAdderStrategy;
        this.accessModifier = accessModifier;
        this.privateFieldOptionse = privateFieldOptionse;
    }

    public FieldCreationStrategy getFieldCreationStrategy() {
        return fieldCreationStrategy;
    }

    public void setFieldCreationStrategy(FieldCreationStrategy fieldCreationStrategy) {
        this.fieldCreationStrategy = fieldCreationStrategy;
    }

    public ClassCreatorStrategy getClassAdderStrategy() {
        return classAdderStrategy;
    }

    public void setClassAdderStrategy(ClassCreatorStrategy classAdderStrategy) {
        this.classAdderStrategy = classAdderStrategy;
    }

    public AccessModifier getAccessModifier() {
        return accessModifier;
    }

    public void setAccessModifier(AccessModifier accessModifier) {
        this.accessModifier = accessModifier;
    }

    public EnumSet<FieldEncapsulationOptions> getPrivateFieldOptionse() {
        return privateFieldOptionse;
    }

    public void setPrivateFieldOptionse(EnumSet<FieldEncapsulationOptions> privateFieldOptionse) {
        this.privateFieldOptionse = privateFieldOptionse;
    }
}
