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

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.nvinayshetty.DTOnator.ActionListener.ContextMenuMouseListener;
import com.nvinayshetty.DTOnator.ClassCreator.ClassType;
import com.nvinayshetty.DTOnator.DtoCreationOptions.DtoGenerationFactory;
import com.nvinayshetty.DTOnator.DtoCreationOptions.FeedType;
import com.nvinayshetty.DTOnator.DtoCreationOptions.FieldEncapsulationOptions;
import com.nvinayshetty.DTOnator.DtoCreationOptions.FieldType;
import com.nvinayshetty.DTOnator.FeedValidator.InputFeedValidationFactory;
import com.nvinayshetty.DTOnator.NameConventionCommands.CamelCase;
import com.nvinayshetty.DTOnator.NameConventionCommands.NameParserCommand;
import com.nvinayshetty.DTOnator.NameConventionCommands.NamePrefixer;
import com.nvinayshetty.DTOnator.nameConflictResolvers.NameConflictResolverCommand;
import com.nvinayshetty.DTOnator.nameConflictResolvers.PrefixingConflictResolverCommand;

import javax.swing.*;
import java.awt.event.*;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class InputWindow extends JFrame {
    private PsiClass mClass;
    private Project project;

    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton buttonOk;
    private JTextPane inputFeedText;
    private JLabel exceptionLabel;

    private JRadioButton createSeparateFile;
    private JRadioButton creteSingleFile;
    private JRadioButton makeFieldsPrivate;
    private JRadioButton pojoRadioButton;
    private JRadioButton gsonRadioButton;
    private JRadioButton provideSetter;
    private JRadioButton provideGetter;
    private JRadioButton exposeRadioButton;
    private ButtonGroup classTypeButtonGroup;
    private ButtonGroup feedTypeButtonGroup;
    private JScrollPane exceptionLoggerPane;
    private JRadioButton useCamelCaseRadioButton;
    private JRadioButton prefixEachFieldWithNamingConventionRadioButton;
    private JRadioButton OnCOnflictPrefixFieldNameRadioButton;
    private JTextField onConflictprefixString;
    private JTextField nameConventionPrefix;
    private JRadioButton jacksonRadioButton;
    private HashSet<NameConflictResolverCommand> nameConflictResolverCommands;
    private HashSet<NameParserCommand> fieldNameParser = new LinkedHashSet<>();


    public InputWindow(PsiClass mClass) {
        this.mClass = mClass;
        project = mClass.getProject();
        setContentPane(contentPane);
        inputFeedText.getRootPane().setSize(750, 400);
        setSize(1000, 600);
        setTitle("Generate DTO");
        getRootPane().setDefaultButton(buttonOk);
        initButtons();
        initListeners();
        setDefaultConditions();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void setDefaultConditions() {
        exceptionLoggerPane.setVisible(false);
        setEncapsulationOptionsVisible(false);
        gsonRadioButton.setSelected(true);
        creteSingleFile.setSelected(true);
        exposeRadioButton.setVisible(true);
        OnCOnflictPrefixFieldNameRadioButton.setSelected(true);
        onConflictprefixString.setText("m");
    }

    private void initListeners() {
        inputFeedText.addMouseListener(new ContextMenuMouseListener());
        inputFeedText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        onOK();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        onCancel();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        makeFieldsPrivate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (makeFieldsPrivate.isSelected()) {
                    setEncapsulationOptionsVisible(true);
                    SetEncapsulationOptionsSelected(true);
                } else {
                    setEncapsulationOptionsVisible(false);
                }
            }
        });
        gsonRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gsonRadioButton.isSelected()) {
                    exposeRadioButton.setVisible(true);
                } else
                    exposeRadioButton.setVisible(false);
            }
        });
        pojoRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pojoRadioButton.isSelected())
                    exposeRadioButton.setVisible(false);
            }
        });

    }

    private void SetEncapsulationOptionsSelected(boolean condition) {
        provideGetter.setSelected(condition);
        provideSetter.setSelected(condition);
    }

    private void setEncapsulationOptionsVisible(boolean condition) {
        provideGetter.setVisible(condition);
        provideSetter.setVisible(condition);
    }

    private void initButtons() {
        buttonOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        classTypeButtonGroup = new ButtonGroup();
        classTypeButtonGroup.add(createSeparateFile);
        classTypeButtonGroup.add(creteSingleFile);
        feedTypeButtonGroup = new ButtonGroup();
        feedTypeButtonGroup.add(pojoRadioButton);
        feedTypeButtonGroup.add(gsonRadioButton);
        feedTypeButtonGroup.add(jacksonRadioButton);
    }


    private void onOK() {
        final Notification processingNotification = new Notification("DtoGenerator", "Dto generation in Progress", "please wait, it may takes few seconds to generate Dto depending on length of the feed", NotificationType.INFORMATION);
        processingNotification.notify(project);
        InputFeedValidationFactory validator = new InputFeedValidationFactory(getFeedType());
        nameConflictResolverCommands = getNameConflictResolvers();
        HashSet<NameParserCommand> fieldNameParser = getFieldNameParserCommands();
        String text = inputFeedText.getText();
        final boolean isValidFeed = true;//alidator.isValidFeed(text, exceptionLoggerPane, exceptionLabel);
        if (isValidFeed) {
            dispose();
            ClassType classType = getClassType();
            EnumSet<FieldEncapsulationOptions> fieldEncapsulationOptions = getFieldEncapsulationOptions();
            PsiFile containingFile = mClass.getContainingFile();
            String validFeed = validator.getValidFeed();
          //  WriteCommandAction writeAction = DtoGenerationFactory.getDtoGeneratorFor(getFeedType(), classType, getFieldTYpe(), fieldEncapsulationOptions,validFeed, mClass, nameConflictResolverCommands, fieldNameParser, customFieldName);
            //writeAction.execute();
        }
    }

    private HashSet<NameConflictResolverCommand> getNameConflictResolvers() {
        nameConflictResolverCommands = new HashSet<>();
        if (OnCOnflictPrefixFieldNameRadioButton.isSelected()) {
            String prefixString = getOnConflictFieldPrefixText();
            NameConflictResolverCommand prefixingConflictResolver = new PrefixingConflictResolverCommand(prefixString);
            updateFieldNameConflictResoverCommands(prefixingConflictResolver);
        }
        return nameConflictResolverCommands;
    }

    private FieldType getFieldTYpe() {
        if (jacksonRadioButton.isSelected())
            return FieldType.JACKSON;
        if (pojoRadioButton.isSelected())
            return FieldType.POJO;
        if (exposeRadioButton.isSelected())
            return FieldType.GSON_EXPOSE;
        else
            return FieldType.GSON;
    }

    private FeedType getFeedType() {
        return FeedType.JSON;
    }

    private String getOnConflictFieldPrefixText() {
        return onConflictprefixString.getText();
    }

    private String getNameConventionPrefixText() {
        return nameConventionPrefix.getText();
    }


    private void updatedFieldNameParserCommands(NameParserCommand parserCommand) {
        fieldNameParser.remove(parserCommand);
        fieldNameParser.add(parserCommand);
    }

    private void updateFieldNameConflictResoverCommands(NameConflictResolverCommand conflictResolver) {
        nameConflictResolverCommands.remove(conflictResolver);
        nameConflictResolverCommands.add(conflictResolver);
    }

    private HashSet<NameParserCommand> getFieldNameParserCommands() {
        NameParserCommand parserCommand;
        if (useCamelCaseRadioButton.isSelected()) {
            parserCommand = new CamelCase();
            updatedFieldNameParserCommands(parserCommand);
        }
        if (prefixEachFieldWithNamingConventionRadioButton.isSelected()) {
            String fieldPrefixText = getNameConventionPrefixText();
            parserCommand = NamePrefixer.prefixWith(fieldPrefixText);
            updatedFieldNameParserCommands(parserCommand);
        }
        return fieldNameParser;
    }

    private EnumSet<FieldEncapsulationOptions> getFieldEncapsulationOptions() {
        EnumSet<FieldEncapsulationOptions> fieldEncapsulationOptions = EnumSet.noneOf(FieldEncapsulationOptions.class);
        if (makeFieldsPrivate.isSelected())
            fieldEncapsulationOptions.add(FieldEncapsulationOptions.PROVIDE_PRIVATE_FIELD);
        if (provideGetter.isSelected())
            fieldEncapsulationOptions.add(FieldEncapsulationOptions.PROVIDE_GETTER);
        if (provideSetter.isSelected())
            fieldEncapsulationOptions.add(FieldEncapsulationOptions.PROVIDE_SETTER);
        return fieldEncapsulationOptions;
    }

    private ClassType getClassType() {
        if (creteSingleFile.isSelected())
            return ClassType.SINGLE_FILE_WITH_INNER_CLASS;
        else
            return ClassType.SEPARATE_FILE;
    }


    private void onCancel() {
        dispose();
    }

}
