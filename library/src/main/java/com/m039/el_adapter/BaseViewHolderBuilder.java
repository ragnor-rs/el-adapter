package com.m039.el_adapter;

import android.view.View;

import com.m039.el_adapter.BaseViewHolderAdapter.ViewHolderBinder;
import com.m039.el_adapter.BaseViewHolderAdapter.ViewHolderCreator;

/**
 * Created by defuera on 05/07/2016.
 */
public class BaseViewHolderBuilder<V extends View, VH extends BaseViewHolder<V>> {

    private final ViewHolderCreator<VH> viewHolderCreator;
    private final ViewHolderBinderChainer<V, VH> viewHolderBinderChainer = new ViewHolderBinderChainer<>(this);
    private ViewHolderBinder<VH> viewHolderBinder;

    public BaseViewHolderBuilder(ViewHolderCreator<VH> viewHolderCreator) {
        this.viewHolderCreator = viewHolderCreator;
    }

    public ViewHolderCreator<VH> getViewHolderCreator() {
        return viewHolderCreator;
    }

    public ViewHolderBinder<VH> getViewHolderBinder() {
        return viewHolderBinder;
    }

    public ViewHolderBinderChainer<V, VH> getViewHolderBinderChainer() {
        return viewHolderBinderChainer;
    }

    public void setViewHolderBinder(ViewHolderBinder<VH> viewHolderBinder) {
        this.viewHolderBinder = viewHolderBinder;
    }


    public static class ViewHolderBinderChainer<V extends View, VH extends BaseViewHolder<V>> {

        private final BaseViewHolderBuilder<V, VH> baseViewHolderBuilder;

        public ViewHolderBinderChainer(BaseViewHolderBuilder<V, VH> baseViewHolderBuilder) {
            this.baseViewHolderBuilder = baseViewHolderBuilder;
        }

        public void addViewHolderBinder(ViewHolderBinder<VH> viewHolderBinder) {
            baseViewHolderBuilder.setViewHolderBinder(viewHolderBinder);
        }
    }

}
