package com.m039.el_adapter.views;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.m039.el_adapter.demo.R;

/**
 * Created by m039 on 6/1/16.
 */
public class RedTextView extends ColoredTextView {

    public RedTextView(Context context) {
        super(context, Color.RED);

        setBackgroundColor(ContextCompat.getColor(context, R.color.black_alpha_10));
    }

}
