package com.m039.el_adapter;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by m039 on 6/1/16.
 */
public abstract class ItemViewAdapter<B extends ItemViewViewBindingBuilder> extends BaseViewAdapter<B>
        implements ItemViewCreatorDelegate<B> {

    public static final int DEFAULT_VIEW_TYPE = 0;

    /**
     * Same logic as in ItemViewManager.
     * <p>
     * This interface should be used when is needed binding of item to view and nothing else.
     *
     * @param <I> type of the object to bind
     */
    public interface ItemViewBinder<I, V extends View> {

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

    private final ViewTypeHelper mViewTypeHelper = new ViewTypeHelper();
    private final Map<Integer, ItemViewHolderBinder<?, ?>> mItemViewHolderBinderByViewType = new HashMap<>();

    public ItemViewAdapter(Class<B> clazz) {
        super(clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        ItemViewHolderBinder<Object, View> itemViewHolderBinder = (ItemViewHolderBinder<Object, View>)
                mItemViewHolderBinderByViewType.get(viewType);

        if (itemViewHolderBinder == null) {
            throw new IllegalStateException(
                    "Can't find binder for " + position + " position, for " + viewType + " ."
            );
        }

        onBindViewHolder(holder, position, itemViewHolderBinder);
    }

    protected abstract void onBindViewHolder(ViewHolder holder, int position, ItemViewHolderBinder binder);

    public <I, V extends View>
    void addViewHolderBinder(
            int viewType,
            @NonNull ItemViewHolderBinder<I, V> itemViewHolderBinder
    ) {
        mItemViewHolderBinderByViewType.put(viewType, itemViewHolderBinder);
    }

    @SuppressWarnings("unchecked")
    public <I, V extends View>
    ItemViewHolderBinder<I, V> getViewHolderBinder(int viewType) {
        return (ItemViewHolderBinder<I, V>) mItemViewHolderBinderByViewType.get(viewType);
    }

    /**
     * @param clazz       <code>clazz</code> will be used to create new view type if if doesn't
     *                    exist yet, and for this viewType <code>viewCreator</code> will be used
     * @param viewCreator creator of views
     */
    @Override
    public <I, V extends View>
    B addViewCreator(Class<I> clazz, ViewCreator<V> viewCreator) {
        addViewHolderCreator(clazz, new DefaultViewHolderCreator<>(viewCreator));
        return newCommandBuilder(clazz);
    }

    /**
     * @param clazz           <code>clazz</code> and <code>viewTypeOfClass</code> will be used
     *                        to create new view type if if doesn't exist yet and for this viewType
     *                        <code>viewCreator</code> will be used
     * @param viewTypeOfClass viewType of <code>clazz</code>. This is additional type to make more
     *                        types for same <code>clazz</code>
     * @param viewCreator     creator of views
     */
    @Override
    public <I, V extends View>
    B addViewCreator(Class<I> clazz, int viewTypeOfClass, ViewCreator<V> viewCreator) {
        addViewHolderCreator(clazz, viewTypeOfClass, new DefaultViewHolderCreator<>(viewCreator));
        return newCommandBuilder(clazz);
    }

    @Override
    public <I, V extends View>
    B addViewHolderCreator(Class<I> clazz, ViewHolderCreator<V> viewHolderCreator) {
        addViewHolderCreator(clazz, DEFAULT_VIEW_TYPE, viewHolderCreator);
        return newCommandBuilder(clazz);
    }

    @Override
    public <I, V extends View>
    B addViewHolderCreator(Class<I> clazz, int viewTypeOfClass, ViewHolderCreator<V> viewHolderCreator) {
        addViewHolderCreator(getItemViewType(clazz, viewTypeOfClass), viewHolderCreator);
        return newCommandBuilder(clazz);
    }

    @SuppressWarnings("unchecked")
    protected <I> B newCommandBuilder(Class<I> clazz) {
        return (B) super.newCommandBuilder().setClass(clazz);
    }

    /**
     * Same as {@link #getItemViewType(Class, int)} but with <code>viewTypeOfClass</code> is 0.
     */
    protected <T> int getItemViewType(Class<T> clazz) {
        return getItemViewType(clazz, DEFAULT_VIEW_TYPE);
    }

    /**
     * This function will create or get viewType based on <code>class</code> and
     * <code>viewTypeOfClass</code>
     */
    protected <T> int getItemViewType(Class<T> clazz, int viewTypeOfClass) {
        return getViewTypeOffset() + mViewTypeHelper.getViewType(clazz, viewTypeOfClass);
    }

    /**
     * You will need this function if you have custom viewTypes and you need that others values
     * doesn't clash with them.
     *
     * @return offset from which functions like {@link #getItemViewType(Class, int)} or
     * {@link #getItemViewType(Class)} will return the value.
     */
    protected int getViewTypeOffset() {
        return 0;
    }

}
