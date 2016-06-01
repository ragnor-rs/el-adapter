package com.m039.el_adapter;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * todo javadoc
 * Created by m039 on 3/21/16.
 */
public class ItemViewAdapter extends BaseViewAdapter<ItemViewCommandBuilder> {

    private final ArrayList<Object> mItems = new ArrayList<>();
    private final Map<Integer, ItemViewHolderBinder<?, ?>> mItemViewHolderBinderByViewType = new HashMap<>();

    /**
     * Same logic as in ItemViewManager.
     * <p>
     * This interface should be used when is needed binding of item to view and nothing else.
     *
     * @param <I> type of the object to bind
     */
    public interface ItemViewBinder<I, V extends View>  {

        /**
         * @param view view to bind to
         * @param item item used for binding
         */
        void onBindView(V view, I item);

    }

    public interface ItemViewHolderBinder<I, V extends View> {

        void onBindViewHolder(ViewHolder<V> viewHolder, I item);

    }

    protected static class DefaultItemViewHolderBinder<I, V extends View>
            implements ItemViewHolderBinder<I, V> {

        @NonNull
        private final ItemViewBinder<I, V> mItemViewBinder;

        public DefaultItemViewHolderBinder(@NonNull ItemViewBinder<I, V> itemViewBinder) {
            mItemViewBinder = itemViewBinder;
        }

        @NonNull
        public ItemViewBinder<I, V> getItemViewBinder() {
            return mItemViewBinder;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onBindViewHolder(ViewHolder<V> viewHolder, I item) {
            mItemViewBinder.onBindView((V) viewHolder.itemView, item);
        }

    }

    public ItemViewAdapter() {
        super(ItemViewCommandBuilder.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Object object = mItems.get(position);

        Class<?> clazz = object.getClass();
        int viewTypeOfClass = getViewTypeOfClass(position);
        int viewType = getItemViewType(clazz, viewTypeOfClass);

        ItemViewHolderBinder<Object, View> itemViewHolderBinder = (ItemViewHolderBinder<Object, View>)
                mItemViewHolderBinderByViewType.get(viewType);

        if (itemViewHolderBinder == null) {
            throw new IllegalStateException("Can't find binder for " + position + " position. " +
                    "Tried for " + clazz.getSimpleName() + " with viewTypeOfClass = " + viewTypeOfClass +
                    " and resulting viewType = " + viewType
            );
        }

        itemViewHolderBinder.onBindViewHolder(holder, object);
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

    public <I, V extends View>
    void addViewHolderBinder(
            Class<I> clazz,
            int viewTypeOfClass,
            ItemViewHolderBinder<I, V> itemViewHolderBinder
    ) {
        int viewType = getItemViewType(clazz, viewTypeOfClass);
        mItemViewHolderBinderByViewType.put(viewType, itemViewHolderBinder);
    }

    public ArrayList<Object> getItems() {
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
    ItemViewCommandBuilder<I, V> addViewCreator(Class<I> clazz, ViewCreator<V> viewCreator) {
        return (ItemViewCommandBuilder<I, V>) super.addViewCreator(clazz, viewCreator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I, V extends View>
    ItemViewCommandBuilder<I, V> addViewCreator(Class<I> clazz, int viewTypeOfClass, ViewCreator<V> viewCreator) {
        return (ItemViewCommandBuilder<I, V>) super.addViewCreator(clazz, viewTypeOfClass, viewCreator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I, V extends View>
    ItemViewCommandBuilder<I, V> addViewCreator(int viewType, ViewCreator<V> viewCreator) {
        return (ItemViewCommandBuilder<I, V>) super.addViewCreator(viewType, viewCreator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I, V extends View>
    ItemViewCommandBuilder<I, V> addViewHolderCreator(Class<I> clazz, ViewHolderCreator<V> viewHolderCreator) {
        return (ItemViewCommandBuilder<I, V>) super.addViewHolderCreator(clazz, viewHolderCreator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I, V extends View>
    ItemViewCommandBuilder<I, V> addViewHolderCreator(Class<I> clazz, int viewTypeOfClass, ViewHolderCreator<V> viewHolderCreator) {
        return (ItemViewCommandBuilder<I, V>) super.addViewHolderCreator(clazz, viewTypeOfClass, viewHolderCreator);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I, V extends View>
    ItemViewCommandBuilder<I, V> addViewHolderCreator(int viewType, ViewHolderCreator<V> viewHolderCreator) {
        return (ItemViewCommandBuilder<I, V>) super.addViewHolderCreator(viewType, viewHolderCreator);
    }
}
