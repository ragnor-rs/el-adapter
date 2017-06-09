/*
 * Copyright (C) 2016 Dmitry Mozgin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.m039.el_adapter.perpage;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
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
@SuppressWarnings("unused")
public class PerPageWithFooterLoaderItemViewAdapter extends PerPageItemViewAdapter {

    protected static final int VIEW_TYPE_FOOTER = Integer.MAX_VALUE;
    private boolean showingFooterLoader;

    @Nullable
    private volatile FooterLoaderWidget mFooterView;
    private PageLoader pageLoader;

    public PerPageWithFooterLoaderItemViewAdapter(
            @Nullable PageLoader pageLoader,
            @Nullable FooterLoaderWidget footerView
    ) {
        super(pageLoader);

        if (footerView != null) {
            setFooterView(footerView);
        }
    }

    public PerPageWithFooterLoaderItemViewAdapter(
            @Nullable PageLoader pageLoader,
            @Nullable FooterLoaderWidget footerView,
            ItemManager itemManager
    ) {
        super(pageLoader, itemManager);

        if (footerView != null) {
            setFooterView(footerView);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void setFooterView(@Nullable FooterLoaderWidget footerView) {
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

    @SuppressWarnings("WeakerAccess")
    public void showFooterLoader(boolean show) {

        if (show != isShowingFooterLoader()) {
            if (show) {
                notifyItemInserted(getFooterPosition());
            } else {
                notifyItemRemoved(getFooterPosition());
            }

            showingFooterLoader = show;
        }

    }

    public void setFooterMessage(@StringRes int message) {
        mFooterView.setMessage(message);
    }

    @SuppressWarnings({"ConstantConditions", "WeakerAccess"})
    public void setFooterState(FooterLoaderWidget.State state) {
        if (mFooterView == null) {
            throw new IllegalArgumentException("setFooterWidget before calling this method");
        }

        switch (state) {
            case LOADING:
                showFooterLoader(true);

                break;
            case HIDDEN:
                showFooterLoader(false);
                break;

            case ERROR:
                showFooterLoader(true);
                break;

        }

        mFooterView.showState(state);
    }

    public boolean isFooter(int position) {
        return getItemViewType(position) == VIEW_TYPE_FOOTER;
    }

    /**
     *
     * @return footer position or -1 of footer is not showing
     */
    @SuppressWarnings("WeakerAccess")
    public int getFooterPosition() {
        return isShowingFooterLoader() ? super.getItemCount() : -1;
    }

    public boolean isShowingFooterLoader() {
        return showingFooterLoader;
    }

    /**
     * @return items count without considering footer loader
     */
    public int getItemCountWithoutFooter() {
        return super.getItemCount() - (isShowingFooterLoader() ? 1 : 0);
    }

    /**
     * @deprecated use {@link PerPageWithFooterLoaderItemViewAdapter#isFooter(int)}
     */
    @Deprecated
    private boolean isFooterPosition(int position) {
        return position == getFooterPosition();
    }

    @Override
    public int getItemViewType(int position) {
        return (isShowingFooterLoader() && isFooter(position)) ?
                VIEW_TYPE_FOOTER :
                super.getItemViewType(position);
    }

    @Override
    protected int getViewTypeOffset() {
        return 1;
    }

}
