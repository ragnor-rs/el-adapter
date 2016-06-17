package com.m039.el_adapter;

import android.support.annotation.IdRes;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by defuera on 17/06/2016.
 */
public class ViewHolderBinderChainer<V extends View> {

    private static final int NO_ID = -1;
    private final Map<Integer, Listener> listenersById = new HashMap<>();

    public ViewHolderBinderChainer(final BaseViewAdapter adapter, final BaseViewAdapter.ViewHolderBinder parentBinder, final int viewType) {
        BaseViewAdapter.ViewHolderBinder newBinder = new BaseViewAdapter.ViewHolderBinder() {
            @Override
            public void onBindViewHolder(final BaseViewAdapter.ViewHolder viewHolder) {
                if (parentBinder != null) {
                    parentBinder.onBindViewHolder(viewHolder);
                }


                for (Map.Entry<Integer, Listener> entry : listenersById.entrySet()) {
                    int id = entry.getKey();
                    final Listener viewHolderClickListener = entry.getValue();

                    /**
                     * WARN:
                     *
                     * Performance bottleneck - a lot of calls to new
                     */

                    View.OnClickListener clickListener = new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            viewHolderClickListener.onClick(viewHolder);
                        }

                    };
                    View view = viewHolder.itemView;
                    
                    if (id == NO_ID) {
                        view.setOnClickListener(clickListener);
                    } else {
                        view.findViewById(id).setOnClickListener(clickListener);
                    }

                }


            }

        };

        adapter.addViewHolderBinder(viewType, newBinder);
    }

    public ViewHolderBinderChainer addOnViewHolderClickListener(@IdRes int id, final Listener listener) {
        listenersById.put(id, listener);
        return this;
    }

    public ViewHolderBinderChainer addOnViewHolderClickListener(final Listener listener) {
        listenersById.put(NO_ID, listener);
        return this;
    }

    public interface Listener<V extends View, VH extends BaseViewAdapter.ViewHolder<V>> {
        void onClick(BaseViewAdapter.ViewHolder viewHolder);
    }

}
