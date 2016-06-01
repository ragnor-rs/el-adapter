package com.m039.el_adapter;

import android.view.View;

import com.m039.el_adapter.BaseViewAdapter.ViewCreator;
import com.m039.el_adapter.BaseViewAdapter.ViewHolderCreator;

/**
 * Created by m039 on 6/1/16.
 */
/* package */ interface ViewCreatorDelegate<B extends BindingBuilder> {

    <I, V extends View>
    B addViewCreator(Class<I> clazz, ViewCreator<V> viewCreator);

    <I, V extends View>
    B addViewCreator(Class<I> clazz, int viewTypeOfClass, ViewCreator<V> viewCreator);

    <I, V extends View>
    B addViewCreator(int viewType, ViewCreator<V> viewCreator);

    <I, V extends View>
    B addViewHolderCreator(Class<I> clazz, ViewHolderCreator<V> viewHolderCreator);

    <I, V extends View>
    B addViewHolderCreator(Class<I> clazz, int viewTypeOfClass, ViewHolderCreator<V> viewHolderCreator);

    <I, V extends View>
    B addViewHolderCreator(int viewType, ViewHolderCreator<V> viewHolderCreator);

}
