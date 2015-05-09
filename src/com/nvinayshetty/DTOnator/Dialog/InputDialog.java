package com.nvinayshetty.DTOnator.Dialog;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.nvinayshetty.DTOnator.Common.ContextMenuMouseListener;
import com.nvinayshetty.DTOnator.DtoGenerators.DtoGenerator;

import javax.swing.*;
import java.awt.event.*;

public class InputDialog extends JDialog {
    protected PsiElementFactory mFactory;
    protected PsiFile mFile;
    protected Project project;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextPane textPane;
    private PsiClass mClass;


    public InputDialog(PsiClass mClass, Project project, PsiFile mFile) {
        this.mClass = mClass;
        this.project = project;
        this.mFile = mFile;
        setContentPane(contentPane);
        setModal(true);
        setTitle("Generate DTO");
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

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

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        dispose();

        String jsonSTR = textPane.getText().toString();
        new DtoGenerator(project, mFile, jsonSTR, mClass, mFile).execute();
    }

    private void onCancel() {
        dispose();
    }


}
