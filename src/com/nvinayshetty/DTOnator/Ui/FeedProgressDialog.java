package com.nvinayshetty.DTOnator.Ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by vinay on 14/6/15.
 */
public class FeedProgressDialog extends JDialog {
    private JLabel progressLabel;
    private JProgressBar progressBar;

    public FeedProgressDialog(Frame ownerFrame) {
        super(ownerFrame, "Processing Feed", true);
        progressBar = new JProgressBar(0, 100);
        add(BorderLayout.CENTER, progressBar);
        progressLabel = new JLabel("creating class...");
        add(BorderLayout.NORTH, progressLabel);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(300, 75);
        setLocationRelativeTo(this);
    }

    public void setProgress(int progress) {
        progressBar.setValue(progress);
    }

    public void setProgressLabel(String label) {
        progressLabel.setText("creating class " + label + "...");
    }

    public void showDialog() {
        repaint();
        setVisible(true);
    }

    public void hideDialog() {
        dispose();

    }
}
