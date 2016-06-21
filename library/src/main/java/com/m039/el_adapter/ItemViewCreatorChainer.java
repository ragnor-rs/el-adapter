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

import android.support.annotation.IdRes;
import android.view.View;

import com.m039.el_adapter.BaseViewAdapter.ViewHolder;
import com.m039.el_adapter.ItemViewAdapter.DefaultItemViewHolderBinder;
import com.m039.el_adapter.ItemViewAdapter.ItemViewBinder;
import com.m039.el_adapter.ItemViewAdapter.ItemViewHolderBinder;
import com.m039.el_adapter.ItemViewAdapter.OnItemViewClickListener;
import com.m039.el_adapter.ItemViewAdapter.OnItemViewHolderClickListener;

import java.util.HashMap;
import java.util.Map;

/**
 * todo custom ViewHolder typization
 *
 * Created by m039 on 6/1/16.
 * Chainer returned by addViewCreator
 */
public class ItemViewCreatorChainer<I, V extends View>
        extends ViewCreatorChainer {

    /**
     * Chainer returned by addViewBinder
     * @param <I> item
     * @param <V> view
     */
    public static class ItemViewBinderChainer<I, V extends View> extends ItemViewCreatorChainer<I, V> {

        private static final int NO_ID = 0;

        private static final class ClickableItemViewHolderChainer<I, V extends View>
                implements ItemViewHolderBinder<I, V> {

            Map<Integer, OnItemViewHolderClickListener> listenersById = new HashMap<>();
            ItemViewHolderBinder<I, V> parentBinder;

            ClickableItemViewHolderChainer(ItemViewHolderBinder<I, V> binder) {
                parentBinder = binder;
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onBindViewHolder(final ViewHolder<V> viewHolder, final I item) {
                if (parentBinder != null) {
                    parentBinder.onBindViewHolder(viewHolder, item);
                }

                V view = viewHolder.itemView;

                for (Map.Entry<Integer, OnItemViewHolderClickListener> entry: listenersById.entrySet()) {
                    int id = entry.getKey();
                    final OnItemViewHolderClickListener viewHolderClickListener = entry.getValue();

                    /**
                     * WARN:
                     *
                     * Performance bottleneck - a lot of calls to new
                     */

                    View.OnClickListener clickListener = new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            viewHolderClickListener.onItemViewHolderClick(viewHolder, item);
                        }

                    };

                    if (id == NO_ID) {
                        view.setOnClickListener(clickListener);
                    } else {
                        view.findViewById(id).setOnClickListener(clickListener);
                    }

                }
            }

            public void addViewHolderClickListener(@IdRes int id, OnItemViewHolderClickListener listener) {
                listenersById.put(id, listener);
            }
        }

        int typeOfBind;

        public ItemViewBinderChainer(BaseViewAdapter adapter, int viewType, int typeOfBind) {
            super(adapter, viewType);
            this.typeOfBind = typeOfBind;
        }

        public ItemViewBinderChainer<I, V> addOnItemViewClickListener(final OnItemViewClickListener<I, V> listener) {
            return addOnItemViewClickListener(NO_ID, listener);
        }

        public ItemViewBinderChainer<I, V> addOnItemViewClickListener(@IdRes int id, final OnItemViewClickListener<I, V> listener) {
            return addOnItemViewHolderClickListener(id, new OnItemViewHolderClickListener<I, V>() {

                @Override
                public void onItemViewHolderClick(ViewHolder<V> viewHolder, I item) {
                    listener.onItemViewClick(viewHolder.itemView, item);
                }

            });
        }

        public ItemViewBinderChainer<I, V> addOnItemViewHolderClickListener(final OnItemViewHolderClickListener<I, V> listener) {
            return addOnItemViewHolderClickListener(NO_ID, listener);
        }

        @SuppressWarnings("unchecked")
        public ItemViewBinderChainer<I, V> addOnItemViewHolderClickListener(@IdRes int id,  final OnItemViewHolderClickListener<I, V> listener) {
            final ItemViewHolderBinder<I, V> binder = (ItemViewHolderBinder<I, V>)
                    adapter.getViewHolderBinder(viewType, typeOfBind);

            ClickableItemViewHolderChainer<I, V> clickableBinder;

            if (binder instanceof ClickableItemViewHolderChainer) {
                clickableBinder = (ClickableItemViewHolderChainer<I, V>) binder;
            } else {
                clickableBinder = new ClickableItemViewHolderChainer<>(binder);
                adapter.addViewHolderBinder(viewType, typeOfBind, clickableBinder);
            }

            clickableBinder.addViewHolderClickListener(id, listener);

            return this;
        }
    }

    final protected ItemViewAdapter adapter; // parametarization

    public ItemViewCreatorChainer(BaseViewAdapter adapter, int viewType) {
        super(adapter, viewType);
        this.adapter = (ItemViewAdapter) adapter;
    }

    public ItemViewBinderChainer<I, V> addViewBinder(
            ItemViewBinder<I, V> itemViewBinder
    ) {
        return addViewHolderBinder(new DefaultItemViewHolderBinder<>(itemViewBinder));
    }

    public ItemViewBinderChainer<I, V> addViewBinder(
            int typeOfBind,
            ItemViewBinder<I, V> itemViewBinder
    ) {
        return addViewHolderBinder(typeOfBind, new DefaultItemViewHolderBinder<>(itemViewBinder));
    }

    public ItemViewBinderChainer<I, V> addViewHolderBinder(
            ItemViewHolderBinder<I, V> itemViewHolderBinder
    ) {
        return addViewHolderBinder(ItemViewAdapter.DEFAULT_TYPE_OF_BIND, itemViewHolderBinder);
    }

    public ItemViewBinderChainer<I, V> addViewHolderBinder(
            int typeOfBind,
            ItemViewHolderBinder<I, V> itemViewHolderBinder
    ) {

        adapter.addViewHolderBinder(viewType, typeOfBind, itemViewHolderBinder);

        return new ItemViewBinderChainer<>(adapter, viewType, typeOfBind);
    }
}
