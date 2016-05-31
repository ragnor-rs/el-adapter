package com.m039.el_adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by m039 on 6/1/16.
 */
public class DemoRecyclerView extends RecyclerView {

    public DemoRecyclerView(Context context) {
        super(context);
    }

    public DemoRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DemoRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    {
        setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
