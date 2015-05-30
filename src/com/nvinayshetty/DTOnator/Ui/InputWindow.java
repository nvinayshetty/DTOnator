package com.nvinayshetty.DTOnator.Ui;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.nvinayshetty.DTOnator.ActionListener.ContextMenuMouseListener;
import com.nvinayshetty.DTOnator.DtoGenerators.DtoGenerator;

import javax.swing.*;
import java.awt.event.*;

public class InputWindow extends JFrame {
    protected PsiElementFactory mFactory;
    protected PsiFile mFile;
    protected Project project;
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton buttonOk;
    private JTextPane textPane;
    private JRadioButton createSeparateFileRadioButton;
    private JRadioButton creteSingleFileRadioButton;
    private PsiClass mClass;


    public InputWindow(PsiClass mClass, Project project, PsiFile mFile) {
        this.mClass = mClass;
        this.project = project;
        this.mFile = mFile;
        setContentPane(contentPane);
        //setModal(true);
//        setSize(400,600);
        setSize(1000, 1200);
        setTitle("Generate DTO");
        getRootPane().setDefaultButton(buttonOk);

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


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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

    private void onOK() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String jsonSTR = textPane.getText().toString();
                DtoGenerator commandAction = new DtoGenerator(project, mFile, jsonSTR, mClass, mFile);
                commandAction.execute();
                dispose();

            }
        });
    }

    private void onCancel() {
        dispose();
    }


}
