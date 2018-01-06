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

import com.nvinayshetty.DTOnator.ClassCreator.ClassType;
import com.nvinayshetty.DTOnator.DtoCreationOptions.FieldEncapsulationOptions;
import com.nvinayshetty.DTOnator.DtoCreationOptions.FieldType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;

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
    private JTextArea someReallyLongAndTextArea;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JTextField textField1;
    private ButtonGroup fileTypeButtonGroup = new ButtonGroup();
    private ButtonGroup typeRadioButtonGroup = new ButtonGroup();

    public SettingsTab() {
        setSize(700, 700);
        setLayout(new BorderLayout());
        filesPane.setPreferredSize(new Dimension(700, 50));
        filesPane.setSize(700, 50);
        setPreferredSize(new Dimension(700, 700));
        add(filesPane, BorderLayout.CENTER);
        initRadioGroups();
        initListeners();
    }

    private void initListeners() {
        gsonRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gsonRadioButton.isSelected()) {
                    exposeCheckBox.setVisible(true);
                } else {
                    exposeCheckBox.setVisible(false);
                }
            }
        });
        jacksonRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jacksonRadioButton.isSelected()) {
                    exposeCheckBox.setVisible(false);
                }
            }
        });
        plainClassRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (plainClassRadioButton.isSelected()) {
                    exposeCheckBox.setVisible(false);
                }
            }
        });
    }

    private void initRadioGroups() {
        fileTypeButtonGroup.add(singleFile);
        fileTypeButtonGroup.add(separeteFileForEachRadioButton);
        typeRadioButtonGroup.add(gsonRadioButton);
        typeRadioButtonGroup.add(plainClassRadioButton);
        typeRadioButtonGroup.add(jacksonRadioButton);
    }

    public FieldType getFieldType() {
        if (jacksonRadioButton.isSelected())
            return FieldType.JACKSON;
        if (plainClassRadioButton.isSelected())
            return FieldType.POJO;
        if (gsonRadioButton.isSelected() && exposeCheckBox.isSelected()) {
            return FieldType.GSON_EXPOSE;
        }
        return FieldType.GSON;

    }

    public ClassType getClassType() {
        if (singleFile.isSelected())
            return ClassType.SINGLE_FILE_WITH_INNER_CLASS;
        else
            return ClassType.SEPARATE_FILE;
    }

    public EnumSet<FieldEncapsulationOptions> getFieldEncapsulationOptions() {
        EnumSet<FieldEncapsulationOptions> fieldEncapsulationOptions = EnumSet.noneOf(FieldEncapsulationOptions.class);
       /* if (makeFieldsPrivate.isSelected())
            fieldEncapsulationOptions.add(FieldEncapsulationOptions.PROVIDE_PRIVATE_FIELD);
        if (provideGetter.isSelected())
            fieldEncapsulationOptions.add(FieldEncapsulationOptions.PROVIDE_GETTER);
        if (provideSetter.isSelected())
            fieldEncapsulationOptions.add(FieldEncapsulationOptions.PROVIDE_SETTER);*/
        return fieldEncapsulationOptions;
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
