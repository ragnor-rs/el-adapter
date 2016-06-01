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
 * Created by m039 on 6/1/16.
 */
/* package */ class ItemViewBindingBuilder<I, V extends View>
        extends ViewBindingBuilder<V> {

    protected static class ClickableBindingBuilder<I, V extends View> extends ItemViewBindingBuilder<I, V> {

        private static class ClickableViewHolderCreator<I, V extends View> implements ViewHolderCreator<V> {

            Map<Integer, OnItemViewHolderClickListener> listenersByBindedViewType = new HashMap<>();
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
                        int viewType = adapter.getItemViewType(position);

                        OnItemViewHolderClickListener<I, V> listener = listenersByBindedViewType.get(viewType);
                        if (listener != null) {
                            I item = (I) adapter.getItemAt(position);
                            listener.onItemViewHolderClick(viewHolder, item);
                        }
                    }

                });

                return viewHolder;
            }

        }

        public ClickableBindingBuilder(BaseViewAdapter adapter, Class<I> clazz, int viewType) {
            super(adapter, viewType);
            this.clazz = clazz;
        }

        public ClickableBindingBuilder<I, V> addOnItemViewClickListener(final OnItemViewClickListener<I, V> listener) {
            return addOnItemViewHolderClickListener(new OnItemViewHolderClickListener<I, V>() {

                @Override
                public void onItemViewHolderClick(ViewHolder<V> viewHolder, I item) {
                    listener.onItemViewClick(viewHolder.itemView, item);
                }

            });
        }

        @SuppressWarnings("unchecked")
        public ClickableBindingBuilder<I, V> addOnItemViewHolderClickListener(final OnItemViewHolderClickListener<I, V> listener) {
            final ViewHolderCreator<V> viewHolderCreator = (ViewHolderCreator<V>)
                    adapter.getViewHolderCreator(viewType);

            ClickableViewHolderCreator<I, V> clickableViewHolderCreator;

            if (viewHolderCreator instanceof ClickableViewHolderCreator) {
                clickableViewHolderCreator = (ClickableViewHolderCreator<I, V>) viewHolderCreator;
            } else {
                clickableViewHolderCreator = new ClickableViewHolderCreator<>(adapter, viewHolderCreator);
                adapter.addViewHolderCreator(viewType, clickableViewHolderCreator);
            }

            clickableViewHolderCreator.listenersByBindedViewType.put(viewType, listener);

            return this;
        }

    }

    final protected ItemViewAdapter adapter; // parametarization
    protected Class<I> clazz;

    public ItemViewBindingBuilder(BaseViewAdapter adapter, int viewType) {
        super(adapter, viewType);
        this.adapter = (ItemViewAdapter) adapter;
    }

    public ItemViewBindingBuilder<I, V> setClass(Class<I> clazz) {
        this.clazz = clazz;
        return this;
    }

    public ClickableBindingBuilder<I, V> addViewBinder(
            ItemViewBinder<I, V> itemViewBinder
    ) {
        return addViewHolderBinder(new DefaultItemViewHolderBinder<>(itemViewBinder));
    }

    public ClickableBindingBuilder<I, V> addViewHolderBinder(
            ItemViewHolderBinder<I, V> itemViewHolderBinder
    ) {
        if (clazz == null) {
            throw new IllegalArgumentException(ListItemAdapter.class.getSimpleName() + " doesn't support binding for no class");
        }

        adapter.addViewHolderBinder(viewType, itemViewHolderBinder);

        return new ClickableBindingBuilder<I, V>(adapter, clazz, viewType);
    }
}
