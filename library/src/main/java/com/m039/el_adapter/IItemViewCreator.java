/**
 * Copyright (C) 2016 Dmitry Mozgin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.m039.el_adapter;

import android.view.View;

/**
 * Created by m039 on 6/1/16.
 */
/* package */ interface IItemViewCreator<B extends ItemViewCreatorChainer>
        extends IViewCreator<B> {

    <I, V extends View>
    B addViewCreator(Class<I> clazz, BaseViewAdapter.ViewCreator<V> viewCreator);

    <I, V extends View>
    B addViewCreator(Class<I> clazz, int typeOfClass, BaseViewAdapter.ViewCreator<V> viewCreator);

    <I, V extends View>
    B addViewHolderCreator(Class<I> clazz, ViewHolderCreator<BaseViewHolder<V>> viewHolderCreator);

    <I, V extends View>
    B addViewHolderCreator(Class<I> clazz, int typeOfClass, ViewHolderCreator<BaseViewHolder<V>> viewHolderCreator);

}
