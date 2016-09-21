package com.m039.el_adapter.perpage;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.m039.el_adapter.BaseViewHolder;
import com.m039.el_adapter.ItemManager;

import java.util.List;

/**
 * Created by m039 on 3/22/16.
 * <p>
 * Data is added via {@link PerPageWithFooterLoaderItemViewAdapter#addAll(List, boolean)} and per page loading will
 * continue untill addAll with hasNextPage = false is called.
 * <p>
 * Limits:
 * <ul>
 * <li>Only one item type is supported</li>
 * <li>Only list of items can be added</li>
 * </ul>
 */
public class PerPageWithFooterLoaderItemViewAdapter extends PerPageItemViewAdapter {

    protected static final int VIEW_TYPE_FOOTER = 0;
    private FooterLoaderWidget mFooterView;
    private boolean showingFooterLoader;

    public PerPageWithFooterLoaderItemViewAdapter(@Nullable PageLoader pageLoader, FooterLoaderWidget footerView) {
        super(pageLoader);
        this.mFooterView = footerView;

        addViewCreator(VIEW_TYPE_FOOTER, new ViewCreator<View>() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return mFooterView;
            }
        });
    }

    public PerPageWithFooterLoaderItemViewAdapter(PageLoader pageLoader, ItemManager itemManager, FooterLoaderWidget footerView) {
        super(pageLoader, itemManager);
        this.mFooterView = footerView;

        addViewCreator(VIEW_TYPE_FOOTER, new ViewCreator<View>() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return mFooterView;
            }
        });
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (!isFooterPosition(position)) {
            super.onBindViewHolder(holder, position);
        }
    }

    /**
     * Depending on items coming this method resulting in following view states:
     * <p>
     * <ul>
     * <li> Hide view loader if (isFirstPage) {view.showLoading(false)}
     * <li> Display emptyView if (isFirstPage && items == null)
     * <li> Display footerLoader if getItemsCount() != 0 && hasNextPage
     * <li> Hide footerLoader if getItemsCount() != 0 && !hasNextPage
     * </ul>
     *
     * @param items       - items to be displayed
     * @param hasNextPage - if there will be more pages
     */
    @CallSuper
    public <T> void addAll(@Nullable List<T> items, boolean hasNextPage) {
        int startPosition = this.getItemCount();

        if (items != null) {
            addItems(items);
            notifyItemRangeInserted(startPosition, items.size());
        }

        if (super.getItemCount() > 0) {
            showFooterLoader(hasNextPage);
        }
    }

    public void showNetworkError() {
        mFooterView.showNetworkError();
    }

    public void showEmptyView() {
        showFooterLoader(true);
        mFooterView.showEmptyView();
    }

    public void showFooterLoader(boolean show) {
        if (showingFooterLoader == show)
            return;

        showingFooterLoader = show;
        if (show) {
            notifyItemInserted(getFooterPosition());
        } else {
            notifyItemRemoved(getFooterPosition());
        }
    }

    public boolean isFooter(int position) {
        return getItemViewType(position) == VIEW_TYPE_FOOTER;
    }

    public int getFooterPosition() {
        return super.getItemCount();
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (showingFooterLoader ? 1 : 0);
    }

    private boolean isFooterPosition(int position) {
        return position == getFooterPosition();
    }

    @Override
    public int getItemViewType(int position) {
        return (showingFooterLoader && isFooterPosition(position)) ?
                VIEW_TYPE_FOOTER :
                super.getItemViewType(position);
    }

    @Override
    protected int getViewTypeOffset() {
        return 1;
    }
}
