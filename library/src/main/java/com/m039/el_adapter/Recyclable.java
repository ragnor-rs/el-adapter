package com.m039.el_adapter;

/**
 * Created by m039 on 10/4/16.
 */

public interface Recyclable {

    /**
     * This method is called in RecyclerView.onViewRecycled
     */
    void recycle();

}
