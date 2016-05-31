package com.m039.el_adapter;

import android.view.View;

import com.m039.el_adapter.BaseViewAdapter.ViewCreator;
import com.m039.el_adapter.BaseViewAdapter.ViewHolderCreator;

/**
 * Created by m039 on 6/1/16.
 */
/* package */ class ItemViewCommandBuilder<I, V extends View> extends CommandBuilder<I, V> {

    public ItemViewCommandBuilder(BaseViewAdapter adapter) {
        super(adapter);
    }

    ItemViewAdapter getAdapter() {
        return (ItemViewAdapter) adapter;
    }

    @SuppressWarnings("unchecked")
    public <II, VV extends View, B extends ItemViewCommandBuilder<II, VV>>
    B addViewCreator(Class<II> clazz, ViewCreator<VV> viewCreator) {
        return (B) adapter.addViewCreator(clazz, viewCreator);
    }

    @SuppressWarnings("unchecked")
    public <II, VV extends View, B extends ItemViewCommandBuilder<II, VV>>
    B addViewCreator(Class<II> clazz, int viewTypeOfClass, ViewCreator<VV> viewCreator) {
        return (B) adapter.addViewCreator(clazz, viewTypeOfClass, viewCreator);
    }

    @SuppressWarnings("unchecked")
    public <II, VV extends View, B extends ItemViewCommandBuilder<II, VV>>
    B addViewCreator(int viewType, ViewCreator<VV> viewCreator) {
        return (B) adapter.addViewCreator(viewType, viewCreator);
    }

    @SuppressWarnings("unchecked")
    public <II, VV extends View, B extends ItemViewCommandBuilder<II, VV>>
    B addViewHolderCreator(Class<II> clazz, ViewHolderCreator<VV> viewHolderCreator) {
        return (B) adapter.addViewHolderCreator(clazz, viewHolderCreator);
    }

    @SuppressWarnings("unchecked")
    public <II, VV extends View, B extends ItemViewCommandBuilder<II, VV>>
    B addViewHolderCreator(Class<II> clazz, int viewTypeOfClass, ViewHolderCreator<VV> viewHolderCreator) {
        return (B) adapter.addViewHolderCreator(clazz, viewTypeOfClass, viewHolderCreator);
    }

    @SuppressWarnings("unchecked")
    public <II, VV extends View, B extends ItemViewCommandBuilder<II, VV>>
    B addViewHolderCreator(int viewType, ViewHolderCreator<VV> viewHolderCreator) {
        return (B) adapter.addViewHolderCreator(viewType, viewHolderCreator);
    }

    public ItemViewCommandBuilder<I, V> addViewBinder(Class<I> clazz, ItemViewAdapter.ItemViewBinder<I, V> itemViewBinder) {
        addViewHolderBinder(clazz, new ItemViewAdapter.DefaultItemViewHolderBinder<>(itemViewBinder));
        return this;
    }

    public ItemViewCommandBuilder<I, V> addViewBinder(Class<I> clazz, int viewTypeOfClass, ItemViewAdapter.ItemViewBinder<I, V> itemViewBinder) {
        addViewHolderBinder(clazz, viewTypeOfClass, new ItemViewAdapter.DefaultItemViewHolderBinder<>(itemViewBinder));
        return this;
    }

    public ItemViewCommandBuilder<I, V> addViewHolderBinder(
            Class<I> clazz,
            ItemViewAdapter.ItemViewHolderBinder<I, V> itemViewHolderBinder
    ) {
        addViewHolderBinder(clazz, BaseViewAdapter.DEFAULT_VIEW_TYPE, itemViewHolderBinder);
        return this;
    }

    public ItemViewCommandBuilder<I, V> addViewHolderBinder(
            Class<I> clazz,
            int viewTypeOfClass,
            ItemViewAdapter.ItemViewHolderBinder<I, V> itemViewHolderBinder
    ) {
        ItemViewAdapter adapter = getAdapter();
        int viewType = adapter.getItemViewType(clazz, viewTypeOfClass);
        adapter.mItemViewHolderBinderByViewType.put(viewType, itemViewHolderBinder);
        return this;
    }

}
