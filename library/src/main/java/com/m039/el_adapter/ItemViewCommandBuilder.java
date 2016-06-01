package com.m039.el_adapter;

import android.view.View;

/**
 * Created by m039 on 6/1/16.
 */
/* package */ class ItemViewCommandBuilder<I, V extends View> extends CommandBuilder<I, V> {

    public ItemViewCommandBuilder(BaseViewAdapter adapter, Class<I> clazz) {
        super(adapter, clazz);
    }

    ItemViewAdapter getAdapter() {
        return (ItemViewAdapter) adapter;
    }

    public ItemViewCommandBuilder<I, V> addViewBinder(ItemViewAdapter.ItemViewBinder<I, V> itemViewBinder) {
        addViewHolderBinder(new ItemViewAdapter.DefaultItemViewHolderBinder<>(itemViewBinder));
        return this;
    }

    public ItemViewCommandBuilder<I, V> addViewBinder(int viewTypeOfClass, ItemViewAdapter.ItemViewBinder<I, V> itemViewBinder) {
        addViewHolderBinder(viewTypeOfClass, new ItemViewAdapter.DefaultItemViewHolderBinder<>(itemViewBinder));
        return this;
    }

    public ItemViewCommandBuilder<I, V> addViewHolderBinder(
            ItemViewAdapter.ItemViewHolderBinder<I, V> itemViewHolderBinder
    ) {
        addViewHolderBinder(BaseViewAdapter.DEFAULT_VIEW_TYPE, itemViewHolderBinder);
        return this;
    }

    public ItemViewCommandBuilder<I, V> addViewHolderBinder(
            int viewTypeOfClass,
            ItemViewAdapter.ItemViewHolderBinder<I, V> itemViewHolderBinder
    ) {
        getAdapter().addViewHolderBinder(clazz, viewTypeOfClass, itemViewHolderBinder);
        return this;
    }

}
