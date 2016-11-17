package com.m039.el_adapter.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.m039.el_adapter.ListItemAdapter;
import com.m039.el_adapter.demo.R;
import com.m039.el_adapter.model.Page;
import com.m039.el_adapter.model.PagesSource;
import com.m039.el_adapter.perpage.PerPageItemViewAdapter;

/**
 * Created by 4xes on 11/11/16.
 */
public class PerPageFragment extends DemoFragment implements PagesSource.LoadingListener, PerPageItemViewAdapter.PageLoader{

    private PerPageItemViewAdapter listAdapter;
    private PagesSource pagesSource;

    @Override
    protected void showDemo(RecyclerView recycler) {

        pagesSource = new PagesSource(getActivity());
        pagesSource.setListener(this);


        listAdapter = new PerPageItemViewAdapter(this, new ListItemAdapter());

        listAdapter
            .addViewCreator(Integer.class, parent -> {
                LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                return inflater.inflate(R.layout.page_header_item, parent, false);
            })
            .addViewBinder((view, item) -> {
                ((TextView) view).setText(String.format("Page %d", item));
            });

        listAdapter
            .addViewCreator(String.class, parent -> {
                LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                return inflater.inflate(R.layout.page_text_item, parent, false);
            })
            .addViewBinder((view, item) -> {
                ((TextView) view).setText(item);
            })
        ;
        recycler.setAdapter(listAdapter);


        addPage(pagesSource.getNextPage());
    }

    public void addPage(Page page){
        int countItems = listAdapter.getItemCount();
        listAdapter.addItem(page.num);
        listAdapter.addItems(page.items);
        listAdapter.notifyItemRangeInserted(countItems, countItems + page.items.size() + 1);
    }

    @Override
    public void onResult(Page page) {
        addPage(page);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onError() {

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
