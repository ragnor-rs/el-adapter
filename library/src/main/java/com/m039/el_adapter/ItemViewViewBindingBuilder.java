package com.m039.el_adapter;

import android.view.View;

/**
 * Created by m039 on 6/1/16.
 */
/* package */ class ItemViewViewBindingBuilder<I, V extends View>
        extends ViewBindingBuilder<V> {

    protected Integer viewType;

    protected Class<I> clazz;

    public ItemViewViewBindingBuilder(BaseViewAdapter adapter) {
        super(adapter);
    }

    public ItemViewViewBindingBuilder<I, V> setClass(Class<I> clazz) {
        this.clazz = clazz;
        return this;
    }

    protected ItemViewAdapter getAdapter() {
        return (ItemViewAdapter) adapter;
    }

    public ItemViewViewBindingBuilder<I, V> addViewBinder(ListItemViewAdapter.ItemViewBinder<I, V> itemViewBinder) {
        return addViewHolderBinder(new ListItemViewAdapter.DefaultItemViewHolderBinder<>(itemViewBinder));
    }

    public ItemViewViewBindingBuilder<I, V> addViewBinder(int viewTypeOfClass, ListItemViewAdapter.ItemViewBinder<I, V> itemViewBinder) {
        return addViewHolderBinder(viewTypeOfClass, new ListItemViewAdapter.DefaultItemViewHolderBinder<>(itemViewBinder));
    }

    public ItemViewViewBindingBuilder<I, V> addViewHolderBinder(
            ListItemViewAdapter.ItemViewHolderBinder<I, V> itemViewHolderBinder
    ) {
        return addViewHolderBinder(ListItemViewAdapter.DEFAULT_VIEW_TYPE, itemViewHolderBinder);
    }

    public ItemViewViewBindingBuilder<I, V> addViewHolderBinder(
            int viewTypeOfClass,
            ListItemViewAdapter.ItemViewHolderBinder<I, V> itemViewHolderBinder
    ) {
        if (clazz == null) {
            throw new IllegalArgumentException(ListItemViewAdapter.class.getSimpleName() + " doesn't support binding for no class");
        }

        ItemViewAdapter adapter = getAdapter();

        viewType = adapter.getItemViewType(clazz, viewTypeOfClass);

        adapter.addViewHolderBinder(viewType, itemViewHolderBinder);

        return this;
    }

    // TODO: 6/1/16 clicks

}
