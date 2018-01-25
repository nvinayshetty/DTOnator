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

package com.nvinayshetty.DTOnator.FeedValidator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;

/**
 * Created by vinay on 30/5/15.
 */
public class JsonFeedValidator implements FeedValidator {
    private JSONException exception;
    private String input;

    @Override
    public boolean isValidFeed(String inputFeed, JLabel exceptionLabel) {
        input=inputFeed;
        if (inputFeed.startsWith("{")) {
            try {
                new JSONObject(inputFeed);
            } catch (JSONException ex) {
                this.exception = ex;
                return false;
            }
        } else if (inputFeed.startsWith("[")) {
            try {
                new JSONArray(inputFeed);
            } catch (JSONException exception) {
                this.exception = exception;
                return false;
            }
        } else {
            try {
                new JSONObject(inputFeed);
            } catch (JSONException ex) {
                try {
                    new JSONArray(inputFeed);
                } catch (JSONException exception) {
                    this.exception = exception;
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public JSONException getException() {
        return exception;
    }

    @Override
    public String getValidFeed() {
        return input;
    }
}
