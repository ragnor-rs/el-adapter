package com.m039.el_adapter.perpage;

import android.support.annotation.Nullable;

import com.m039.el_adapter.ArrayListItemManager;
import com.m039.el_adapter.BaseViewHolder;
import com.m039.el_adapter.ItemManager;
import com.m039.el_adapter.ManagingAdapter;

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
 * <li>Per page data number is hardcoded to amount of {@link PerPageItemViewAdapter#LIMIT}</li>
 * </ul>
 */
public class PerPageItemViewAdapter extends ManagingAdapter {

    public static final int LIMIT = 20;

    @Nullable
    private PageLoader mPageLoader;

    public PerPageItemViewAdapter(@Nullable PageLoader pageLoader) {
        super(new ArrayListItemManager());
        mPageLoader = pageLoader;
    }

    public PerPageItemViewAdapter(@Nullable PageLoader pageLoader, ItemManager itemManager) {
        super(itemManager);
        mPageLoader = pageLoader;
    }

    public void setPageLoader(PageLoader pageLoader) {
        this.mPageLoader = pageLoader;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
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
