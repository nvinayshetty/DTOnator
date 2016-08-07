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
