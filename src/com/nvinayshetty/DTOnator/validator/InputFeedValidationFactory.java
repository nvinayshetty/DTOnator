package com.nvinayshetty.DTOnator.validator;

import com.nvinayshetty.DTOnator.DtoGenerators.FeedType;

import javax.swing.*;

/**
 * Created by vinay on 30/5/15.
 */
public class InputFeedValidationFactory implements FeedValidator {
    FeedValidator feedValidator;
    JLabel exJLabel;

    public InputFeedValidationFactory(FeedType feedType, JLabel exceptionLabel) {
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
