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

import com.m039.el_adapter.ItemViewAdapter.ItemViewBuilder.ItemViewBinderChainer;
import com.m039.el_adapter.ItemViewAdapter.ItemViewBuilder.ItemViewHolderBinderChainer;
import com.m039.el_adapter.denis.BaseViewAdapter;
import com.m039.el_adapter.denis.BaseViewAdapter.BaseViewBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by m039 on 6/1/16.
 */
public abstract class ItemViewAdapter extends BaseViewAdapter<ItemViewAdapter.ItemViewBuilder> {

    public static final int DEFAULT_TYPE_OF_CLASS = DEFAULT_VIEW_TYPE;

    /**
     * Bind an item to a view
     */
    public interface ItemViewBinder<I, V extends View> {

        /**
         * @param view view to bind to
         * @param item item used for binding
         */
        void onBindView(V view, I item);

    }

    public interface ItemViewHolderBinder<I, V extends View> {

        void onBindViewHolder(BaseViewHolder<V> viewHolder, I item);

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
        public void onBindViewHolder(BaseViewHolder<V> viewHolder, I item) {
            mItemViewBinder.onBindView((V) viewHolder.itemView, item);
        }

    }

    private final ViewTypeHelper mViewTypeHelper = new ViewTypeHelper();
    private final Map<Integer, Map<Integer, ItemViewHolderBinder>> mItemViewHolderBindersByViewType = new HashMap<>();

    public ItemViewAdapter() {
        super();
    }


    //region RecyclerView#Adapter

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
//        int viewType = getItemViewType(position);
//
//        Map<Integer, ItemViewHolderBinder> binders = mItemViewHolderBindersByViewType.get(viewType);
//
//        if (binders == null) {
//            throw new IllegalStateException("Can't find binders for " + position + ":" + viewType + ".");
//        }
//
//        ItemViewHolderBinder binder = binders.get(typeOfBind);
//
//        if (binder == null) {
//            throw new IllegalStateException(
//                    "Can't find binder for " + position + ":" + viewType + ":" + typeOfBind + "."
//            );
//        }
//
//        binder.onBindViewHolder(holder, getItemAt(position));


        ItemViewBinder<Object, View> viewBinder = getItemViewBinder(getItemViewType(position));

        if (viewBinder != null) {
            viewBinder.onBindView(holder.itemView, getItemAt(position));
        } else {
            // do nothing, don't bind a thing
        }


    }

    //endregion


    protected abstract Object getItemAt(int position);

//    public <I, V extends View>
//    void addViewHolderBinder(
//            int viewType,
//            int typeOfBind,
//            @NonNull ItemViewHolderBinder<I, V> itemViewHolderBinder
//    ) {
//        Map<Integer, ItemViewHolderBinder> binders =
//                mItemViewHolderBindersByViewType.get(viewType);
//
//        if (binders == null) {
//            binders = new HashMap<>();
//            mItemViewHolderBindersByViewType.put(viewType, binders);
//        }
//
//        binders.put(typeOfBind, itemViewHolderBinder);
//    }
//
//    @SuppressWarnings("unchecked")
//    public <I, V extends View>
//    ItemViewHolderBinder<I, V> getViewHolderBinder(int viewType, int typeOfBind) {
//        Map<Integer, ItemViewHolderBinder> binders =
//                mItemViewHolderBindersByViewType.get(viewType);
//
//        if (binders != null) {
//            return binders.get(typeOfBind);
//        } else {
//            return null;
//        }
//    }

    /**
     * @param clazz       <code>clazz</code> will be used to create new view type if if doesn't
     *                    exist yet, and for this viewType <code>viewCreator</code> will be used
     * @param viewCreator creator of views
     */
    public <I, V extends View>
    ItemViewBinderChainer<I, V> addViewCreator(Class<I> clazz, ViewCreator<V> viewCreator) {
        return addViewCreator(clazz, DEFAULT_TYPE_OF_CLASS, viewCreator);
    }

    /**
     * @param clazz       <code>clazz</code> and <code>typeOfClass</code> will be used
     *                    to create new view type if if doesn't exist yet and for this viewType
     *                    <code>viewCreator</code> will be used
     * @param typeOfClass viewType of <code>clazz</code>. This is additional type to make more
     *                    types for same <code>clazz</code>
     * @param viewCreator creator of views
     */
    public <I, V extends View>
    ItemViewBinderChainer<I, V> addViewCreator(Class<I> clazz, int typeOfClass, ViewCreator<V> viewCreator) {
        int viewType = getItemViewType(clazz, typeOfClass);
        addViewCreator(viewType, viewCreator);
        ItemViewBuilder builder = getBuilder(viewType);
        return (ItemViewBinderChainer<I, V>) builder.getItemViewBinderChainer();
    }

    public <I, V extends View>
    ItemViewHolderBinderChainer<I, V> addViewHolderCreator(Class<I> clazz, ViewHolderCreator<BaseViewHolder<V>> viewHolderCreator) {
        return addViewHolderCreator(clazz, DEFAULT_TYPE_OF_CLASS, viewHolderCreator);
    }

    @SuppressWarnings("unchecked")
    public <I, V extends View>
    ItemViewHolderBinderChainer<I, V> addViewHolderCreator(Class<I> clazz, int typeOfClass, ViewHolderCreator<BaseViewHolder<V>> viewHolderCreator) {
        int viewType = getItemViewType(clazz, typeOfClass);
        addViewHolderCreator(viewType, viewHolderCreator);
        ItemViewBuilder builder = getBuilder(viewType);
        return (ItemViewHolderBinderChainer<I, V>) builder.getItemViewHolderBinderChainer();
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

    /* package */ String findClassName(int viewType) {
        return mViewTypeHelper.findClassName(viewType);
    }


    protected <I, V extends View> ItemViewBinder<I, V> getItemViewBinder(int viewType) {
        return (ItemViewBinder<I, V>) getBuilder(viewType).getItemViewBinder();
    }

    public static class ItemViewBuilder<I, V extends View> extends BaseViewAdapter.BaseViewBuilder {


//        public static class BaseViewBuilder<V extends View, VH extends BaseViewHolder<V>> extends BaseViewHolderBuilder<V, VH> {

        private ItemViewBinder<I, V> itemViewBinder;
        private ItemViewHolderBinder<I, V> itemViewHolderBinder;
        private final ItemViewBinderChainer<I, V> itemViewBinderChainer = new ItemViewBinderChainer<>(this);
        private final ItemViewHolderBinderChainer<I, V> itemViewHolderBinderChainer = new ItemViewHolderBinderChainer<>(this);

        public ItemViewBuilder(ViewHolderCreator<?> creator) {
            super(creator);
        }

        public ItemViewBinderChainer<I, V> getItemViewBinderChainer() {
            return itemViewBinderChainer;
        }

        public ItemViewHolderBinderChainer<I, V> getItemViewHolderBinderChainer() {
            return itemViewHolderBinderChainer;
        }

        public ItemViewBinder<I, V> getItemViewBinder() {
            return itemViewBinder;
        }


        public static class ItemViewBinderChainer<I, V extends View> {

            private final ItemViewBuilder<I, V> elBuilder;

            public ItemViewBinderChainer(ItemViewBuilder<I, V> elBuilder) {
                this.elBuilder = elBuilder;
            }

            public void addItemViewBinder(ItemViewBinder<I, V> viewBinder) {
                elBuilder.itemViewBinder = viewBinder;
            }
        }



        public static class ItemViewHolderBinderChainer<I, V extends View> {

            private final ItemViewBuilder<I, V> elBuilder;

            public ItemViewHolderBinderChainer(ItemViewBuilder<I, V> elBuilder) {
                this.elBuilder = elBuilder;
            }

            public void addViewHolderBinder(ItemViewHolderBinder<I, V> viewHolderBinder) {
                elBuilder.itemViewHolderBinder = viewHolderBinder;
            }
        }

//        }


    }


}
