package com.m039.el_adapter.views;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m039.el_adapter.demo.R;

import static android.view.ViewGroup.LayoutParams.*;

/**
 * Created by m039 on 6/1/16.
 */
/* package */ class ColoredTextView extends TextView {

    public ColoredTextView(Context context, int color) {
        super(context);
        setTextColor(color);

        int padding = context.getResources().getDimensionPixelOffset(R.dimen.padding_16);
        setPadding(padding, padding, padding, padding);

        setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
    }

}
