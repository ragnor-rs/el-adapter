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

package com.m039.el_adapter.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.m039.el_adapter.ListItemAdapter;
import com.m039.el_adapter.demo.R;
import com.m039.el_adapter.model.Page;
import com.m039.el_adapter.model.PagesSource;
import com.m039.el_adapter.perpage.FooterLoaderWidget;
import com.m039.el_adapter.perpage.PerPageItemViewAdapter;
import com.m039.el_adapter.perpage.PerPageWithFooterLoaderItemViewAdapter;
import com.m039.el_adapter.widgets.MyFooterLoaderWidget;

/**
 * Created by 4xes on 11/11/16.
 */
public class PerPageViewFragment extends DemoFragment implements PagesSource.LoadingListener, PerPageItemViewAdapter.PageLoader {

    private PerPageWithFooterLoaderItemViewAdapter listAdapter;
    private PagesSource pagesSource;
    private MyFooterLoaderWidget footerLoaderWidget;

    @Override
    protected void showDemo(RecyclerView recycler) {

        pagesSource = new PagesSource(getActivity());
        pagesSource.setListener(this);

        footerLoaderWidget = new MyFooterLoaderWidget(getActivity());
        footerLoaderWidget.setOnRetryClickListener(v -> pagesSource.getDelayedNextPage());

        listAdapter = new PerPageWithFooterLoaderItemViewAdapter(this, footerLoaderWidget, new ListItemAdapter());

        listAdapter.addViewCreator(Integer.class, parent -> {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            return inflater.inflate(R.layout.page_header_item, parent, false);
        }).addViewBinder((view, item) -> {
            ((TextView) view).setText(String.format("Page %d", item));
        });

        listAdapter.addViewCreator(String.class, parent -> {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            return inflater.inflate(R.layout.page_text_item, parent, false);
        }).addViewBinder((view, item) -> {
            ((TextView) view).setText(item);
        });
        recycler.setAdapter(listAdapter);

        addPage(pagesSource.getNextPage());
    }

    public void addPage(Page page) {
        int countItems = listAdapter.getItemCount();
        listAdapter.addItem(page.num);
        listAdapter.addItems(page.items);
        listAdapter.notifyItemRangeInserted(countItems, countItems + page.items.size() + 1);
    }

    @Override
    public void onResult(Page page) {
        addPage(page);
        listAdapter.setFooterState(FooterLoaderWidget.State.HIDDEN);
    }

    @Override
    public void onLoading() {
        recycler.post(() -> listAdapter.setFooterState(FooterLoaderWidget.State.LOADING));
    }

    @Override
    public void onError() {
        recycler.post(() -> listAdapter.setFooterState(FooterLoaderWidget.State.ERROR));
    }

    @Override
    public void startPageLoading() {
        pagesSource.getDelayedNextPage();
    }

    @Override
    public boolean isPageLoading() {
        return pagesSource.isLoading();
    }
}
