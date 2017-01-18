/*
 * Copyright (C) 2016 Dmitry Mozgin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.m039.el_adapter;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.m039.el_adapter.ItemViewAdapter.ItemViewHelper.BindClickViewClickChainer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by m039 on 6/1/16.
 */
public abstract class ItemViewAdapter<B extends ItemViewAdapter.ItemViewHelper> extends BaseViewAdapter<B> {

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

    private final ViewTypeHelper mViewTypeHelper = new ViewTypeHelper();

    public ItemViewAdapter() {
        super();
    }


    //region RecyclerView#Adapter

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final BaseViewHolder viewHolder;

        try {

            viewHolder = super.onCreateViewHolder(parent, viewType);

        } catch (UnknownViewType e) {
            String className = mViewTypeHelper.findClassName(viewType);

            throw new UnknownViewType("Can't create view of type " +
                    viewType + (className != null ? " or '" + className : "'") + "." +
                    " You should register " +
                    ViewCreator.class.getSimpleName() +
                    " or " +
                    ViewHolderCreator.class.getSimpleName() +
                    " for that type."
            );
        }

        final View view = viewHolder.itemView;

        //item view click listener
        for (Object entryO : getBuilder(viewType).getItemViewClickListenersById().entrySet()) {
            Map.Entry<Integer, ItemViewHelper.OnItemViewClickListener> entry = (Map.Entry<Integer, ItemViewHelper.OnItemViewClickListener>) entryO; //todo wtf
            int id = entry.getKey();
            final ItemViewHelper.OnItemViewClickListener viewClickListener = entry.getValue();

            /**
             * WARN:
             *
             * Performance bottleneck - a lot of calls to new
             */

            View.OnClickListener clickListener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int adapterPosition = viewHolder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        viewClickListener.onItemViewClick(view, getItemAt(adapterPosition));
                    }
                }

            };

            if (id == BaseViewHelper.NO_ID_CLICK_LISTENER) {
                view.setOnClickListener(clickListener);
            } else {
                View v = view.findViewById(id);
                if (v != null) {
                    v.setOnClickListener(clickListener);
                }
            }
        }

        //todo DRY
        //item view holder click listener
        for (Object entryO : getBuilder(viewType).getItemViewHolderClickListenersById().entrySet()) {
            Map.Entry<Integer, ItemViewHelper.OnItemViewHolderClickListener> entry = (Map.Entry<Integer, ItemViewHelper.OnItemViewHolderClickListener>) entryO; //todo wtf
            int id = entry.getKey();
            final ItemViewHelper.OnItemViewHolderClickListener viewClickListener = entry.getValue();

            /**
             * WARN:
             *
             * Performance bottleneck - a lot of calls to new
             */

            View.OnClickListener clickListener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    viewClickListener.onItemViewClick(viewHolder, getItemAt(viewHolder.getAdapterPosition()));
                }

            };

            if (id == BaseViewHelper.NO_ID_CLICK_LISTENER) {
                view.setOnClickListener(clickListener);
            } else {
                view.findViewById(id).setOnClickListener(clickListener);
            }
        }

        return viewHolder;


    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        ItemViewHolderBinder<Object, View> viewBinder = getItemViewHolderBinder(getItemViewType(position));

        if (viewBinder != null) {
            viewBinder.onBindViewHolder(holder, getItemAt(position));
        } else {
            // do nothing, don't bind a thing
        }


    }

    //endregion


    protected abstract Object getItemAt(int position);


    /**
     * @param clazz       <code>clazz</code> will be used to create new view type if if doesn't
     *                    exist yet, and for this viewType <code>viewCreator</code> will be used
     * @param viewCreator creator of views
     */
    public <I, V extends View>
    BindClickViewClickChainer<I, V> addViewCreator(Class<I> clazz, ViewCreator<V> viewCreator) {
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
    BindClickViewClickChainer<I, V> addViewCreator(Class<I> clazz, int typeOfClass, ViewCreator<V> viewCreator) {
        int viewType = getItemViewType(clazz, typeOfClass);
        addViewCreator(viewType, viewCreator);
        ItemViewHelper builder = getBuilder(viewType);
        return (BindClickViewClickChainer<I, V>) builder.getItemViewChainer();
    }

    public <I, V extends View>
    BindClickViewClickChainer<I, V> addViewHolderCreator(Class<I> clazz, ViewHolderCreator<BaseViewHolder<V>> viewHolderCreator) {
        return addViewHolderCreator(clazz, DEFAULT_TYPE_OF_CLASS, viewHolderCreator);
    }

    @SuppressWarnings("unchecked")
    public <I, V extends View>
    BindClickViewClickChainer<I, V> addViewHolderCreator(Class<I> clazz, int typeOfClass, ViewHolderCreator<BaseViewHolder<V>> viewHolderCreator) {
        int viewType = getItemViewType(clazz, typeOfClass);
        addViewHolderCreator(viewType, viewHolderCreator);
        ItemViewHelper builder = getBuilder(viewType);
        return (BindClickViewClickChainer<I, V>) builder.getItemViewChainer();
    }


    //region item view types

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

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(getItemAt(position).getClass(), getTypeOfClass(position));
    }

    //endregion

    protected <I, V extends View> ItemViewHolderBinder<I, V> getItemViewHolderBinder(int viewType) {
        return (ItemViewHolderBinder<I, V>) getBuilder(viewType).getItemViewHolderBinder();
    }


    public static class ItemViewHelper<I, V extends View> extends BaseViewAdapter.BaseViewHelper<V> {

        private ItemViewHolderBinder<I, V> itemViewHolderBinder;
        private Map<Integer, OnItemViewClickListener<I, V>> itemViewClickListenersById = new HashMap<>();
        private Map<Integer, OnItemViewHolderClickListener<I, V>> itemViewHolderClickListenersById = new HashMap<>();

        public ItemViewHelper(ViewHolderCreator<BaseViewHolder<V>> creator) {
            super(creator);
        }

        public BindClickViewClickChainer<I, V> getItemViewChainer() {
            return new BindClickViewClickChainer<>(this);
        }

        public ItemViewHolderBinder<I, V> getItemViewHolderBinder() {
            return itemViewHolderBinder;
        }

        public void setViewBinder(ItemViewHolderBinder<I, V> itemViewHolderBinder) {
            this.itemViewHolderBinder = itemViewHolderBinder;
        }


        private void addItemViewHolderClickListener(@IdRes int resId, OnItemViewHolderClickListener<I, V> onItemViewHolderClickListener) {
            itemViewHolderClickListenersById.put(resId, onItemViewHolderClickListener);
        }


        private void addItemViewClickListener(@IdRes int resId, OnItemViewClickListener<I, V> onItemViewClickListener) {
            itemViewClickListenersById.put(resId, onItemViewClickListener);
        }

        public Map<Integer, OnItemViewClickListener<I, V>> getItemViewClickListenersById() {
            return itemViewClickListenersById;
        }

        public Map<Integer, OnItemViewHolderClickListener<I, V>> getItemViewHolderClickListenersById() {
            return itemViewHolderClickListenersById;
        }

        //region Chainers

        /**
         * this Chainer can chain addOnViewHolderClickListener, addOnViewHolderClickListener, addViewHolderBinder
         *
         * @param <V>
         */
        public static class BindClickViewClickChainer<I, V extends View> extends BindViewClickChainer<I, V> {

            public BindClickViewClickChainer(ItemViewHelper<I, V> builder) {
                super(builder);
            }

            public BindViewClickChainer<I, V> addOnItemViewHolderClickListener(OnItemViewHolderClickListener<I, V> onItemViewHolderClickListener) {
                getHelper().addItemViewHolderClickListener(NO_ID_CLICK_LISTENER, onItemViewHolderClickListener);
                return new BindViewClickChainer<>(getHelper());
            }

            public BindViewClickChainer<I, V> addOnItemViewClickListener(OnItemViewClickListener<I, V> onItemViewClickListener) {
                getHelper().addItemViewClickListener(NO_ID_CLICK_LISTENER, onItemViewClickListener);
                return new BindViewClickChainer<>(getHelper());
            }

        }

        /**
         * this Chainer can chain
         * <p>addOnViewHolderClickListener, addViewClickListener,
         * <p>addViewHolderBinder, addViewBinder
         *
         * @param <V>
         */
        public static class BindViewClickChainer<I, V extends View> extends BindChainer<I, V> {

            public BindViewClickChainer(ItemViewHelper<I, V> builder) {
                super(builder);
            }

            public BindViewClickChainer<I, V> addOnItemViewHolderClickListener(@IdRes int resId, OnItemViewHolderClickListener<I, V> onItemViewHolderClickListener) {
                getHelper().addItemViewHolderClickListener(resId, onItemViewHolderClickListener);
                return new BindViewClickChainer<>(getHelper());

            }

            public BindViewClickChainer<I, V> addOnItemViewClickListener(@IdRes int resId, OnItemViewClickListener<I, V> onItemViewClickListener) {
                getHelper().addItemViewClickListener(resId, onItemViewClickListener);
                return new BindViewClickChainer<>(getHelper());
            }

        }


        /**
         * this Chainer can chain addViewHolderBinder, addViewBinder
         *
         * @param <V>
         */
        public static class BindChainer<I, V extends View> extends BaseViewHelper.BaseChainer<V, BaseViewHolder<V>, ItemViewHelper<I, V>> {

            public BindChainer(ItemViewHelper<I, V> builder) {
                super(builder);
            }

            public ClickViewClickChainer<I, V> addViewBinder(ItemViewBinder<I, V> itemViewBinder) {
                getHelper().setViewBinder(new DefaultItemViewHolderBinder<>(itemViewBinder));
                return new ClickViewClickChainer<>(getHelper());
            }

            public ClickViewClickChainer<I, V> addViewHolderBinder(ItemViewHolderBinder<I, V> itemViewHolderBinder) {
                getHelper().setViewBinder(itemViewHolderBinder);
                return new ClickViewClickChainer<>(getHelper());
            }

        }

        /**
         * this Chainer can chain addOnViewHolderClickListener, addViewClickListener
         *
         * @param <V>
         */
        public static class ClickViewClickChainer<I, V extends View> {

            private final ItemViewHelper<I, V> helper;

            public ClickViewClickChainer(ItemViewHelper<I, V> helper) {
                this.helper = helper;
            }

            public BindViewClickChainer<I, V> addOnItemViewHolderClickListener(OnItemViewHolderClickListener<I, V> onItemViewHolderClickListener) {
                helper.addItemViewHolderClickListener(NO_ID_CLICK_LISTENER, onItemViewHolderClickListener);
                return new BindViewClickChainer<>(helper);
            }

            public BindViewClickChainer<I, V> addOnItemViewClickListener(OnItemViewClickListener<I, V> onItemViewClickListener) {
                helper.addItemViewClickListener(NO_ID_CLICK_LISTENER, onItemViewClickListener);
                return new BindViewClickChainer<>(helper);
            }


            public BindViewClickChainer<I, V> addOnItemViewHolderClickListener(@IdRes int resId, OnItemViewHolderClickListener<I, V> onItemViewHolderClickListener) {
                helper.addItemViewHolderClickListener(resId, onItemViewHolderClickListener);
                return new BindViewClickChainer<>(helper);

            }

            public BindViewClickChainer<I, V> addOnItemViewClickListener(@IdRes int resId, OnItemViewClickListener<I, V> onItemViewClickListener) {
                helper.addItemViewClickListener(resId, onItemViewClickListener);
                return new BindViewClickChainer<>(helper);
            }
        }

        //endregion


        public interface OnItemViewClickListener<I, V extends View> {
            void onItemViewClick(V view, I item);
        }

        public interface OnItemViewHolderClickListener<I, V extends View> {
            void onItemViewClick(BaseViewHolder<V> view, I item);
        }

        private static class DefaultItemViewHolderBinder<I, V extends View>
                implements ItemViewHolderBinder<I, V> {

            @NonNull
            private final ItemViewBinder<I, V> mItemViewBinder;

            public DefaultItemViewHolderBinder(@NonNull ItemViewBinder<I, V> itemViewBinder) {
                mItemViewBinder = itemViewBinder;
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onBindViewHolder(BaseViewHolder<V> viewHolder, I item) {
                mItemViewBinder.onBindView(viewHolder.itemView, item);
            }

        }

    }


}
