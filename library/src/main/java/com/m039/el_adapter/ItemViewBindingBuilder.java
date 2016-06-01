package com.m039.el_adapter;

import android.view.View;

/**
 * Created by m039 on 6/1/16.
 */
/* package */ class ItemViewBindingBuilder<I, V extends View> extends ItemBindingBuilder<I, V> {

    protected Integer viewType;

    public ItemViewBindingBuilder(BaseViewAdapter adapter) {
        super(adapter);
    }

    BaseItemViewAdapter getAdapter() {
        return (BaseItemViewAdapter) adapter;
    }

    public ItemViewBindingBuilder<I, V> addViewBinder(ListItemViewAdapter.ItemViewBinder<I, V> itemViewBinder) {
        return addViewHolderBinder(new ListItemViewAdapter.DefaultItemViewHolderBinder<>(itemViewBinder));
    }

    public ItemViewBindingBuilder<I, V> addViewBinder(int viewTypeOfClass, ListItemViewAdapter.ItemViewBinder<I, V> itemViewBinder) {
        return addViewHolderBinder(viewTypeOfClass, new ListItemViewAdapter.DefaultItemViewHolderBinder<>(itemViewBinder));
    }

    public ItemViewBindingBuilder<I, V> addViewHolderBinder(
            ListItemViewAdapter.ItemViewHolderBinder<I, V> itemViewHolderBinder
    ) {
        return addViewHolderBinder(ListItemViewAdapter.DEFAULT_VIEW_TYPE, itemViewHolderBinder);
    }

    public ItemViewBindingBuilder<I, V> addViewHolderBinder(
            int viewTypeOfClass,
            ListItemViewAdapter.ItemViewHolderBinder<I, V> itemViewHolderBinder
    ) {
        if (clazz == null) {
            throw new IllegalArgumentException(ListItemViewAdapter.class.getSimpleName() + " doesn't support binding for no class");
        }

        BaseItemViewAdapter adapter = getAdapter();

        viewType = adapter.getItemViewType(clazz, viewTypeOfClass);

        adapter.addViewHolderBinder(viewType, itemViewHolderBinder);

        return this;
    }

}
