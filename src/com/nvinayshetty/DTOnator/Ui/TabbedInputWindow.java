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

package com.nvinayshetty.DTOnator.Ui;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.ui.components.JBTabbedPane;
import com.nvinayshetty.DTOnator.ClassCreator.ClassType;
import com.nvinayshetty.DTOnator.DtoCreationOptions.FieldEncapsulationOptions;
import com.nvinayshetty.DTOnator.DtoCreationOptions.FieldType;

import javax.swing.*;
import java.util.EnumSet;

public class TabbedInputWindow extends JFrame implements JsonInputEditorPane.SettingListener {
    private JBTabbedPane tabbedPane;
    private SettingsTab settingsTab;
    private JsonInputEditorPane.GenerateClickListener generateClickListener;

    public TabbedInputWindow(Project project, PsiFile psiFile, JsonInputEditorPane.GenerateClickListener generateClickListener) {
        this.generateClickListener = generateClickListener;
        tabbedPane = new JBTabbedPane();
        setSize(800, 700);
        JsonInputEditorPane jsonEditorPane = new JsonInputEditorPane(project, psiFile, this);
        tabbedPane.add("DTO Generator", jsonEditorPane);
        settingsTab = new SettingsTab();
        tabbedPane.add("Settings", settingsTab);
        setContentPane(tabbedPane);
    }


    @Override
    public void onGenerateClickWithValidFeed(String input) {
        FieldType fieldType = settingsTab.getFieldType();
        ClassType classType = settingsTab.getClassType();
        EnumSet<FieldEncapsulationOptions> fieldEncapsulationOptions = settingsTab.getFieldEncapsulationOptions();
        generateClickListener.onGenerateButtonClick(fieldType, classType, fieldEncapsulationOptions, input);
    }
}
