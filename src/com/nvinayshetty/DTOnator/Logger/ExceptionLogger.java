package com.nvinayshetty.DTOnator.Logger;

import javax.swing.*;

/**
 * Created by vinay on 30/5/15.
 */
public class ExceptionLogger {
    JLabel exceptionLabel;

    public ExceptionLogger(JLabel exceptionLabel) {
        this.exceptionLabel = exceptionLabel;
    }

    public void Log(Exception exception) {
        exceptionLabel.setVisible(true);
        exceptionLabel.setText(exception.toString());

        exceptionLabel.getRootPane().invalidate();
        exceptionLabel.getRootPane().validate();
        exceptionLabel.getRootPane().repaint();
    }

    public void setErrorLog(String errorText) {
        exceptionLabel.setText(errorText);
    }
}
