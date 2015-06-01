package com.nvinayshetty.DTOnator.Ui;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.nvinayshetty.DTOnator.ActionListener.ContextMenuMouseListener;
import com.nvinayshetty.DTOnator.DtoGenerators.DtoCreator;
import com.nvinayshetty.DTOnator.DtoGenerators.DtoCreatorBuilder;
import com.nvinayshetty.DTOnator.DtoGenerators.DtoFileCreationPreference;

import javax.swing.*;
import java.awt.event.*;

public class InputWindow extends JFrame {
    private PsiElementFactory mFactory;
    private PsiFile mFile;
    private PsiClass mClass;
    private Project project;
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton buttonOk;
    private JLabel exceptionLabel;
    private JTextPane textPane;
    private JRadioButton createSeparateFileRadioButton;
    private JRadioButton creteSingleFileRadioButton;
    private ButtonGroup radioButtonGroup;


    public InputWindow(PsiClass mClass, Project project, PsiFile mFile) {
        this.mClass = mClass;
        this.project = project;
        this.mFile = mFile;
        setContentPane(contentPane);
        setSize(1000, 1200);
        setTitle("Generate DTO");
        getRootPane().setDefaultButton(buttonOk);
        initButtons();
        initListeners();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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

    private void initListeners() {
        textPane.addMouseListener(new ContextMenuMouseListener());
        textPane.addKeyListener(new KeyListener() {
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
        radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(createSeparateFileRadioButton);
        radioButtonGroup.add(creteSingleFileRadioButton);
        creteSingleFileRadioButton.setSelected(true);
    }

    private void onOK() {
        DtoCreator dtoCreator = new DtoCreatorBuilder().setJsonSTR(textPane.getText()).setProject(project).setPsiFile(mFile).setPsiClass(mClass).setExceptionLabel(exceptionLabel).setFileCreationPreference(getFileCreationPreference()).build();
        dtoCreator.createDto();
        dispose();
    }

    private DtoFileCreationPreference getFileCreationPreference() {
        if (createSeparateFileRadioButton.isSelected())
            return DtoFileCreationPreference.SEPARATE_FILE;
        else
            return DtoFileCreationPreference.SINGLE_FILE_WITH_INNER_CLASS;
    }

    private void onCancel() {
        dispose();
    }


}
