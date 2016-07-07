package com.m039.el_adapter.perpage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by defuera on 07/07/2016.
 */
public abstract class FooterLoaderWidget extends View {

    public FooterLoaderWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FooterLoaderWidget(Context context) {
        super(context);
    }

    public abstract void showNetworkError();

    public abstract void showEmptyView();
}
