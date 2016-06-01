package com.m039.el_adapter;

import android.view.View;

/**
 * Created by m039 on 6/1/16.
 */
/* package */ abstract class ViewBindingBuilder<V extends View> {

    final protected BaseViewAdapter adapter;
    final protected int viewType;

    public ViewBindingBuilder(BaseViewAdapter adapter, int viewType) {
        this.adapter = adapter;
        this.viewType = viewType;
    }

}
