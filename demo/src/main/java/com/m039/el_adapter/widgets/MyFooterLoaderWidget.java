package com.m039.el_adapter.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.m039.el_adapter.demo.R;

public class MyFooterLoaderWidget extends DemoFooterLoaderWidget {
    public MyFooterLoaderWidget(Context context) {
        super(context, R.layout.footer_loader_widget);
    }

    public MyFooterLoaderWidget(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.footer_loader_widget);
    }
}