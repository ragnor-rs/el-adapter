package com.m039.el_adapter;

import android.view.View;

/**
 * Created by m039 on 6/1/16.
 */
/* package */ interface ItemViewCreatorDelegate<B extends ItemViewViewBindingBuilder>
        extends ViewCreatorDelegate<B> {

    <I, V extends View>
    B addViewCreator(Class<I> clazz, BaseViewAdapter.ViewCreator<V> viewCreator);

    <I, V extends View>
    B addViewCreator(Class<I> clazz, int viewTypeOfClass, BaseViewAdapter.ViewCreator<V> viewCreator);

    <I, V extends View>
    B addViewHolderCreator(Class<I> clazz, BaseViewAdapter.ViewHolderCreator<V> viewHolderCreator);

    <I, V extends View>
    B addViewHolderCreator(Class<I> clazz, int viewTypeOfClass, BaseViewAdapter.ViewHolderCreator<V> viewHolderCreator);

}
