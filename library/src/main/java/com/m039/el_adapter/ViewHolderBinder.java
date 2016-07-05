package com.m039.el_adapter;

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
