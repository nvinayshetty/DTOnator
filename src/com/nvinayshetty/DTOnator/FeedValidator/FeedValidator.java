package com.nvinayshetty.DTOnator.FeedValidator;

import javax.swing.*;

/**
 * Created by vinay on 30/5/15.
 */
public interface FeedValidator {

    boolean isValidFeed(String inputFeed, JLabel label);

    public Object getValidFeed();
}
