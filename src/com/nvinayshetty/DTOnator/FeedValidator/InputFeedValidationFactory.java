package com.nvinayshetty.DTOnator.FeedValidator;

import com.nvinayshetty.DTOnator.DtoCreators.FeedType;

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
            case XML:
                break;
        }
    }

    @Override
    public boolean isValidFeed(String inputFeed, JScrollPane exceptionLoggerPane, JLabel exceptionLabel) {
        return feedValidator.isValidFeed(inputFeed, exceptionLoggerPane, exceptionLabel);
    }

    @Override
    public Object getValidFeed() {
        return feedValidator.getValidFeed();
    }
}
