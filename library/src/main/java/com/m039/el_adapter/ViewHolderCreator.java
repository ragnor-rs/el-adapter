package com.m039.el_adapter;

import android.view.ViewGroup;

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
