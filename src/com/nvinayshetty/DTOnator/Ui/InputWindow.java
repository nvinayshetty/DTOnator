package com.nvinayshetty.DTOnator.Ui;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.nvinayshetty.DTOnator.ActionListener.ContextMenuMouseListener;
import com.nvinayshetty.DTOnator.ClassCreator.ClassType;
import com.nvinayshetty.DTOnator.DtoCreators.*;
import com.nvinayshetty.DTOnator.Logger.ExceptionLogger;

import javax.swing.*;
import java.awt.event.*;
import java.util.EnumSet;

public class InputWindow extends JFrame {
    private PsiClass mClass;
    private Project project;
    private PsiFile mFile;

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

    private ButtonGroup classTypeButtonGroup;
    private ButtonGroup feedTypeButtonGroup;
    private JScrollPane exceptionLoggerPane;

    private EnumSet<PrivateFieldOptions> privateFieldOptions = EnumSet.noneOf(PrivateFieldOptions.class);

    public InputWindow(PsiClass mClass) {
        this.mClass = mClass;
        this.project = mClass.getProject();
        this.mFile = mClass.getContainingFile();
        setContentPane(contentPane);
        inputFeedText.getRootPane().setSize(500, 400);
        setSize(1000, 500);
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
        setPrivateFieldOptionsVisible(false);
        exceptionLoggerPane.setVisible(false);
        gsonRadioButton.setSelected(true);
        creteSingleFile.setSelected(true);
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
                    setPrivateFieldOptionsVisible(true);
                    SetPrivateFieldOptionsSelected(true);
                } else {
                    setPrivateFieldOptionsVisible(false);
                }
            }
        });
    }

    private void SetPrivateFieldOptionsSelected(boolean condition) {
        provideGetter.setSelected(condition);
        provideSetter.setSelected(condition);
    }

    private void setPrivateFieldOptionsVisible(boolean condition) {
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
    }

    private void onOK() {
        getPrivateFieldPreferences();
        DtoCreator dtoCreator = new DtoCreatorBuilder()
                .setJsonSTR(inputFeedText.getText())
                .setFeedType(getFeedType())
                .setPsiClass(mClass)
                .setExceptionLabel(exceptionLabel)
                .setClassType(getFileTypePreference())
                .setFieldType(getFieldTYpe())
                .setPrivateFieldOptions(getPrivateFieldPreferences())
                .build();
        dtoCreator.createDto();
        dispose();
    }

    private FieldType getFieldTYpe() {
        if (pojoRadioButton.isSelected())
            return FieldType.POJO;
        else
            return FieldType.GSON;
    }

    private FeedType getFeedType() {
        //Todo:implement Xml support
        return FeedType.JSON;
    }

    private EnumSet<PrivateFieldOptions> getPrivateFieldPreferences() {
        if (makeFieldsPrivate.isSelected())
            privateFieldOptions.add(PrivateFieldOptions.PROVIDE_PRIVATE_FIELD);
        if (provideGetter.isSelected())
            privateFieldOptions.add(PrivateFieldOptions.PROVIDE_GETTER);
        if (provideSetter.isSelected())
            privateFieldOptions.add(PrivateFieldOptions.PROVIDE_SETTER);
        return privateFieldOptions;
    }

    private ClassType getFileTypePreference() {
        if (creteSingleFile.isSelected())
            return ClassType.SINGLE_FILE_WITH_INNER_CLASS;
        else
            return ClassType.SEPARATE_FILE;
    }


    private void onCancel() {
        dispose();
    }

    public void setData(ExceptionLogger data) {
    }

    public void getData(ExceptionLogger data) {
    }

    public boolean isModified(ExceptionLogger data) {
        return false;
    }
}
