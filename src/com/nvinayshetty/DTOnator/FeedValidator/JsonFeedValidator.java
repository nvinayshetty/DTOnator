package com.nvinayshetty.DTOnator.FeedValidator;

import com.nvinayshetty.DTOnator.Logger.ExceptionLogger;
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
