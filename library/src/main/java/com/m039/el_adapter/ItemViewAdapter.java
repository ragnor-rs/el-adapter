package com.m039.el_adapter;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * todo javadoc
 * Created by m039 on 3/21/16.
 */
public class ItemViewAdapter extends BaseViewAdapter {

    private final ArrayList<Object> mItems = new ArrayList<>();
    private final Map<Integer, ItemViewBinder<?, ?>> mItemViewBinderByViewType = new HashMap<>();

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

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Object object = mItems.get(position);

        ItemViewBinder<Object, View> itemViewBinder = (ItemViewBinder<Object, View>) mItemViewBinderByViewType
                .get(getItemViewType(object.getClass(), getViewTypeOfClass(position)));

        itemViewBinder.onBindView(holder.itemView, object);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    protected <T, V extends View> void addCreatorAndBinder(
            Class<T> clazz,
            ViewCreator<V> viewCreator,
            ItemViewBinder<T, V> itemViewBinder
    ) {
        addViewCreator(clazz, viewCreator);
        addViewBinder(clazz, itemViewBinder);
    }

    protected <T, V extends View> void addCreatorAndBinder(
            Class<T> clazz,
            int viewTypeOfClass,
            ViewCreator<V> viewCreator,
            ItemViewBinder<T, V> itemViewBinder
    ) {
        addViewCreator(getItemViewType(clazz, viewTypeOfClass), viewCreator);
        mItemViewBinderByViewType.put(getItemViewType(clazz, viewTypeOfClass), itemViewBinder);
    }

    protected <T, V extends View> void addViewBinder(Class<T> clazz, ItemViewBinder<T, V> itemViewBinder) {
        mItemViewBinderByViewType.put(getItemViewType(clazz), itemViewBinder);
    }

    protected <T, V extends View> void addViewBinder(Class<T> clazz, int viewTypeOfClass, ItemViewBinder<T, V> itemViewBinder) {
        mItemViewBinderByViewType.put(getItemViewType(clazz, viewTypeOfClass), itemViewBinder);
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

    public <I> void addItems(int index, @NonNull List<I> items) {
        if (!items.isEmpty()) {
            mItems.addAll(index, items);
        }
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

}
