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

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by defuera on 05/07/2016.
 * adds simple addViewCreator, addViewBinderMethods
 */
public abstract class BaseViewAdapter<B extends BaseViewAdapter.BaseViewBuilder> extends BaseViewHolderAdapter<B> {

    /**
     * This interface is used to create views in {@link #onCreateViewHolder(ViewGroup, int)}
     */
    public interface ViewCreator<V extends View> {

        /**
         * @param parent parent of a new view
         * @return should be a new created view
         */
        V onCreateView(ViewGroup parent);

    }

    public interface ViewBinder<V extends View> {

        /**
         * To get position call view.getAdapterPosition()
         *
         * @param view view to bind
         * @param position
         */
        void onBindView(V view, int position);

    }


    //region RecyclerView#Adapter


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final BaseViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

        final View view = viewHolder.itemView;
        for (Object entryO : getBuilder(viewType).getViewClickListeners().entrySet()) {
            Map.Entry<Integer, BaseViewBuilder.ViewClickListener> entry = (Map.Entry<Integer, BaseViewBuilder.ViewClickListener>) entryO; //todo wtf
            int id = entry.getKey();
            final BaseViewBuilder.ViewClickListener viewClickListener = entry.getValue();

            /**
             * WARN:
             *
             * Performance bottleneck - a lot of calls to new
             */

            View.OnClickListener clickListener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    viewClickListener.onViewClick(view, viewHolder.getAdapterPosition());
                }

            };

            if (id == BaseViewBuilder.NO_ID_CLICK_LISTENER) {
                view.setOnClickListener(clickListener);
            } else {
                view.findViewById(id).setOnClickListener(clickListener);
            }
        }

        return viewHolder;


    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        ViewBinder<View> viewBinder = getViewBinder(getItemViewType(position));

        if (viewBinder != null) {
            viewBinder.onBindView(holder.itemView, holder.getAdapterPosition());
        } else {
            // do nothing, don't bind a thing
        }

    }

    //endregion


    public <V extends View> BaseViewBuilder.BindClickViewClickChainer<V> addViewCreator(int viewType, ViewCreator<V> viewCreator) {
        addViewHolderCreator(viewType, new DefaultViewHolderCreator<>(viewCreator));
        return (BaseViewBuilder.BindClickViewClickChainer<V>) getBuilder(viewType).getBaseViewChainer();
    }

    protected <V extends View> ViewBinder<V> getViewBinder(int viewType) {
        return (ViewBinder<V>) getBuilder(viewType).getViewBinder();
    }

    protected static class DefaultViewHolderCreator<V extends View> implements ViewHolderCreator<BaseViewHolder<V>> {

        @NonNull
        private final ViewCreator<V> mViewCreator;

        DefaultViewHolderCreator(@NonNull ViewCreator<V> viewCreator) {
            mViewCreator = viewCreator;
        }

        @NonNull
        public ViewCreator<V> getViewCreator() {
            return mViewCreator;
        }

        @Override
        public BaseViewHolder<V> onCreateViewHolder(ViewGroup parent) {
            V view = mViewCreator.onCreateView(parent);

            setLayoutParams(view);

            return new BaseViewHolder<>(view);
        }

        protected void setLayoutParams(View view) {
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(new ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT));
            }
        }

    }

    public static class BaseViewBuilder<V extends View> extends BaseViewHolderHelper<V, BaseViewHolder<V>> { //todo rename to BaseViewHelper

        private ViewBinder<V> viewBinder;
        private Map<Integer, ViewClickListener<V>> viewClickListenersById = new HashMap<>();

        public BaseViewBuilder(ViewHolderCreator<BaseViewHolder<V>> creator) {
            super(creator);
        }

        public BindClickViewClickChainer<V> getBaseViewChainer() {
            return new BindClickViewClickChainer<>(this);
        }

        public ViewBinder<V> getViewBinder() {
            return viewBinder;
        }

        public void setViewBinder(ViewBinder<V> viewBinder) {
            this.viewBinder = viewBinder;
        }

        public void addViewClickListener(@IdRes int resId, ViewClickListener<V> viewClickListener) {
            viewClickListenersById.put(resId, viewClickListener);
        }

        public Map<Integer, ViewClickListener<V>> getViewClickListeners() {
            return viewClickListenersById;
        }


        //region Chainers

        /**
         * this Chainer can chain addViewHolderClickListener, addViewHolderClickListener, addViewHolderBinder
         *
         * @param <V>
         */
        public static class BindClickViewClickChainer<V extends View> extends BindViewClickChainer<V> {

            public BindClickViewClickChainer(BaseViewBuilder<V> builder) {
                super(builder);
            }

            public BindViewClickChainer<V> addViewHolderClickListener(ViewHolderClickListener<V> viewHolderClickListener) {
                getBuilder().addViewHolderClickListener(NO_ID_CLICK_LISTENER, viewHolderClickListener);
                return new BindViewClickChainer<>(getBuilder());
            }

            public BindViewClickChainer<V> addViewClickListener(ViewClickListener<V> viewClickListener) {
                getBuilder().addViewClickListener(NO_ID_CLICK_LISTENER, viewClickListener);
                return new BindViewClickChainer<>(getBuilder());
            }

        }

        /**
         * this Chainer can chain
         * <p>addViewHolderClickListener, addViewClickListener,
         * <p>addViewHolderBinder, addItemViewBinder
         *
         * @param <V>
         */
        public static class BindViewClickChainer<V extends View> extends BindChainer<V, BaseViewBuilder<V>> {

            public BindViewClickChainer(BaseViewBuilder<V> builder) {
                super(builder);
            }

            public BindViewClickChainer<V> addViewHolderClickListener(@IdRes int resId, ViewHolderClickListener<V> viewHolderClickListener) {
                getBuilder().addViewHolderClickListener(resId, viewHolderClickListener);
                return new BindViewClickChainer<>(getBuilder());

            }

            public BindViewClickChainer<V> addViewClickListener(@IdRes int resId, ViewClickListener<V> viewClickListener) {
                getBuilder().addViewClickListener(resId, viewClickListener);
                return new BindViewClickChainer<>(getBuilder());
            }


        }

        /**
         * this Chainer can chain addViewHolderBinder, addItemViewBinder
         *
         * @param <V>
         */
        public static class BindChainer<V extends View, B extends BaseViewBuilder<V>> extends BaseViewHolderHelper.BindChainer<V, BaseViewHolder<V>, B> {

            public BindChainer(B builder) {
                super(builder);
            }

            public void addViewBinder(ViewBinder<V> viewBinder) {
                getBuilder().setViewBinder(viewBinder);
            }

        }


        public interface ViewClickListener<V extends View> {
            void onViewClick(V view, int position);
        }

    }

    //endregion


}