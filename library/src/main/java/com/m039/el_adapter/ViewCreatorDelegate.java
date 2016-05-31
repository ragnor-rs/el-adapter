package com.m039.el_adapter;

import com.m039.el_adapter.BaseViewAdapter.ViewCreator;
import com.m039.el_adapter.BaseViewAdapter.ViewHolderCreator;

/**
 * Created by m039 on 6/1/16.
 */
public interface ViewCreatorDelegate<B extends CommandBuilder> {

    B addViewCreator(Class clazz, ViewCreator viewCreator);

    B addViewCreator(Class clazz, int viewTypeOfClass, ViewCreator viewCreator);

    B addViewCreator(int viewType, ViewCreator viewCreator);

    B addViewHolderCreator(Class clazz, ViewHolderCreator viewHolderCreator);

    B addViewHolderCreator(Class clazz, int viewTypeOfClass, ViewHolderCreator viewHolderCreator);

    B addViewHolderCreator(int viewType, ViewHolderCreator viewHolderCreator);

}
