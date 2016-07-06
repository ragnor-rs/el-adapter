package com.m039.el_adapter;

import android.view.View;

/**
 * Created by defuera on 05/07/2016.
 */
public interface IBaseAdapter {

     //todo BaseViewHolderAdapter.ViewHolderCreator probably should not be in interface
     //todo use interface instead
     <V extends View, VH extends BaseViewHolder<V>>
     BaseViewHolderBuilder.ViewHolderBinderChainer addViewHolderCreator(int viewType, BaseViewHolderAdapter.ViewHolderCreator<VH> creator);

}
