package com.m039.el_adapter;

import android.support.annotation.IdRes;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by defuera on 17/06/2016.
 */
public class ViewHolderBinderChainer<VH extends BaseViewAdapter.ViewHolder> {

    private static final int NO_ID = -1;
    private final Map<Integer, Listener<VH>> listenersById = new HashMap<>();
    private final BaseViewAdapter adapter;
    private final BaseViewAdapter.ViewHolderBinder<VH> newBinder;
    private final int viewType;

    public ViewHolderBinderChainer(final BaseViewAdapter adapter, final BaseViewAdapter.ViewHolderBinder<VH> parentBinder, final int viewType) {
        this.adapter = adapter;
        this.viewType = viewType;
        newBinder = new BaseViewAdapter.ViewHolderBinder<VH>() {

            @Override
            public void onBindViewHolder(final VH viewHolder) {

                if (parentBinder != null) {
                    parentBinder.onBindViewHolder(viewHolder);
                }

                for (Map.Entry<Integer, Listener<VH>> entry : listenersById.entrySet()) {
                    int id = entry.getKey();
                    final Listener<VH> listener = entry.getValue();

                    /**
                     * WARN:
                     *
                     * Performance bottleneck - a lot of calls to new
                     */

                    View.OnClickListener clickListener = new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            listener.onClick(viewHolder);
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

    }

    public ViewHolderBinderChainer<VH> addOnViewHolderClickListener(@IdRes int id, final Listener<VH> listener) {
        addListenerAndSwapBinder(id, listener);
        return this;
    }

    public ViewHolderBinderChainer<VH> addOnViewHolderClickListener(final Listener<VH> listener) {
        addListenerAndSwapBinder(NO_ID, listener);
        return this;
    }

    private void addListenerAndSwapBinder(@IdRes int id, final Listener<VH> listener) {
        listenersById.put(id, listener);

        if (listenersById.size() != 0){
            adapter.addViewHolderBinder(viewType, newBinder);
        }
    }

    public interface Listener<VH extends BaseViewAdapter.ViewHolder> {
        void onClick(VH viewHolder);
    }

}
