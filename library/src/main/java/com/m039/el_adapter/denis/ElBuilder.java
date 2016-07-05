package com.m039.el_adapter.denis;

import android.view.View;

import com.m039.el_adapter.BaseViewHolder;
import com.m039.el_adapter.ViewHolderBinder;
import com.m039.el_adapter.ViewHolderCreator;

/**
 * Created by defuera on 05/07/2016.
 */
public class ElBuilder<V extends View, VH extends BaseViewHolder<V>> {

    private final ViewHolderCreator<VH> viewHolderCreator;
    private final ViewHolderBinderChainer<V, VH> viewHolderBinderChainer = new ViewHolderBinderChainer<>(this);
    private ViewHolderBinder<VH> viewHolderBinder;

    public ElBuilder(ViewHolderCreator<VH> viewHolderCreator) {
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

        private final ElBuilder<V, VH> elBuilder;

        public ViewHolderBinderChainer(ElBuilder<V, VH> elBuilder) {
            this.elBuilder = elBuilder;
        }

        public void addViewHolderBinder(ViewHolderBinder<VH> viewHolderBinder) {
            elBuilder.setViewHolderBinder(viewHolderBinder);
        }
    }
}
