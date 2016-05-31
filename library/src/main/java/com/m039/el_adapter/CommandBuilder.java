package com.m039.el_adapter;

import android.view.View;

/**
 * Created by m039 on 6/1/16.
 */
/* package */ abstract class CommandBuilder<I, V extends View> {

    final protected BaseViewAdapter adapter;

    public CommandBuilder(BaseViewAdapter adapter) {
        this.adapter = adapter;
    }

}
