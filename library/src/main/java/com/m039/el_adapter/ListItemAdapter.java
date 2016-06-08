/**
 * Copyright (C) 2016 Dmitry Mozgin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.m039.el_adapter;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * todo javadoc
 * Created by m039 on 3/21/16.
 */
public class ListItemAdapter extends ItemViewAdapter<ItemViewBindingBuilder> {

    private final List<Object> mItems = new ArrayList<>();

    public ListItemAdapter() {
        super(new ViewBindingBuilderCreator() {
            @Override
            public ViewBindingBuilder newBindingBuilder(BaseViewAdapter adapter, int viewType) {
                return new ItemViewBindingBuilder(adapter, viewType);
            }
        });
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

    public <I> void addItems(@NonNull Collection<I> items) {
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

    @Override
    public Object getItemAt(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(mItems.get(position).getClass(), getTypeOfClass(position));
    }

    //
    // Parametrization
    //

    @Override
    @SuppressWarnings("unchecked")
    public <I, V extends View>
    ItemViewBindingBuilder<I, V> addViewCreator(Class<I> clazz, ViewCreator<V> viewCreator) {
        return (ItemViewBindingBuilder<I, V>) super.addViewCreator(clazz, viewCreator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I, V extends View>
    ItemViewBindingBuilder<I, V> addViewCreator(Class<I> clazz, int typeOfClass, ViewCreator<V> viewCreator) {
        return (ItemViewBindingBuilder<I, V>) super.addViewCreator(clazz, typeOfClass, viewCreator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V extends View>
    ItemViewBindingBuilder<Void, V> addViewCreator(int viewType, ViewCreator<V> viewCreator) {
        return (ItemViewBindingBuilder<Void, V>) super.addViewCreator(viewType, viewCreator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I, V extends View>
    ItemViewBindingBuilder<I, V> addViewHolderCreator(Class<I> clazz, ViewHolderCreator<V> viewHolderCreator) {
        return (ItemViewBindingBuilder<I, V>) super.addViewHolderCreator(clazz, viewHolderCreator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I, V extends View>
    ItemViewBindingBuilder<I, V> addViewHolderCreator(Class<I> clazz, int typeOfClass, ViewHolderCreator<V> viewHolderCreator) {
        return (ItemViewBindingBuilder<I, V>) super.addViewHolderCreator(clazz, typeOfClass, viewHolderCreator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V extends View>
    ItemViewBindingBuilder<Void, V> addViewHolderCreator(int viewType, ViewHolderCreator<V> viewHolderCreator) {
        return (ItemViewBindingBuilder<Void, V>) super.addViewHolderCreator(viewType, viewHolderCreator);
    }
}
