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
import com.nvinayshetty.DTOnator.ClassCreator.ClassType;
import com.nvinayshetty.DTOnator.DtoCreationOptions.FieldEncapsulationOptions;
import com.nvinayshetty.DTOnator.DtoCreationOptions.FieldType;
import com.nvinayshetty.DTOnator.FieldCreator.LanguageType;
import com.nvinayshetty.DTOnator.NameConventionCommands.CamelCase;
import com.nvinayshetty.DTOnator.NameConventionCommands.ClassName.ClassNameOptions;
import com.nvinayshetty.DTOnator.NameConventionCommands.NameParserCommand;
import com.nvinayshetty.DTOnator.NameConventionCommands.NamePrefixer;
import com.nvinayshetty.DTOnator.persistence.DtonatorPreferences;
import com.nvinayshetty.DTOnator.persistence.Naming;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class SettingsTab extends JPanel {
    private JPanel filesPane;
    private JRadioButton singleFile;
    private JRadioButton separeteFileForEachRadioButton;
    private JRadioButton gsonRadioButton;
    private JRadioButton plainClassRadioButton;
    private JRadioButton jacksonRadioButton;
    private JCheckBox exposeCheckBox;
    private JRadioButton makeFieldsPrivateRadioButton;
    private JCheckBox provideGetterCheckBox;
    private JCheckBox provideSetterCheckBox;
    private JCheckBox useCamelCaseCheckBox;
    private JCheckBox prefixFieldWithCheckBox;
    private JTextField nameConventionPrefix;
    private JRadioButton customRadioButton;
    private JRadioButton valRadioButton;
    private JRadioButton varRadioButton;
    private JEditorPane ifYouLikeThisEditorPane;
    private JLabel settingsExceptionLabel;
    private JPanel kotlinPanel;
    private JPanel encapsulatePane;
    private JTextField customTextField;
    private JRadioButton autoValueRadioButton;
    private JPanel non;
    private JCheckBox entityCheckBox;
    private JCheckBox beanCheckBox;
    private ButtonGroup kotlinButtonGroup = new ButtonGroup();
    private ButtonGroup fileTypeButtonGroup = new ButtonGroup();
    private ButtonGroup typeRadioButtonGroup = new ButtonGroup();
    private HashSet<NameParserCommand> fieldNameParser = new LinkedHashSet<>();
    private Project project;
    private LanguageType languageOfClassUnderCaret;

    public SettingsTab(Project project) {
        this.project = project;
        setSize(800, 750);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(700, 750));
        add(filesPane, BorderLayout.CENTER);
        initRadioGroups();
        initListeners();
        initPreferences();
    }

    private static void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) { /* TODO: error handling */ }
        } else { /* TODO: error handling */ }
    }

    private void initPreferences() {
        DtonatorPreferences instance = DtonatorPreferences.getInstance(project);
        if (instance != null && instance.getState() != null) {
            if (instance.getState().getFieldType() != null) {
                switch (instance.getState().getFieldType()) {
                    case JACKSON:
                        jacksonRadioButton.setSelected(true);
                        exposeCheckBox.setSelected(false);
                        break;
                    case GSON:
                        gsonRadioButton.setSelected(true);
                        exposeCheckBox.setSelected(false);
                        break;
                    case POJO:
                        plainClassRadioButton.setSelected(true);
                        exposeCheckBox.setSelected(false);
                        break;
                    case GSON_EXPOSE:
                        gsonRadioButton.setSelected(true);
                        exposeCheckBox.setSelected(true);
                        break;
                    case CUSTOM:
                        customRadioButton.setSelected(true);
                        exposeCheckBox.setSelected(false);
                        customTextField.setVisible(true);
                        break;
                    case AUTO_VALUE:
                        autoValueRadioButton.setSelected(true);
                        exposeCheckBox.setSelected(false);
                        disableEncapsulate();
                        break;
                }
            }
            if (instance.getState().getClassType() != null) {
                switch (instance.getState().getClassType()) {
                    case SEPARATE_FILE:
                        separeteFileForEachRadioButton.setSelected(true);
                        break;
                    case SINGLE_FILE_WITH_INNER_CLASS:
                        singleFile.setSelected(true);
                        break;
                }
            }
            if (instance.getState().getEncapsulete() != null) {
                makeFieldsPrivateRadioButton.setSelected(true);
                provideGetterCheckBox.setVisible(true);
                provideSetterCheckBox.setVisible(true);
                for (FieldEncapsulationOptions fieldEncapsulationOptions : instance.getState().getEncapsulete())
                    switch (fieldEncapsulationOptions) {
                        case PROVIDE_GETTER:
                            provideGetterCheckBox.setSelected(true);
                            break;
                        case PROVIDE_SETTER:
                            provideSetterCheckBox.setSelected(true);
                            break;
                        case PROVIDE_PRIVATE_FIELD:
                            makeFieldsPrivateRadioButton.setSelected(true);
                            break;
                    }
            }
            if (instance.getState().getNaming() != null) {
                for (Naming naming : instance.getState().getNaming()) {
                    switch (naming) {
                        case PREFEIX:
                            if (!instance.getState().getPreixingName().isEmpty()) {
                                prefixFieldWithCheckBox.setSelected(true);
                                nameConventionPrefix.setText(instance.getState().getPreixingName());
                            }
                            break;
                        case CAMEL_CASE:
                            useCamelCaseCheckBox.setSelected(true);
                            break;
                    }
                }
            }
            if (instance.getState().getClassNameOptions() != null) {
                switch (instance.getState().getClassNameOptions()) {
                    case Bean:
                        entityCheckBox.setSelected(false);
                        beanCheckBox.setSelected(true);
                    case Entity:
                        beanCheckBox.setSelected(false);
                        entityCheckBox.setSelected(true);

                }
            }
            String customAnnotationPattern = instance.getState().getCustomAnnotationPattern();
            if (customAnnotationPattern != null && !customAnnotationPattern.isEmpty()) {
                customTextField.setText(customAnnotationPattern);
            }
            if (instance.getState().getLanguageType() != null) {
                switch (instance.getState().getLanguageType()) {
                    case KOTLIN_VAL:
                        valRadioButton.setSelected(true);
                        break;
                    case KOTLIN_VAR:
                        varRadioButton.setSelected(true);
                        break;
                }

            }
        }

    }

    private void initListeners() {
        gsonRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gsonRadioButton.isSelected()) {
                    enableEncapsulate();
                    exposeCheckBox.setVisible(true);
                    customTextField.setVisible(false);
                } else {
                    exposeCheckBox.setVisible(false);
                }
            }
        });
        jacksonRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jacksonRadioButton.isSelected()) {
                    enableEncapsulate();
                    exposeCheckBox.setVisible(false);
                    customTextField.setVisible(false);
                }
            }
        });
        plainClassRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (plainClassRadioButton.isSelected()) {
                    enableEncapsulate();
                    exposeCheckBox.setVisible(false);
                    customTextField.setVisible(false);
                }
            }
        });
        makeFieldsPrivateRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (makeFieldsPrivateRadioButton.isSelected()) {
                    enableEncapsulate();
                    provideGetterCheckBox.setVisible(true);
                    provideSetterCheckBox.setVisible(true);
                    customTextField.setVisible(false);
                } else {
                    provideGetterCheckBox.setVisible(false);
                    provideSetterCheckBox.setVisible(false);
                }
            }
        });
        customRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (customRadioButton.isSelected()) {
                    enableEncapsulate();
                    customTextField.setVisible(true);
                    exposeCheckBox.setVisible(false);
                } else {
                    customTextField.setVisible(false);
                }
            }
        });
        autoValueRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (autoValueRadioButton.isSelected()) {
                    exposeCheckBox.setVisible(false);
                    customTextField.setVisible(false);
                    disableEncapsulate();
                }
            }
        });
        ifYouLikeThisEditorPane.setEditable(false);
        ifYouLikeThisEditorPane.setOpaque(false);
        ifYouLikeThisEditorPane.setContentType("text/html");
        ifYouLikeThisEditorPane.setText("<html> \n" +
                "<body>\n" +
                "<p> If you like this tool, or you want to support our efforts <br>you can do so by adding more  ★ To Our Repository <a href=\\\"https://github.com/nvinayshetty/DTOnator\\\" target=\\\"_blank\\\">@Github</a>.</p>  \n" +
                "<p>You can also do that  <a href=\\\"https://plugins.jetbrains.com/plugin/7834-dto-generator\\\" target=\\\"_blank\\\">@Jetbrains</a> With Your ★ And Reviews</p>\n" +
                "<p>This Is A Free Software Distributed Under <ahref=https://github.com/nvinayshetty/DTOnator/blob/master/LICENSE.md>GNU General Public License v2.0</a></p>\n" +
                "</body>\n" +
                "</html>\n");
        ifYouLikeThisEditorPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent hle) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                    try {
                        final URI uri = new URI(hle.getDescription());
                        open(uri);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        entityCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(entityCheckBox.isSelected()){
                    beanCheckBox.setSelected(false);
                }
            }
        });
        beanCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(beanCheckBox.isSelected()){
                    entityCheckBox.setSelected(false);
                }
            }
        });

    }

    public void hideAutoValue() {
        autoValueRadioButton.setVisible(false);
    }

    private void disableEncapsulate() {
        makeFieldsPrivateRadioButton.setSelected(false);
        provideSetterCheckBox.setSelected(false);
        provideGetterCheckBox.setSelected(false);
        encapsulatePane.setEnabled(false);
        makeFieldsPrivateRadioButton.setEnabled(false);
        provideGetterCheckBox.setEnabled(false);
        provideSetterCheckBox.setEnabled(false);
    }

    private void enableEncapsulate() {
        encapsulatePane.setEnabled(true);
        makeFieldsPrivateRadioButton.setEnabled(true);
        provideGetterCheckBox.setEnabled(true);
        provideSetterCheckBox.setEnabled(true);
    }

    private void initRadioGroups() {
        fileTypeButtonGroup.add(singleFile);
        fileTypeButtonGroup.add(separeteFileForEachRadioButton);
        typeRadioButtonGroup.add(gsonRadioButton);
        typeRadioButtonGroup.add(customRadioButton);
        typeRadioButtonGroup.add(plainClassRadioButton);
        typeRadioButtonGroup.add(autoValueRadioButton);
        typeRadioButtonGroup.add(jacksonRadioButton);
        kotlinButtonGroup.add(valRadioButton);
        kotlinButtonGroup.add(varRadioButton);

    }

    public FieldType getFieldType() {
        if (autoValueRadioButton.isSelected()) {
            return FieldType.AUTO_VALUE;
        }
        if (customRadioButton.isSelected() && !customTextField.getText().isEmpty()) {
            return FieldType.CUSTOM;
        }
        if (jacksonRadioButton.isSelected())
            return FieldType.JACKSON;
        if (plainClassRadioButton.isSelected())
            return FieldType.POJO;
        if (gsonRadioButton.isSelected() && exposeCheckBox.isSelected()) {
            return FieldType.GSON_EXPOSE;
        }

        if (gsonRadioButton.isSelected())
            return FieldType.GSON;
        return null;
    }

    public ClassType getClassType() {
        if (singleFile.isSelected())
            return ClassType.SINGLE_FILE_WITH_INNER_CLASS;
        else if (separeteFileForEachRadioButton.isSelected())
            return ClassType.SEPARATE_FILE;
        else return null;
    }

    public void showSettingAlert(String msg) {
        settingsExceptionLabel.setForeground(Color.red);
        settingsExceptionLabel.setVisible(true);
        settingsExceptionLabel.setText(msg);
        settingsExceptionLabel.getRootPane().invalidate();
        settingsExceptionLabel.getRootPane().validate();
        settingsExceptionLabel.getRootPane().repaint();
    }

    public EnumSet<FieldEncapsulationOptions> getFieldEncapsulationOptions() {
        EnumSet<FieldEncapsulationOptions> fieldEncapsulationOptions = EnumSet.noneOf(FieldEncapsulationOptions.class);
        if (makeFieldsPrivateRadioButton.isSelected())
            fieldEncapsulationOptions.add(FieldEncapsulationOptions.PROVIDE_PRIVATE_FIELD);
        if (provideGetterCheckBox.isSelected())
            fieldEncapsulationOptions.add(FieldEncapsulationOptions.PROVIDE_GETTER);
        if (provideSetterCheckBox.isSelected())
            fieldEncapsulationOptions.add(FieldEncapsulationOptions.PROVIDE_SETTER);
        return fieldEncapsulationOptions;
    }


    public void hideSettingsAlert() {
        settingsExceptionLabel.setVisible(false);
        settingsExceptionLabel.setText("");
        settingsExceptionLabel.getRootPane().invalidate();
        settingsExceptionLabel.getRootPane().validate();
        settingsExceptionLabel.getRootPane().repaint();
    }

    public String getNameConventionPrefixText() {
        return nameConventionPrefix.getText();
    }


    public HashSet<NameParserCommand> getFieldNameParserCommands() {
        NameParserCommand parserCommand;
        if (useCamelCaseCheckBox.isSelected()) {
            parserCommand = new CamelCase();
            updatedFieldNameParserCommands(parserCommand);
        }
        if (prefixFieldWithCheckBox.isSelected()) {
            String fieldPrefixText = getNameConventionPrefixText();
            parserCommand = NamePrefixer.prefixWith(fieldPrefixText);
            updatedFieldNameParserCommands(parserCommand);
        }
        return fieldNameParser;
    }

    private void updatedFieldNameParserCommands(NameParserCommand parserCommand) {
        fieldNameParser.remove(parserCommand);
        fieldNameParser.add(parserCommand);
    }

    public void enableKotlinOptions() {
        kotlinPanel.setVisible(true);
    }

    public void hideKotlinOptions() {
        kotlinPanel.setVisible(false);
    }

    public void hideEncapsulationOptions() {
        encapsulatePane.setVisible(false);

    }

    public void enableEncapsulationOptions() {
        encapsulatePane.setVisible(true);
    }

    public LanguageType getLanguage() {
        if (languageOfClassUnderCaret != LanguageType.KOTLIN)
            return null;
        if (valRadioButton.isSelected() && valRadioButton.isDisplayable())
            return LanguageType.KOTLIN_VAL;
        else if (varRadioButton.isVisible() && valRadioButton.isDisplayable())
            return LanguageType.KOTLIN_VAR;
        return null;
    }

    public ClassNameOptions getClassNameOptions() {
        if (beanCheckBox.isSelected())
            return ClassNameOptions.Bean;
        if (entityCheckBox.isSelected())
            return ClassNameOptions.Entity;
        return null;
    }

    public String getCustomFieldPattern() {
        return customTextField.getText();
    }

    public boolean isAbstractClassWithAnnotation() {
        return autoValueRadioButton.isSelected();
    }

    public void setLanguageOfClassUnderCaret(LanguageType languageOfClassUnderCaret) {
        this.languageOfClassUnderCaret = languageOfClassUnderCaret;
    }


}
