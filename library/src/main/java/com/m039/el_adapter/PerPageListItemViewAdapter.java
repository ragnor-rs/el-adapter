package com.m039.el_adapter;

import android.support.annotation.Nullable;

/**
 * Created by m039 on 3/22/16.
 *
 * This class provides convenient way of implementing per page data loading.
 *
 * <p>
 * Adapter must be initialized with <code>PageLoader</code>.
 * </p>
 *
 * Limits:
 *
 * <ul>
 * <li>Per page data number is hardcoded to amount of {@link PerPageListItemViewAdapter#LIMIT}</li>
 * </ul>
 */
public class PerPageListItemViewAdapter extends ListItemViewAdapter {

    public static final int LIMIT = 20;

    @Nullable
    private final PageLoader mPageLoader;

    public PerPageListItemViewAdapter(@Nullable PageLoader pageLoader) {
        mPageLoader = pageLoader;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (mPageLoader != null && timeToLoadMore(position) && !mPageLoader.isPageLoading()) {
            mPageLoader.startPageLoading();
        }
    }

    private boolean timeToLoadMore(int lastItemPosition) {
        return getItemCount() - LIMIT / 2 <= lastItemPosition;
    }

    /**
     * Interface for page loading
     */
    public interface PageLoader {

        /**
         * This method will be called every time at the end of the list
         */
        void startPageLoading();

        /**
         * @return true if request is executing
         */
        boolean isPageLoading();

    }

}
