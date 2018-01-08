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

package com.nvinayshetty.DTOnator.persistence;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.nvinayshetty.DTOnator.ClassCreator.ClassType;
import com.nvinayshetty.DTOnator.DtoCreationOptions.FieldEncapsulationOptions;
import com.nvinayshetty.DTOnator.DtoCreationOptions.FieldType;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;


@State(
        name = "DtonatorPreferences",
        storages = {
                @Storage("DtonatorPreferences.xml")}
)
public class DtonatorPreferences implements PersistentStateComponent<DtonatorPreferences> {
    private FieldType fieldType;
    private ClassType classType;
    private KotlinOptions kotlinOptions;
    private List<FieldEncapsulationOptions> encapsulete;
    private List<Naming> naming;
    private String preixingName="";

    public String getPreixingName() {
        return preixingName;
    }

    public void setPreixingName(String preixingName) {
        this.preixingName = preixingName;
    }

    @Nullable
    public static DtonatorPreferences getInstance(Project project) {
        return ServiceManager.getService(project, DtonatorPreferences.class);
    }

    @Nullable
    @Override
    public DtonatorPreferences getState() {
        return this;
    }

    @Override
    public void loadState(DtonatorPreferences dtonatorPreferences) {
        XmlSerializerUtil.copyBean(dtonatorPreferences, this);
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public ClassType getClassType() {
        return classType;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    public KotlinOptions getKotlinOptions() {
        return kotlinOptions;
    }

    public void setKotlinOptions(KotlinOptions kotlinOptions) {
        this.kotlinOptions = kotlinOptions;
    }

    public List<FieldEncapsulationOptions> getEncapsulete() {
        return encapsulete;
    }

    public void setEncapsulete(List<FieldEncapsulationOptions> encapsulete) {
        this.encapsulete = encapsulete;
    }

    public List<Naming> getNaming() {
        return naming;
    }

    public void setNaming(List<Naming> naming) {
        this.naming = naming;
    }
}
