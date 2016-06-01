package com.m039.el_adapter;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by m039 on 6/1/16.
 */
public abstract class ItemViewAdapter<B extends ItemViewBindingBuilder> extends BaseViewAdapter<B>
        implements ItemViewCreatorDelegate<B> {

    public static final int DEFAULT_VIEW_TYPE_OF_CLASS = DEFAULT_VIEW_TYPE;

    /**
     *
     * Bind an item to a view
     *
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

    public interface OnItemViewClickListener<I, V extends View> {

        void onItemViewClick(V view, I item);

    }

    public interface OnItemViewHolderClickListener<I, V extends View> {

        void onItemViewHolderClick(ViewHolder<V> viewHolder, I item);

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
        return addViewHolderCreator(clazz, new DefaultViewHolderCreator<>(viewCreator));
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
        return addViewHolderCreator(clazz, viewTypeOfClass, new DefaultViewHolderCreator<>(viewCreator));
    }

    @Override
    public <I, V extends View>
    B addViewHolderCreator(Class<I> clazz, ViewHolderCreator<V> viewHolderCreator) {
        return addViewHolderCreator(clazz, DEFAULT_VIEW_TYPE_OF_CLASS, viewHolderCreator);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I, V extends View>
    B addViewHolderCreator(Class<I> clazz, int viewTypeOfClass, ViewHolderCreator<V> viewHolderCreator) {
        int viewType = getItemViewType(clazz, viewTypeOfClass);
        return (B) addViewHolderCreator(viewType, viewHolderCreator).setClass(clazz);
    }

    /**
     * Same as {@link #getItemViewType(Class, int)} but with <code>viewTypeOfClass</code> is 0.
     */
    protected <T> int getItemViewType(Class<T> clazz) {
        return getItemViewType(clazz, DEFAULT_VIEW_TYPE_OF_CLASS);
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
