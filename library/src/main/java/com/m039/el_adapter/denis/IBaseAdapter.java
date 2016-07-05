package com.m039.el_adapter.denis;

import android.view.View;

import com.m039.el_adapter.BaseViewHolder;
import com.m039.el_adapter.ViewHolderCreator;

/**
 * Created by defuera on 05/07/2016.
 */
public interface IBaseAdapter {

    <V extends View, VH extends BaseViewHolder<V>>
    ElBuilder<V, VH>.ViewHolderBinderChainer addViewHolderCreator(int viewType, ViewHolderCreator<VH> creator);

}
