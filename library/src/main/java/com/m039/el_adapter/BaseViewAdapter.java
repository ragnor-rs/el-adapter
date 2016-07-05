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

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.m039.el_adapter.denis.ElBuilder;
import com.m039.el_adapter.denis.IBaseAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for adapters.
 * <p>
 * The main difference between this class and other that it doesn't know anything about data only
 * how to create view.
 * <p>
 * BaseViewAdapter is only responsible for creating view, not binding them.
 * <p>
 * Logic for binding should be in another/inherited classes.
 * <p>
 * Tha main idea to separate creation and binding cause creation always returns same view for a same
 * viewType, but binding may differ.
 * <p>
 * Created by m039 on 3/3/16.
 */
public abstract class BaseViewAdapter<B extends ElBuilder> extends RecyclerView.Adapter<BaseViewHolder<?>> //todo rename to BaseViewHolderAdapter
        implements IBaseAdapter<B> {

    protected final Map<Integer, B> builderMap = new HashMap<>();

    protected BaseViewAdapter() {}

    @Override
    public <V extends View, VH extends BaseViewHolder<V>> B.ViewHolderBinderChainer
    addViewHolderCreator(int viewType, ViewHolderCreator<VH> creator) {

        B elBuilder = createBuilder(creator);
        builderMap.put(viewType, elBuilder);

        return elBuilder.viewHolderBinderChainer();
    }

    protected abstract <V extends View, VH extends BaseViewHolder<V>>  B createBuilder(ViewHolderCreator<VH> creator);

    /**
     * @return viewHolderCreator associated with <code>viewType</code> or null
     */
    protected ViewHolderCreator getViewHolderCreator(int viewType) {
        return builderMap.get(viewType).getViewHolderCreator();
    }

    /**
     * @return viewHolderBinder associated with <code>viewType</code> or null
     */
    protected ViewHolderBinder<?> getViewHolderBinder(int viewType) {
        return builderMap.get(viewType).getViewHolderBinder();
    }

    //region RecyclerView.Adapter

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderCreator viewHolderCreator = getViewHolderCreator(viewType);

        if (viewHolderCreator == null) {
            throw new IllegalStateException("Can't create view of type " + viewType + ".");
        }

        return viewHolderCreator.onCreateViewHolder(parent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        ViewHolderBinder viewHolderBinder = getViewHolderBinder(getItemViewType(position));

        if (viewHolderBinder != null) {
            viewHolderBinder.onBindViewHolder(holder);
        } else {
            // do nothing, don't bind a thing
        }
    }

    //endregion

}
