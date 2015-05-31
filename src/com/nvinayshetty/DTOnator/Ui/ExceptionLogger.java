package com.nvinayshetty.DTOnator.Ui;

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
        exceptionLabel.setText(exception.toString());
    }

    public void setErrorLog(String errorText) {
        exceptionLabel.setText(errorText);
    }
}
