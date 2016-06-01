package com.m039.el_adapter;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by m039 on 6/1/16.
 */
/* package */ abstract class CommandBuilder<I, V extends View> {

    final protected BaseViewAdapter adapter;

    @Nullable
    final protected Class<I> clazz;

    public CommandBuilder(BaseViewAdapter adapter, @Nullable Class<I> clazz) {
        this.adapter = adapter;
        this.clazz = clazz;
    }

}
