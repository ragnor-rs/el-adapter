package com.m039.el_adapter;

import android.view.View;

/**
 * Created by m039 on 6/1/16.
 */
/* package */ class ItemBindingBuilder<I, V extends View> extends BindingBuilder {

    protected Class<I> clazz;

    public ItemBindingBuilder(BaseViewAdapter adapter) {
        super(adapter);
    }

    public ItemBindingBuilder<I, V> setClass(Class<I> clazz) {
        this.clazz = clazz;
        return this;
    }

}
