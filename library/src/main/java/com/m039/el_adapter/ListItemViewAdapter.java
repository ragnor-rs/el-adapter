package com.m039.el_adapter;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * todo javadoc
 * Created by m039 on 3/21/16.
 */
public class ListItemViewAdapter extends ItemViewAdapter<ItemViewViewBindingBuilder> {

    private final List<Object> mItems = new ArrayList<>();

    public ListItemViewAdapter() {
        super(ItemViewViewBindingBuilder.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, ItemViewHolderBinder binder) {
        binder.onBindViewHolder(holder, mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void removeItem(int position) {
        removeItemsRange(position, 1);
    }

    public void removeAllItems() {
        removeItemsRange(0, mItems.size());
    }

    public void removeItemsRange(int positionStart, int itemCount) {
        if (mItems.size() < itemCount || itemCount <= 0) {
            return;
        }
        mItems.subList(positionStart, positionStart + itemCount).clear();
    }

    public <T> void addItem(T item) {
        mItems.add(item);
    }

    public <T> void addItem(int index, T item) {
        mItems.add(index, item);
    }

    public <I> void addItems(@NonNull List<I> items) {
        if (!items.isEmpty()) {
            mItems.addAll(items);
        }
    }

    public <I> void addItems(@NonNull I items[]) {
        if (items.length > 0) {
            mItems.addAll(Arrays.asList(items));
        }
    }

    public <I> void addItems(int index, @NonNull List<I> items) {
        if (!items.isEmpty()) {
            mItems.addAll(index, items);
        }
    }

    public List<Object> getItems() {
        return mItems;
    }

    public Object getItemAt(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(mItems.get(position).getClass(), getViewTypeOfClass(position));
    }

    @SuppressWarnings("unused")
    protected int getViewTypeOfClass(int position) {
        return DEFAULT_VIEW_TYPE;
    }

    //
    // Parametrization
    //

    @Override
    @SuppressWarnings("unchecked")
    public <I, V extends View>
    ItemViewViewBindingBuilder<I, V> addViewCreator(Class<I> clazz, ViewCreator<V> viewCreator) {
        return (ItemViewViewBindingBuilder<I, V>) super.addViewCreator(clazz, viewCreator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I, V extends View>
    ItemViewViewBindingBuilder<I, V> addViewCreator(Class<I> clazz, int viewTypeOfClass, ViewCreator<V> viewCreator) {
        return (ItemViewViewBindingBuilder<I, V>) super.addViewCreator(clazz, viewTypeOfClass, viewCreator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V extends View>
    ItemViewViewBindingBuilder<Void, V> addViewCreator(int viewType, ViewCreator<V> viewCreator) {
        return (ItemViewViewBindingBuilder<Void, V>) super.addViewCreator(viewType, viewCreator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I, V extends View>
    ItemViewViewBindingBuilder<I, V> addViewHolderCreator(Class<I> clazz, ViewHolderCreator<V> viewHolderCreator) {
        return (ItemViewViewBindingBuilder<I, V>) super.addViewHolderCreator(clazz, viewHolderCreator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I, V extends View>
    ItemViewViewBindingBuilder<I, V> addViewHolderCreator(Class<I> clazz, int viewTypeOfClass, ViewHolderCreator<V> viewHolderCreator) {
        return (ItemViewViewBindingBuilder<I, V>) super.addViewHolderCreator(clazz, viewTypeOfClass, viewHolderCreator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V extends View>
    ItemViewViewBindingBuilder<Void, V> addViewHolderCreator(int viewType, ViewHolderCreator<V> viewHolderCreator) {
        return (ItemViewViewBindingBuilder<Void, V>) super.addViewHolderCreator(viewType, viewHolderCreator);
    }
}
