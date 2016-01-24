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

package nvinayshetty.DTOnator.FeedValidator;

import nvinayshetty.DTOnator.Logger.ExceptionLogger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;

/**
 * Created by vinay on 30/5/15.
 */
public class JsonFeedValidator implements FeedValidator {
    JSONObject json = null;

    @Override
    public boolean isValidFeed(String inputFeed, JScrollPane exceptionLoggerPane, JLabel exceptionLabel) {
        JSONException exception = null;
        boolean isVallid = true;
        try {
            json = new JSONObject(inputFeed);
        } catch (JSONException ex) {
            exception = ex;
            isVallid = false;
            try {
                JSONArray jsonArray = new JSONArray(inputFeed);
                json = (JSONObject) jsonArray.get(0);
                isVallid = true;
                exception = null;
            } catch (JSONException ex1) {
                exception = ex;
                isVallid = false;
            }
            if (exception != null)
                showAlert(exceptionLoggerPane, exceptionLabel, exception);
        }
        return isVallid;
    }

    private void showAlert(JScrollPane exceptionLoggerPane, JLabel exceptionLabel, JSONException ex1) {
        exceptionLoggerPane.setVisible(true);
        exceptionLabel.setVisible(true);
        new ExceptionLogger(exceptionLabel).Log(ex1);
        exceptionLoggerPane.invalidate();
        exceptionLoggerPane.validate();
        exceptionLoggerPane.repaint();
    }

    @Override
    public Object getValidFeed() {
        return json;
    }


}
