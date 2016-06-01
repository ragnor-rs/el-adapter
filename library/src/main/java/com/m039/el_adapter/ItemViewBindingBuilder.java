package com.m039.el_adapter;

import android.view.View;

/**
 * Created by m039 on 6/1/16.
 */
/* package */ class ItemViewBindingBuilder<I, V extends View> extends BindingBuilder<I, V> {

    public ItemViewBindingBuilder(BaseViewAdapter adapter, Class<I> clazz) {
        super(adapter, clazz);
    }

    ItemViewAdapter getAdapter() {
        return (ItemViewAdapter) adapter;
    }

    public ItemViewBindingBuilder<I, V> addViewBinder(ItemViewAdapter.ItemViewBinder<I, V> itemViewBinder) {
        addViewHolderBinder(new ItemViewAdapter.DefaultItemViewHolderBinder<>(itemViewBinder));
        return this;
    }

    public ItemViewBindingBuilder<I, V> addViewBinder(int viewTypeOfClass, ItemViewAdapter.ItemViewBinder<I, V> itemViewBinder) {
        addViewHolderBinder(viewTypeOfClass, new ItemViewAdapter.DefaultItemViewHolderBinder<>(itemViewBinder));
        return this;
    }

    public ItemViewBindingBuilder<I, V> addViewHolderBinder(
            ItemViewAdapter.ItemViewHolderBinder<I, V> itemViewHolderBinder
    ) {
        addViewHolderBinder(BaseViewAdapter.DEFAULT_VIEW_TYPE, itemViewHolderBinder);
        return this;
    }

    public ItemViewBindingBuilder<I, V> addViewHolderBinder(
            int viewTypeOfClass,
            ItemViewAdapter.ItemViewHolderBinder<I, V> itemViewHolderBinder
    ) {
        getAdapter().addViewHolderBinder(clazz, viewTypeOfClass, itemViewHolderBinder);
        return this;
    }

}
