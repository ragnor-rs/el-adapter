package com.m039.el_adapter.denis;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.m039.el_adapter.BaseViewHolder;
import com.m039.el_adapter.ViewHolderBinder;
import com.m039.el_adapter.ViewHolderCreator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by defuera on 05/07/2016.
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.ViewHolder> implements IBaseAdapter {

    private final Map<Integer, ElBuilder<?, ?>> builderMap = new HashMap<>();

    @Override
    public <V extends View, VH extends BaseAdapter.ViewHolder<V>>
    ElBuilder<V, VH>.ViewHolderBinderChainer addViewHolderCreator(int viewType, ViewHolderCreator<VH> creator) {
        ElBuilder<V, VH> elBuilder = new ElBuilder<>(creator);
        builderMap.put(viewType, elBuilder);
        return elBuilder.viewHolderBinderChainer();
    }

//    public interface ViewHolderCreator<VH extends BaseViewHolder> {
//
//        /**
//         * @param parent parent of a new view
//         * @return should be a new created viewHolder
//         */
//        VH onCreateViewHolder(ViewGroup parent);
//
//    }
//
//    public interface ViewHolderBinder<VH extends BaseViewHolder> {
//
//        /**
//         * To get position call viewHolder.getAdapterPosition()
//         *
//         * @param viewHolder viewHolder to bind
//         */
//        void onBindViewHolder(VH viewHolder);
//
//    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderCreator viewHolderCreator = builderMap.get(viewType).getViewHolderCreator();

        if (viewHolderCreator == null) {
            throw new IllegalStateException("Can't create view of type " + viewType + ".");
        }

        return viewHolderCreator.onCreateViewHolder(parent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        ViewHolderBinder viewHolderBinder = builderMap.get(getItemViewType(position)).getViewHolderBinder();

        if (viewHolderBinder != null) {
            viewHolderBinder.onBindViewHolder(holder);
        } else {
            // do nothing, don't bind a thing
        }
    }

}
