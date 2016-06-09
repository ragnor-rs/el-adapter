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

import android.view.View;
import android.view.ViewGroup;

import com.m039.el_adapter.BaseViewAdapter.ViewHolder;
import com.m039.el_adapter.BaseViewAdapter.ViewHolderCreator;
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
        extends ViewCreatorChainer<V> {

    /**
     * Chainer returned by addViewBinder
     * @param <I> item
     * @param <V> view
     */
    public static class ItemViewBinderChainer<I, V extends View> extends ItemViewCreatorChainer<I, V> {

        private static class ClickableViewHolderCreator<I, V extends View> implements ViewHolderCreator<V> {

            Map<Integer, OnItemViewHolderClickListener> listenersByTypeOfBind = new HashMap<>();
            final ViewHolderCreator<V> viewHolderCreator;
            final ItemViewAdapter adapter;

            public ClickableViewHolderCreator(ItemViewAdapter itemViewAdapter, ViewHolderCreator<V> viewHolderCreator) {
                this.viewHolderCreator = viewHolderCreator;
                this.adapter = itemViewAdapter;
            }

            @SuppressWarnings("unchecked")
            @Override
            public ViewHolder<V> onCreateViewHolder(ViewGroup parent) {
                final ViewHolder<V> viewHolder = viewHolderCreator.onCreateViewHolder(parent);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        int position = viewHolder.getAdapterPosition();
                        int typeOfBind = adapter.getTypeOfBind(position);

                        OnItemViewHolderClickListener<I, V> listener = listenersByTypeOfBind.get(typeOfBind);
                        if (listener != null) {
                            I item = (I) adapter.getItemAt(position);
                            listener.onItemViewHolderClick(viewHolder, item);
                        }
                    }

                });

                return viewHolder;
            }

        }

        int typeOfBind;

        public ItemViewBinderChainer(BaseViewAdapter adapter, int viewType, int typeOfBind) {
            super(adapter, viewType);
            this.typeOfBind = typeOfBind;
        }

        public ItemViewBinderChainer<I, V> addOnItemViewClickListener(final OnItemViewClickListener<I, V> listener) {
            return addOnItemViewHolderClickListener(new OnItemViewHolderClickListener<I, V>() {

                @Override
                public void onItemViewHolderClick(ViewHolder<V> viewHolder, I item) {
                    listener.onItemViewClick(viewHolder.itemView, item);
                }

            });
        }

        @SuppressWarnings("unchecked")
        public ItemViewBinderChainer<I, V> addOnItemViewHolderClickListener(final OnItemViewHolderClickListener<I, V> listener) {
            final ViewHolderCreator<V> viewHolderCreator = (ViewHolderCreator<V>)
                    adapter.getViewHolderCreator(viewType);

            ClickableViewHolderCreator<I, V> clickableViewHolderCreator;

            if (viewHolderCreator instanceof ClickableViewHolderCreator) {
                clickableViewHolderCreator = (ClickableViewHolderCreator<I, V>) viewHolderCreator;
            } else {
                clickableViewHolderCreator = new ClickableViewHolderCreator<>(adapter, viewHolderCreator);
                adapter.addViewHolderCreator(viewType, clickableViewHolderCreator);
            }

            clickableViewHolderCreator.listenersByTypeOfBind.put(typeOfBind, listener);

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
