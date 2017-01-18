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
import android.view.View;

import com.m039.el_adapter.BaseViewHolderAdapter.ViewHolderBinder;
import com.m039.el_adapter.BaseViewHolderAdapter.ViewHolderCreator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by defuera on 05/07/2016.
 */
public class BaseViewHolderBuilder<V extends View, VH extends BaseViewHolder<V>> {

    public static final int NO_ID_CLICK_LISTENER = 0;

    private final ViewHolderCreator<VH> viewHolderCreator;
    private ViewHolderBinder<VH> viewHolderBinder;
    private Map<Integer, ViewHolderClickListener<V>> viewHolderClickListeners = new HashMap<>();

    public BaseViewHolderBuilder(ViewHolderCreator<VH> viewHolderCreator) {
        this.viewHolderCreator = viewHolderCreator;
    }

    public ViewHolderCreator<VH> getViewHolderCreator() {
        return viewHolderCreator;
    }

    public ViewHolderBinder<VH> getViewHolderBinder() {
        return viewHolderBinder;
    }

    public void setViewHolderBinder(ViewHolderBinder<VH> viewHolderBinder) {
        this.viewHolderBinder = viewHolderBinder;
    }

    public void addViewHolderClickListener(@IdRes int resId, ViewHolderClickListener<V> viewHolderClickListener) {
        viewHolderClickListeners.put(resId, viewHolderClickListener);
    }

    public Map<Integer, ViewHolderClickListener<V>> getViewHolderClickListeners() {
        return viewHolderClickListeners;
    }

    public BindClickViewClickChainer<V, VH> getBaseViewHolderChainer() {
        return new BindClickViewClickChainer<>(this);
    }

    //region Chainers

    /**
     * this Chainer can chain addOnViewHolderClickListener, addOnViewHolderClickListener, addViewHolderBinder
     *
     * @param <V>
     * @param <VH>
     */
    public static class BindClickViewClickChainer<V extends View, VH extends BaseViewHolder<V>> extends BindViewClickChainer<V, VH> {


        public BindClickViewClickChainer(BaseViewHolderBuilder<V, VH> builder) {
            super(builder);
        }

        public BindViewClickChainer<V, VH> addViewHolderClickListener(ViewHolderClickListener<V> viewHolderClickListener) {
            getHelper().addViewHolderClickListener(NO_ID_CLICK_LISTENER, viewHolderClickListener);
            return new BindViewClickChainer<>(getHelper());
        }

    }


    /**
     * this Chainer can chain addOnViewHolderClickListener, addViewHolderBinder
     *
     * @param <V>
     * @param <VH>
     */
    public static class BindViewClickChainer<V extends View, VH extends BaseViewHolder<V>> extends BindChainer<V, VH, BaseViewHolderBuilder<V, VH>> {

        public BindViewClickChainer(BaseViewHolderBuilder<V, VH> builder) {
            super(builder);
        }

        public BindViewClickChainer<V, VH> addOnViewHolderClickListener(@IdRes int resId, ViewHolderClickListener<V> viewHolderClickListener) {
            getHelper().addViewHolderClickListener(resId, viewHolderClickListener);
            return new BindViewClickChainer<>(getHelper());
        }

    }

    /**
     * this Chainer can chain addViewHolderBinder
     *
     * @param <V>
     * @param <VH>
     */
    public static class BindChainer<V extends View, VH extends BaseViewHolder<V>, B extends BaseViewHolderBuilder<V, VH>> extends BaseChainer<V, VH, B> {

        public BindChainer(B helper) {
            super(helper);
        }

        public ClickViewClickChainer<V, VH, B> addViewHolderBinder(ViewHolderBinder<VH> viewHolderBinder) {
            getHelper().setViewHolderBinder(viewHolderBinder);
            return new ClickViewClickChainer<>(getHelper());
        }
    }

    /**
     * this Chainer can chain addOnViewHolderClickListener, addViewClickListener
     *
     * @param <V>
     * @param <VH>
     */
    public static class ClickViewClickChainer<V extends View, VH extends BaseViewHolder<V>, B extends BaseViewHolderBuilder<V, VH>> extends BaseChainer<V, VH, B> {

        public ClickViewClickChainer(B helper) {
            super(helper);
        }

        public BindViewClickChainer<V, VH> addOnViewHolderClickListener(ViewHolderClickListener<V> viewHolderClickListener) {
            getHelper().addViewHolderClickListener(NO_ID_CLICK_LISTENER, viewHolderClickListener);
            return new BindViewClickChainer<>(getHelper());
        }

        public BindViewClickChainer<V, VH> addOnViewHolderClickListener(@IdRes int resId, ViewHolderClickListener<V> viewHolderClickListener) {
            getHelper().addViewHolderClickListener(resId, viewHolderClickListener);
            return new BindViewClickChainer<>(getHelper());
        }

    }

    public static class BaseChainer<V extends View, VH extends BaseViewHolder<V>, B extends BaseViewHolderBuilder<V, VH>> {

        private final B helper;

        public BaseChainer(B helper) {
            this.helper = helper;
        }

        public B getHelper() {
            return helper;
        }
    }

    //endregion


    public interface ViewHolderClickListener<V extends View> {
        void onViewHolderClick(BaseViewHolder<V> viewHolder);
    }
}
