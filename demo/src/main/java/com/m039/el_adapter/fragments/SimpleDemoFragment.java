package com.m039.el_adapter.fragments;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.m039.el_adapter.ListItemAdapter;

/**
 * Created by m039 on 6/1/16.
 */
public class SimpleDemoFragment extends DemoFragment {

    @Override
    protected void showDemo(RecyclerView recycler) {
        ListItemAdapter listAdapter = new ListItemAdapter();

        listAdapter
                .addViewCreator(Integer.class, parent -> new TextView(getActivity()))
                .addViewBinder((view, item) -> view.setText(String.valueOf(item)));

        for (int i = 0; i < 1000; i++) {
            listAdapter.addItem(i);
        }

        recycler.setAdapter(listAdapter);
    }

}
