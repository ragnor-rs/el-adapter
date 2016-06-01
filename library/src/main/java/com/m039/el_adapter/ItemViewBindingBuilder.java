package com.m039.el_adapter;

import android.view.View;

import com.m039.el_adapter.ItemViewAdapter.DefaultItemViewHolderBinder;
import com.m039.el_adapter.ItemViewAdapter.ItemViewBinder;
import com.m039.el_adapter.ItemViewAdapter.ItemViewHolderBinder;

import static com.m039.el_adapter.BaseViewAdapter.DEFAULT_VIEW_TYPE;

/**
 * Created by m039 on 6/1/16.
 */
/* package */ class ItemViewBindingBuilder<I, V extends View>
        extends ViewBindingBuilder<V> {

    protected Class<I> clazz;
    protected Integer bindedViewType;

    public ItemViewBindingBuilder(BaseViewAdapter adapter, int viewType) {
        super(adapter, viewType);
    }

    public ItemViewBindingBuilder<I, V> setClass(Class<I> clazz) {
        this.clazz = clazz;
        return this;
    }

    protected ItemViewAdapter getAdapter() {
        return (ItemViewAdapter) adapter;
    }

    public ItemViewBindingBuilder<I, V> addViewBinder(ItemViewBinder<I, V> itemViewBinder) {
        return addViewHolderBinder(new DefaultItemViewHolderBinder<>(itemViewBinder));
    }

    public ItemViewBindingBuilder<I, V> addViewBinder(int viewTypeOfClass, ItemViewBinder<I, V> itemViewBinder) {
        return addViewHolderBinder(viewTypeOfClass, new DefaultItemViewHolderBinder<>(itemViewBinder));
    }

    public ItemViewBindingBuilder<I, V> addViewHolderBinder(
            ItemViewHolderBinder<I, V> itemViewHolderBinder
    ) {
        return addViewHolderBinder(DEFAULT_VIEW_TYPE, itemViewHolderBinder);
    }

    public ItemViewBindingBuilder<I, V> addViewHolderBinder(
            int viewTypeOfClass,
            ItemViewHolderBinder<I, V> itemViewHolderBinder
    ) {
        if (clazz == null) {
            throw new IllegalArgumentException(ListItemAdapter.class.getSimpleName() + " doesn't support binding for no class");
        }

        ItemViewAdapter adapter = getAdapter();

        bindedViewType = adapter.getItemViewType(clazz, viewTypeOfClass);

        adapter.addViewHolderBinder(bindedViewType, itemViewHolderBinder);

        return this;
    }

    // TODO: 6/1/16 clicks

}
