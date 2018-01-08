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
import com.nvinayshetty.DTOnator.FieldCreator.LanguageType;
import com.nvinayshetty.DTOnator.NameConventionCommands.CamelCase;
import com.nvinayshetty.DTOnator.NameConventionCommands.NameParserCommand;
import com.nvinayshetty.DTOnator.NameConventionCommands.NamePrefixer;
import com.nvinayshetty.DTOnator.persistence.DtonatorPreferences;
import com.nvinayshetty.DTOnator.persistence.Naming;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;

public class TabbedInputWindow extends JFrame implements JsonInputEditorPane.SettingListener {
    private final JsonInputEditorPane jsonEditorPane;
    private JBTabbedPane tabbedPane;
    private SettingsTab settingsTab;
    private Project project;
    private JsonInputEditorPane.GenerateClickListener generateClickListener;

    public TabbedInputWindow(Project project, PsiFile psiFile, JsonInputEditorPane.GenerateClickListener generateClickListener, LanguageType languageType) {
        this.generateClickListener = generateClickListener;
        this.project = project;
        tabbedPane = new JBTabbedPane();
        setSize(800, 700);
        jsonEditorPane = new JsonInputEditorPane(project, psiFile, this);
        tabbedPane.add("DTO Generator", jsonEditorPane);
        settingsTab = new SettingsTab(project);
        tabbedPane.add("Settings", settingsTab);
        setContentPane(tabbedPane);
        if (languageType == LanguageType.KOTLIN) {
            settingsTab.enableKotlinOptions();
            settingsTab.hideEncapsulationOptions();
        } else {
            settingsTab.hideKotlinOptions();
            settingsTab.enableEncapsulationOptions();
        }
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 0) {
                    settingsTab.hideSettingsAlert();
                }
            }
        });
    }


    @Override
    public void onGenerateClickWithValidFeed(String input) {
        FieldType fieldType = settingsTab.getFieldType();
        ClassType classType = settingsTab.getClassType();
        LanguageType language = settingsTab.getLanguage();
        HashSet<NameParserCommand> fieldNameParserCommands = settingsTab.getFieldNameParserCommands();
        DtonatorPreferences instance = DtonatorPreferences.getInstance(project);
        EnumSet<FieldEncapsulationOptions> fieldEncapsulationOptions = settingsTab.getFieldEncapsulationOptions();

        if (classType == null) {
            settingsTab.showSettingAlert("Please select How to organize classes");
            tabbedPane.setSelectedIndex(1);
            return;
        }
        if (fieldType == null) {
            settingsTab.showSettingAlert("Please select Which annotation type to use in settings tab");
            tabbedPane.setSelectedIndex(1);
            return;
        }
        instance.setFieldType(fieldType);
        instance.setClassType(classType);
        List<FieldEncapsulationOptions> options = new ArrayList<>();
        for (FieldEncapsulationOptions fieldEncapsulationOption : fieldEncapsulationOptions) {
            options.add(fieldEncapsulationOption);
        }
        instance.setEncapsulete(options);
        for (NameParserCommand nameParserCommand : fieldNameParserCommands) {
            List<Naming> namings = new ArrayList<>();
            if (nameParserCommand instanceof CamelCase) {
                namings.add(Naming.CAMEL_CASE);
            }
            if (nameParserCommand instanceof NamePrefixer) {
                namings.add(Naming.PREFEIX);
            }
            instance.setNaming(namings);

        }
        generateClickListener.onGenerateButtonClick(fieldType, classType, fieldEncapsulationOptions, input, fieldNameParserCommands, language);
        dispose();
    }

}
