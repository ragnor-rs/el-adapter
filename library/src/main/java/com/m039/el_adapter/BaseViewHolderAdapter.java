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

import com.m039.el_adapter.BaseViewHolderHelper.ViewHolderClickListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for adapters.
 * <p>
 * The main difference between this class and other that it doesn't know anything about data only
 * how to create view.
 * <p>
 * BaseViewHolderAdapter is only responsible for creating view, not binding them.
 * <p>
 * Logic for binding should be in another/inherited classes.
 * <p>
 * Tha main idea to separate creation and binding cause creation always returns same view for a same
 * viewType, but binding may differ.
 * <p>
 * Created by m039 on 3/3/16.
 */
public abstract class BaseViewHolderAdapter<B extends BaseViewHolderHelper> extends RecyclerView.Adapter<BaseViewHolder<?>>
        implements IBaseAdapter {

    public static final int DEFAULT_VIEW_TYPE = 0;

    /**
     * This interface is used to create views in {@link #onCreateViewHolder(ViewGroup)}
     * <p>
     * todo add type check for ViewHolders
     */
    public interface ViewHolderCreator<VH extends BaseViewHolder> {

        /**
         * @param parent parent of a new view
         * @return should be a new created viewHolder
         */
        VH onCreateViewHolder(ViewGroup parent);

    }

    /**
     * Created by defuera on 05/07/2016.
     */
    public interface ViewHolderBinder<VH extends BaseViewHolder> {

        /**
         * To get position call viewHolder.getAdapterPosition()
         *
         * @param viewHolder viewHolder to bind
         */
        void onBindViewHolder(VH viewHolder);

    }


    private final Map<Integer, B> builderMap = new HashMap<>();

    protected abstract <V extends View, VH extends BaseViewHolder<V>> B createBuilder(ViewHolderCreator<VH> creator);

    @Override
    public <V extends View, VH extends BaseViewHolder<V>> BaseViewHolderHelper.BindClickViewClickChainer<V, VH>
    addViewHolderCreator(int viewType, ViewHolderCreator<VH> creator) {

        B elBuilder = createBuilder(creator);
        builderMap.put(viewType, elBuilder);

        return (BaseViewHolderHelper.BindClickViewClickChainer<V, VH>) elBuilder.getBaseViewHolderChainer();
    }


    //region RecyclerView.Adapter

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        B builder = getBuilder(viewType);
        ViewHolderCreator viewHolderCreator = builder.getViewHolderCreator();

        if (viewHolderCreator == null) {
            throw new IllegalStateException("Can't create view of type " + viewType + ".");
        }


        final BaseViewHolder viewHolder = viewHolderCreator.onCreateViewHolder(parent);
        final View view = viewHolder.itemView;
        for (Object entryO : builder.getViewHolderClickListeners().entrySet()){
            Map.Entry<Integer, ViewHolderClickListener> entry = (Map.Entry<Integer, ViewHolderClickListener>) entryO; //todo wtf
            int id = entry.getKey();
            final ViewHolderClickListener viewHolderClickListener = entry.getValue();

            /**
             * WARN:
             *
             * Performance bottleneck - a lot of calls to new
             */

            View.OnClickListener clickListener = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    viewHolderClickListener.onViewHolderClick(viewHolder);
                }

            };

            if (id == BaseViewHolderHelper.NO_ID_CLICK_LISTENER) {
                view.setOnClickListener(clickListener);
            } else {
                view.findViewById(id).setOnClickListener(clickListener);
            }
        }

        return viewHolder;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        ViewHolderBinder viewHolderBinder = getBuilder(getItemViewType(position)).getViewHolderBinder();

        if (viewHolderBinder != null) {
            viewHolderBinder.onBindViewHolder(holder);
        } else {
            // do nothing, don't bind a thing
        }
    }

    //endregion

    protected B getBuilder(int viewType) {
        return builderMap.get(viewType);
    }
}
