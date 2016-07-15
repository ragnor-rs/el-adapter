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
public abstract class BaseViewAdapter<B extends BaseViewAdapter.BaseViewHelper> extends BaseViewHolderAdapter<B> {

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
         * @param view     view to bind
         * @param position
         */
        void onBindView(V view, int position);

    }


    //region RecyclerView#Adapter


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final BaseViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

        final View view = viewHolder.itemView;
        for (Object entryO : getHelper(viewType).getViewClickListeners().entrySet()) {
            Map.Entry<Integer, BaseViewHelper.ViewClickListener> entry = (Map.Entry<Integer, BaseViewHelper.ViewClickListener>) entryO; //todo wtf
            int id = entry.getKey();
            final BaseViewHelper.ViewClickListener viewClickListener = entry.getValue();

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

            if (id == BaseViewHelper.NO_ID_CLICK_LISTENER) {
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


    public <V extends View> BaseViewHelper.BindClickViewClickChainer<V> addViewCreator(int viewType, ViewCreator<V> viewCreator) {
        addViewHolderCreator(viewType, new DefaultViewHolderCreator<>(viewCreator));
        return (BaseViewHelper.BindClickViewClickChainer<V>) getHelper(viewType).getBaseViewChainer();
    }

    protected <V extends View> ViewBinder<V> getViewBinder(int viewType) {
        return (ViewBinder<V>) getHelper(viewType).getViewBinder();
    }

    protected <V extends View, VH extends BaseViewHolder<V>>ViewHolderCreator<VH> getViewHolderCreator(int viewType){
        return getHelper(viewType).getViewHolderCreator();
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

    public static class BaseViewHelper<V extends View> extends BaseViewHolderHelper<V, BaseViewHolder<V>> {

        private ViewBinder<V> viewBinder;
        private Map<Integer, ViewClickListener<V>> viewClickListenersById = new HashMap<>();

        public BaseViewHelper(ViewHolderCreator<BaseViewHolder<V>> creator) {
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
         * this Chainer can chain addOnViewHolderClickListener, addOnViewHolderClickListener, addViewHolderBinder
         *
         * @param <V>
         */
        public static class BindClickViewClickChainer<V extends View> extends BindViewClickChainer<V> {

            public BindClickViewClickChainer(BaseViewHelper<V> builder) {
                super(builder);
            }

            public BindViewClickChainer<V> addViewHolderClickListener(ViewHolderClickListener<V> viewHolderClickListener) {
                getHelper().addViewHolderClickListener(NO_ID_CLICK_LISTENER, viewHolderClickListener);
                return new BindViewClickChainer<>(getHelper());
            }

            public BindViewClickChainer<V> addViewClickListener(ViewClickListener<V> viewClickListener) {
                getHelper().addViewClickListener(NO_ID_CLICK_LISTENER, viewClickListener);
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
        public static class BindViewClickChainer<V extends View> extends BindChainer<V, BaseViewHelper<V>> {

            public BindViewClickChainer(BaseViewHelper<V> builder) {
                super(builder);
            }

            public BindViewClickChainer<V> addViewHolderClickListener(@IdRes int resId, ViewHolderClickListener<V> viewHolderClickListener) {
                getHelper().addViewHolderClickListener(resId, viewHolderClickListener);
                return new BindViewClickChainer<>(getHelper());

            }

            public BindViewClickChainer<V> addViewClickListener(@IdRes int resId, ViewClickListener<V> viewClickListener) {
                getHelper().addViewClickListener(resId, viewClickListener);
                return new BindViewClickChainer<>(getHelper());
            }

        }

        /**
         * this Chainer can chain addViewHolderBinder, addViewBinder
         *
         * @param <V>
         */
        public static class BindChainer<V extends View, B extends BaseViewHelper<V>> extends BaseViewHolderHelper.BindChainer<V, BaseViewHolder<V>, B> {

            public BindChainer(B helper) {
                super(helper);
            }

            public ClickViewClickChainer<V> addViewBinder(ViewBinder<V> viewBinder) {
                getHelper().setViewBinder(viewBinder);
                return new ClickViewClickChainer<>(getHelper());

            }

        }

        /**
         * this Chainer can chain addOnViewHolderClickListener, addViewClickListener
         *
         * @param <V>
         */
        public static class ClickViewClickChainer<V extends View> {

            private final BaseViewHelper<V> helper;

            public ClickViewClickChainer(BaseViewHelper<V> helper) {
                this.helper = helper;
            }

            public BindViewClickChainer<V> addViewHolderClickListener(ViewHolderClickListener<V> viewHolderClickListener) {
                return addViewHolderClickListener(NO_ID_CLICK_LISTENER, viewHolderClickListener);

            }

            public BindViewClickChainer<V> addViewClickListener(ViewClickListener<V> viewClickListener) {
                return addViewClickListener(NO_ID_CLICK_LISTENER, viewClickListener);
            }

            public BindViewClickChainer<V> addViewHolderClickListener(@IdRes int resId, ViewHolderClickListener<V> viewHolderClickListener) {
                helper.addViewHolderClickListener(resId, viewHolderClickListener);
                return new BindViewClickChainer<>(helper);

            }

            public BindViewClickChainer<V> addViewClickListener(@IdRes int resId, ViewClickListener<V> viewClickListener) {
                helper.addViewClickListener(resId, viewClickListener);
                return new BindViewClickChainer<>(helper);
            }
        }

        public interface ViewClickListener<V extends View> {
            void onViewClick(V view, int position);
        }

    }

    //endregion


}