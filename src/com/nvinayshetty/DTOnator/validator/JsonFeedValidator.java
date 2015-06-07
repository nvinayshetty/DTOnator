package com.nvinayshetty.DTOnator.validator;

import com.nvinayshetty.DTOnator.Logger.ExceptionLogger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;

/**
 * Created by vinay on 30/5/15.
 */
public class JsonFeedValidator implements FeedValidator {
    JSONObject json;

    @Override
    public boolean isValidFeed(String inputFeed, JLabel exceptionLabel) {
        try {
            json = new JSONObject(inputFeed);
        } catch (JSONException ex) {
            //Todo:remove this log
            new ExceptionLogger(exceptionLabel).Log(ex);
            try {
                JSONArray jsonArray = new JSONArray(inputFeed);
                Object jsonobject = jsonArray.get(0);
                if (jsonobject instanceof JSONObject)
                    json = (JSONObject) jsonobject;
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
