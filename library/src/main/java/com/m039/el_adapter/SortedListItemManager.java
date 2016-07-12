package com.m039.el_adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.util.SortedListAdapterCallback;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SortedListItemManager implements ItemManager {

    private final SortedList<Object> mItems;

    public SortedListItemManager(SortedListAdapterCallback<Object> adapterCallback) {
        mItems = new SortedList<>(Object.class, adapterCallback);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void removeItem(int position) {
        mItems.removeItemAt(position);
    }

    public void removeAllItems() {
        mItems.clear();
    }

    public void removeItemsRange(int positionStart, int itemCount) {
        if (itemCount <= 0) {
            return;
        }

        mItems.beginBatchedUpdates();
        for (int i = positionStart; i < positionStart + itemCount; i++) {
            mItems.removeItemAt(i);
        }
        mItems.endBatchedUpdates();
    }

    public <T> void addItem(T item) {
        mItems.add(item);
    }

    @Override
    public <T> void addItem(int index, T item) {

    }

    public <T> void addItems(@NonNull T items[]) {
        if (items.length > 0) {
            mItems.addAll(Arrays.asList(items));
        }
    }

    @Override
    public <I> void addItems(int index, @NonNull List<I> items) {
        throw new UnsupportedOperationException("SortedList canno add item by index, since it will be immediately sorted to another position");
    }

    public <T> void addItems(Collection<T> items) {
        if (items.size() > 0) {
            mItems.beginBatchedUpdates();
            for (Object item : items) {
                mItems.add(item);
            }
            mItems.endBatchedUpdates();
        }
    }

    public List<Object> getItems() {
        throw new UnsupportedOperationException("SortedList canno add item by index, since it will be immediately sorted to another position");
    }

    public SortedList<Object> getSortedList(){
        return mItems;
    }

    public Object getItemAt(int position) {
        return mItems.get(position);
    }
}
