package com.m039.el_adapter.denis;

import android.view.View;

import com.m039.el_adapter.BaseViewHolder;
import com.m039.el_adapter.ViewHolderBinder;
import com.m039.el_adapter.ViewHolderCreator;

/**
 * Created by defuera on 05/07/2016.
 */
public class ElBuilder<V extends View, VH extends BaseViewHolder<V>> {

    private final ViewHolderCreator<VH> creator;
    private ViewHolderBinder<VH> viewHolderBinder;

    public ElBuilder(ViewHolderCreator<VH> creator) {
        this.creator = creator;
    }

    public ViewHolderCreator<VH> getViewHolderCreator() {
        return creator;
    }

    public ViewHolderBinder<VH> getViewHolderBinder() {
        return viewHolderBinder;
    }

    public ViewHolderBinderChainer viewHolderBinderChainer() {
        return new ViewHolderBinderChainer();
    }

    public class ViewHolderBinderChainer {

        public void addViewBinder(ViewHolderBinder<VH> viewHolderBinder){
            ElBuilder.this.viewHolderBinder = viewHolderBinder;
        }
    }
}
