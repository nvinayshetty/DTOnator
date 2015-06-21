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
        try {
            json = new JSONObject(inputFeed);
        } catch (JSONException ex) {
            exceptionLoggerPane.setVisible(true);
            exceptionLabel.setVisible(true);
            new ExceptionLogger(exceptionLabel).Log(ex);
            exceptionLoggerPane.invalidate();
            exceptionLoggerPane.validate();
            exceptionLoggerPane.repaint();
            return false;
        }
        if (json == null) {
            try {
                JSONArray jsonArray = new JSONArray(inputFeed);
                json = (JSONObject) jsonArray.get(0);
            } catch (JSONException ex1) {
                exceptionLoggerPane.setVisible(true);
                exceptionLabel.setVisible(true);
                new ExceptionLogger(exceptionLabel).Log(ex1);
                exceptionLoggerPane.invalidate();
                exceptionLoggerPane.validate();
                exceptionLoggerPane.repaint();
                return false;
            }
        }

        return true;
    }

    @Override
    public Object getValidFeed() {
        return json;
    }


}
