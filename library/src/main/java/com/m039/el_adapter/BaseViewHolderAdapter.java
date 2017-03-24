/*
 * Copyright (C) 2016 Dmitry Mozgin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
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

import com.m039.el_adapter.BaseViewHolderBuilder.ViewHolderClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
public abstract class BaseViewHolderAdapter<B extends BaseViewHolderBuilder>
        extends RecyclerView.Adapter<BaseViewHolder<?>>
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
    public <V extends View, VH extends BaseViewHolder<V>> BaseViewHolderBuilder.BindClickViewClickChainer<V, VH> addViewHolderCreator(int viewType, ViewHolderCreator<VH> creator) {

        B elBuilder = createBuilder(creator);
        builderMap.put(viewType, elBuilder);

        return (BaseViewHolderBuilder.BindClickViewClickChainer<V, VH>) elBuilder.getBaseViewHolderChainer();

    }


    //region RecyclerView.Adapter

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        B builder = getBuilder(viewType);
        if (builder == null) {
            throw new UnknownViewType("Can't create view of type " + viewType + ".");
        }
        return builder.getViewHolderCreator().onCreateViewHolder(parent);
    }

    @Override
    public void onViewAttachedToWindow(final BaseViewHolder<?> holder) {
        super.onViewAttachedToWindow(holder);
        attachListeners(
                holder,
                new ClickListenerSource<ViewHolderClickListener, B>() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public Map<?, ViewHolderClickListener> getClickListeners(B builder) {
                        return builder.getViewHolderClickListeners();
                    }

                },
                new ClickListenerWrapper<ViewHolderClickListener>() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onClick(ViewHolderClickListener viewClickListener, BaseViewHolder<?> holder) {
                        viewClickListener.onViewHolderClick(holder);
                    }

                }
        );
    }

    interface ClickListenerSource<L, B> {
        Map<?, L> getClickListeners(B builder);
    }

    interface ClickListenerWrapper<L> {
        void onClick(L viewClickListener, BaseViewHolder<?> holder);
    }

    <L> void attachListeners(
            final BaseViewHolder<?> holder,
            final ClickListenerSource<L, B> clickListenerSource,
            final ClickListenerWrapper<L> clickListenerWrapper
    ) {

        final View view = holder.getItemView();
        final int adapterPosition = holder.getAdapterPosition();

        if (adapterPosition == RecyclerView.NO_POSITION) {
            return;
        }

        Map<?, L> clickListeners = clickListenerSource.getClickListeners(getBuilder(holder.getItemViewType()));
        Set<? extends Map.Entry<?, L>> viewClickListeners = clickListeners.entrySet();

        List<Integer> viewIds = holder.getViewsWithListenersIds();
        if (viewIds == null) {
            holder.setViewsWithListenersIds(viewIds = new ArrayList<>());
        }

        for (Map.Entry<?, L> clickListenerEntry : viewClickListeners) {

            final L viewClickListener = clickListenerEntry.getValue();
            View.OnClickListener clickListener = new View.OnClickListener() {

                @SuppressWarnings("unchecked")
                @Override
                public void onClick(View v) {
                    clickListenerWrapper.onClick(viewClickListener, holder);
                }

            };

            final Integer id = (Integer) clickListenerEntry.getKey();
            if (id.equals(BaseViewAdapter.BaseViewHelper.NO_ID_CLICK_LISTENER)) {
                view.setOnClickListener(clickListener);
            } else {
                View targetView = view.findViewById(id);
                if (targetView != null) {
                    targetView.setOnClickListener(clickListener);
                }
            }

            viewIds.add(id);

        }

    }

    @Override
    public void onViewDetachedFromWindow(BaseViewHolder<?> holder) {

        /*
         * To fix https://zvooq1.atlassian.net/browse/ZAN-1558, we have to detach all the listeners
         */

        final View view = holder.getItemView();

        List<Integer> viewsWithListenersIds = holder.getViewsWithListenersIds();

        for (Integer id : viewsWithListenersIds) {
            if (id.equals(BaseViewAdapter.BaseViewHelper.NO_ID_CLICK_LISTENER)) {
                view.setOnClickListener(null);
            } else {
                view.findViewById(id).setOnClickListener(null);
            }
        }

        holder.setViewsWithListenersIds(null);

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        ViewHolderBinder viewBinder = getBuilder(getItemViewType(position)).getViewHolderBinder();
        if (viewBinder != null) {
            viewBinder.onBindViewHolder(holder);
        }
    }

    //endregion


    protected B getBuilder(int viewType) {
        return builderMap.get(viewType);
    }

}
