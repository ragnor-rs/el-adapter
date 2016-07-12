package com.m039.el_adapter;

import android.view.View;

/**
 * Created by defuera on 05/07/2016.
 */
public interface IBaseAdapter {

     <V extends View, VH extends BaseViewHolder<V>>
     BaseViewHolderBuilder.BindClickViewClickChainer addViewHolderCreator(int viewType, BaseViewHolderAdapter.ViewHolderCreator<VH> creator);

}
