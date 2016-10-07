package com.m039.el_adapter.perpage;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by defuera on 07/07/2016.
 */
public abstract class FooterLoaderWidget extends FrameLayout {

    public FooterLoaderWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FooterLoaderWidget(Context context) {
        super(context);
    }

    public abstract void showState(State state);

    /**
     * @param errorMessage message to be displayed on state == ERROR
     */
    public void setErrorMessage(String errorMessage) {
    }

    public enum State {
        LOADING, HIDDEN, ERROR
    }

    public abstract void setMessage(@StringRes int message);
}
