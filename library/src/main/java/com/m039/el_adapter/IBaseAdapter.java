package com.m039.el_adapter;

import android.view.View;

/**
 * Created by defuera on 05/07/2016.
 */
public interface IBaseAdapter {

    /**
     * @param viewType is used to bind Item with its ViewCreator and ViewBinder
     */
     <V extends View, VH extends BaseViewHolder<V>>
     BaseViewHolderHelper.BindClickViewClickChainer addViewHolderCreator(int viewType, BaseViewHolderAdapter.ViewHolderCreator<VH> creator);

}
