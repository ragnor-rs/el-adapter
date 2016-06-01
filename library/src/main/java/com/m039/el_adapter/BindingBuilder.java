package com.m039.el_adapter;

import android.view.View;

/**
 * Created by m039 on 6/1/16.
 */
/* package */ abstract class BindingBuilder<I, V extends View> {

    final protected BaseViewAdapter adapter;
    final protected Class<I> clazz;

    public BindingBuilder(BaseViewAdapter adapter, Class<I> clazz) {
        this.adapter = adapter;
        this.clazz = clazz;
    }

}
