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


import com.nvinayshetty.DTOnator.DtoCreationOptions.FeedType;
import org.json.JSONException;

import javax.swing.*;

/**
 * Created by vinay on 30/5/15.
 */
public class InputFeedValidationFactory implements FeedValidator {
    FeedValidator feedValidator;

    public InputFeedValidationFactory(FeedType feedType) {
        switch (feedType) {
            case JSON:
                feedValidator = new JsonFeedValidator();
                break;
        }
    }

    @Override
    public boolean isValidFeed(String inputFeed, JLabel exceptionLabel) {
        return feedValidator.isValidFeed(inputFeed, exceptionLabel);
    }

    @Override
    public JSONException getException() {
        return feedValidator.getException();
    }

    @Override
    public String getValidFeed() {
        return feedValidator.getValidFeed();
    }
}
