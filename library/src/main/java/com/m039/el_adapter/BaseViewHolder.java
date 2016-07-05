package com.m039.el_adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by defuera on 05/07/2016.
 */
public class BaseViewHolder<V extends View> extends RecyclerView.ViewHolder {

    public V itemView; // parameterize

    public BaseViewHolder(V itemView) {
        super(itemView);
        this.itemView = itemView;
    }

}
