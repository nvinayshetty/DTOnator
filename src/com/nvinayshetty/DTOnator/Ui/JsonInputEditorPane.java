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

import com.google.gson.*;
import com.intellij.json.JsonLanguage;
import com.intellij.lang.Language;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.nvinayshetty.DTOnator.ClassCreator.ClassType;
import com.nvinayshetty.DTOnator.DtoCreationOptions.FeedType;
import com.nvinayshetty.DTOnator.DtoCreationOptions.FieldEncapsulationOptions;
import com.nvinayshetty.DTOnator.DtoCreationOptions.FieldType;
import com.nvinayshetty.DTOnator.FeedValidator.InputFeedValidationFactory;
import com.nvinayshetty.DTOnator.FieldCreator.LanguageType;
import com.nvinayshetty.DTOnator.Logger.ExceptionLogger;
import com.nvinayshetty.DTOnator.NameConventionCommands.ClassName.ClassNameOptions;
import com.nvinayshetty.DTOnator.NameConventionCommands.NameParserCommand;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;
import java.util.HashSet;

public class JsonInputEditorPane extends JPanel {
    private JPanel buttonsPanel;
    private JLabel exceptionLabel;
    private JButton validate;
    private JButton format;
    private JButton generate;
    private InputFeedValidationFactory validator;
    private Project project;
    private SettingListener settingListener;
    private PsiFile psiFile;
    private FileEditor fileEditor;


    public JsonInputEditorPane(Project project, SettingListener settingListener) {
        this.project = project;
        this.settingListener = settingListener;
        setLayout(new BorderLayout());
        buttonsPanel = new JPanel();
        exceptionLabel = new JLabel();
        buttonsPanel.setLayout(new GridBagLayout());
        createEditor(project);
        createButtonsPanel();
        createButtonClickListeners();
        validator = new InputFeedValidationFactory(FeedType.JSON);
    }

    private void createButtonClickListeners() {
        validate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String inputText = getInputText();
                boolean validFeed = validator.isValidFeed(inputText, exceptionLabel);
                if (!validFeed) {
                    showError();
                } else {
                    showValid();
                    formatInput(inputText);
                }
            }
        });
        format.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final String inputText = getInputText();
                boolean validFeed = validator.isValidFeed(inputText, exceptionLabel);
                if (!validFeed) {
                    showError();
                } else {
                    formatInput(inputText);
                }
            }
        });
        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String inputText = getInputText();
                boolean validFeed = validator.isValidFeed(inputText, exceptionLabel);
                if (!validFeed) {
                    showError();
                } else {
                    settingListener.onGenerateClickWithValidFeed(inputText);
                }
            }
        });
    }

    private void formatInput(String inputText) {
        JSONTokener jsonTokener = new JSONTokener(inputText);
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonParser jsonParser = new JsonParser();
        Object object = jsonTokener.nextValue();
        if (object instanceof JSONObject) {

            JsonObject json = jsonParser.parse(inputText).getAsJsonObject();
            String prettyJson = gson.toJson(json);
            updateInputText(prettyJson);
        }else{
            JsonArray json = jsonParser.parse(inputText).getAsJsonArray();
            String prettyJson = gson.toJson(json);
            updateInputText(prettyJson);
        }
    }

    private void showError() {
        JSONException errorDescription = validator.getException();
        showAlert(errorDescription);
    }

    private void showValid() {
        exceptionLabel.setVisible(true);
        exceptionLabel.setForeground(Color.GREEN);
        exceptionLabel.setText("Valid!!");
        exceptionLabel.getRootPane().invalidate();
        exceptionLabel.getRootPane().validate();
        exceptionLabel.getRootPane().repaint();
    }

    private void createButtonsPanel() {
        add(buttonsPanel, BorderLayout.PAGE_END);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        buttonsPanel.add(exceptionLabel, c);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.5;
        validate = new JButton("Validate");
        buttonsPanel.add(validate, c);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.5;
        format = new JButton("Format");
        buttonsPanel.add(format, c);
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 0.5;
        generate = new JButton("Generate");
        buttonsPanel.add(generate, c);
    }

    private String getInputText() {
        return psiFile.getText().trim();
    }

    private void updateInputText(final String prettyJson) {
        new WriteCommandAction(project) {
            @Override
            protected void run(@NotNull Result result) throws Throwable {
                TextEditor textComponent = (TextEditor) fileEditor;
                textComponent.getEditor().getDocument().setText(prettyJson);
            }
        }.execute();


    }

    private void createEditor(Project project) {
        Language language = Language.findLanguageByID(JsonLanguage.INSTANCE.getID());
        psiFile = PsiFileFactory.getInstance(project).createFileFromText(language, "");
        fileEditor = TextEditorProvider.getInstance().createEditor(project, psiFile.getVirtualFile());
        fileEditor.getComponent().setPreferredSize(new Dimension(920, 200));
        add(fileEditor.getComponent(), BorderLayout.CENTER);
    }

    private void showAlert(JSONException ex) {
        exceptionLabel.setForeground(Color.red);
        new ExceptionLogger(exceptionLabel).Log(ex);
    }

    public void showAlert(String msg) {
        exceptionLabel.setVisible(true);
        exceptionLabel.setText(msg);
        exceptionLabel.getRootPane().invalidate();
        exceptionLabel.getRootPane().validate();
        exceptionLabel.getRootPane().repaint();
    }


    public interface GenerateClickListener {
        void onGenerateButtonClick(FieldType fieldType, ClassType classType, EnumSet<FieldEncapsulationOptions> fieldEncapsulationOptions, String input, HashSet<NameParserCommand> nameParserCommands, LanguageType language, String customFieldName, boolean abstractClassWithAnnotation, ClassNameOptions classNameOptions);
    }

    public interface SettingListener {
        void onGenerateClickWithValidFeed(String input);

    }

}
