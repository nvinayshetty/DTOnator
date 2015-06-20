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
    public boolean isValidFeed(String inputFeed, JLabel exceptionLabel) {
        try {
            json = new JSONObject(inputFeed);
        } catch (JSONException ex) {
            new ExceptionLogger(exceptionLabel).Log(ex);
            return false;
        }
        if (json == null) {
            try {
                JSONArray jsonArray = new JSONArray(inputFeed);
                json = (JSONObject) jsonArray.get(0);
            } catch (JSONException ex1) {
                new ExceptionLogger(exceptionLabel).Log(ex1);
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
