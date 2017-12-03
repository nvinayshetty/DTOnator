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

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.nvinayshetty.DTOnator.Logger.ExceptionLogger;
import org.json.JSONTokener;

import javax.swing.*;

/**
 * Created by vinay on 30/5/15.
 */
public class JsonFeedValidator implements FeedValidator {
    private String input;


    @Override
    public boolean isValidFeed(String inputFeed, JScrollPane exceptionLoggerPane, JLabel exceptionLabel) {
        try {
            this.input = inputFeed;
            JsonParser parser = new JsonParser();
            parser.parse(inputFeed);
            return true;
        } catch (JsonSyntaxException jse) {
            System.out.println("Not a valid Json String:" + jse.getMessage());
            showAlert(exceptionLoggerPane, exceptionLabel, jse);
            return false;
        }

    }


    public boolean containesEmptyJsonList(String jsonString){
        JSONTokener jsonTokener = new JSONTokener(input);
        Object object = jsonTokener.nextValue();
        return false;
    }
    private void showAlert(JScrollPane exceptionLoggerPane, JLabel exceptionLabel, JsonSyntaxException ex) {
        exceptionLoggerPane.setVisible(true);
        exceptionLabel.setVisible(true);
        new ExceptionLogger(exceptionLabel).Log(ex);
        exceptionLoggerPane.invalidate();
        exceptionLoggerPane.validate();
        exceptionLoggerPane.repaint();
    }

    @Override
    public String getValidFeed() {
        return input;
    }


}
