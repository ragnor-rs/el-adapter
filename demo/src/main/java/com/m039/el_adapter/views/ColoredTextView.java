package com.m039.el_adapter.views;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by m039 on 6/1/16.
 */
/* package */ class ColoredTextView extends TextView {

    public ColoredTextView(Context context, int color) {
        super(context);
        setTextColor(color);
    }

}
