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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by m039 on 6/1/16.
 */
public abstract class ItemViewAdapter<B extends ItemViewBindingBuilder> extends BaseViewAdapter<B>
        implements ItemViewCreatorDelegate<B> {

    public static final int DEFAULT_TYPE_OF_CLASS = DEFAULT_VIEW_TYPE;
    public static final int DEFAULT_TYPE_OF_BIND = DEFAULT_VIEW_TYPE;

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
    private final Map<Integer, Map<Integer, ItemViewHolderBinder>> mItemViewHolderBindersByViewType = new HashMap<>();

    public ItemViewAdapter(ViewBindingBuilderCreator bindingBuilderCreator) {
        super(bindingBuilderCreator);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        int typeOfBind = getTypeOfBind(position);

        Map<Integer, ItemViewHolderBinder> binders =
                mItemViewHolderBindersByViewType.get(viewType);

        if (binders == null) {
            throw new IllegalStateException(
                    "Can't find binders for " + position + ":" + viewType + ":" + typeOfBind + "."
            );
        }

        ItemViewHolderBinder binder = binders.get(typeOfBind);

        if (binder == null) {
            throw new IllegalStateException(
                    "Can't find binder for " + position + ":" + viewType + ":" + typeOfBind + "."
            );
        }

        binder.onBindViewHolder(holder, getItemAt(position));
    }

    protected abstract Object getItemAt(int position);

    public <I, V extends View>
    void addViewHolderBinder(
            int viewType,
            int typeOfBind,
            @NonNull ItemViewHolderBinder<I, V> itemViewHolderBinder
    ) {
        Map<Integer, ItemViewHolderBinder> binders =
                mItemViewHolderBindersByViewType.get(viewType);

        if (binders == null) {
            binders = new HashMap<>();
            mItemViewHolderBindersByViewType.put(viewType, binders);
        }

        binders.put(typeOfBind, itemViewHolderBinder);
    }

    @SuppressWarnings("unchecked")
    public <I, V extends View>
    ItemViewHolderBinder<I, V> getViewHolderBinder(int viewType, int typeOfBind) {
        Map<Integer, ItemViewHolderBinder> binders =
                mItemViewHolderBindersByViewType.get(viewType);

        if (binders != null) {
            return binders.get(typeOfBind);
        } else {
            return null;
        }
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
     * @param clazz           <code>clazz</code> and <code>typeOfClass</code> will be used
     *                        to create new view type if if doesn't exist yet and for this viewType
     *                        <code>viewCreator</code> will be used
     * @param typeOfClass viewType of <code>clazz</code>. This is additional type to make more
     *                        types for same <code>clazz</code>
     * @param viewCreator     creator of views
     */
    @Override
    public <I, V extends View>
    B addViewCreator(Class<I> clazz, int typeOfClass, ViewCreator<V> viewCreator) {
        return addViewHolderCreator(clazz, typeOfClass, new DefaultViewHolderCreator<>(viewCreator));
    }

    @Override
    public <I, V extends View>
    B addViewHolderCreator(Class<I> clazz, ViewHolderCreator<V> viewHolderCreator) {
        return addViewHolderCreator(clazz, DEFAULT_TYPE_OF_CLASS, viewHolderCreator);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I, V extends View>
    B addViewHolderCreator(Class<I> clazz, int typeOfClass, ViewHolderCreator<V> viewHolderCreator) {
        int viewType = getItemViewType(clazz, typeOfClass);
        return (B) addViewHolderCreator(viewType, viewHolderCreator).setClass(clazz);
    }

    /**
     * Same as {@link #getItemViewType(Class, int)} but with <code>typeOfClasss</code> is 0.
     */
    protected <I> int getItemViewType(Class<I> clazz) {
        return getItemViewType(clazz, DEFAULT_TYPE_OF_CLASS);
    }

    /**
     * This function will create or get viewType based on <code>class</code> and
     * <code>typeOfClass</code>
     */
    protected <I> int getItemViewType(Class<I> clazz, int typeOfClass) {
        return getViewTypeOffset() + mViewTypeHelper.getViewType(clazz, typeOfClass);
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

    protected int getTypeOfClass(int position) {
        return DEFAULT_TYPE_OF_CLASS;
    }

    protected int getTypeOfBind(int position) {
        return DEFAULT_TYPE_OF_BIND;
    }

    /* package */ String findClassName(int viewType) {
        return mViewTypeHelper.findClassName(viewType);
    }

}
