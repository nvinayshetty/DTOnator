package com.nvinayshetty.DTOnator.validator;

import javax.swing.*;

/**
 * Created by vinay on 30/5/15.
 */
public class InputValidator implements FeedValidator {
    FeedValidator feedValidator;
    JLabel exJLabel;

    public InputValidator(FeedType feedType, JLabel exceptionLabel) {
        exJLabel = exceptionLabel;
        switch (feedType) {
            case JsonObject:
                feedValidator = new JsonFeedValidator();
                break;
            case JsonArray:
                break;
            case XmlFeed:
                break;
        }
    }

    @Override
    public boolean isValidFeed(String inputFeed, JLabel exceptionLabel) {
        return feedValidator.isValidFeed(inputFeed, exceptionLabel);
    }

    @Override
    public Object getValidFeed() {
        return feedValidator.getValidFeed();
    }
}
