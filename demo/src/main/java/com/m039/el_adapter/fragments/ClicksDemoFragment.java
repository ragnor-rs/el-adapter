package com.m039.el_adapter.fragments;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.m039.el_adapter.ItemViewAdapter;
import com.m039.el_adapter.ListItemAdapter;
import com.m039.el_adapter.demo.R;

import java.util.Arrays;

/**
 * Created by m039 on 6/10/16.
 */
public class ClicksDemoFragment extends DemoFragment {

    @Override
    protected void showDemo(RecyclerView recycler) {
        ListItemAdapter listAdapter = new ListItemAdapter();

        listAdapter
                .addViewCreator(String.class, parent -> {
                    LayoutInflater inflater = (LayoutInflater) getActivity()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    return inflater.inflate(R.layout.two_items, parent, false);
                })
                .addOnItemViewClickListener((view, item) -> {
                    toast("Click on whole item");
                })
                .addOnItemViewClickListener(R.id.first, (view, item) -> {
                    toast("Click on first");
                })
                .addOnItemViewClickListener(R.id.second, (view, item) -> {
                    toast("Click on second");
                })

                .addViewBinder((view, item) -> {
                    ((TextView) view.findViewById(R.id.first)).setText("->");
                    ((TextView) view.findViewById(R.id.second)).setText(item);
                    view.findViewById(R.id.divider).setBackgroundColor(Color.GREEN);
                });


        listAdapter
                .addViewCreator(Integer.class, parent -> {
                    LayoutInflater inflater = (LayoutInflater) getActivity()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    return inflater.inflate(R.layout.two_items, parent, false);
                })
                .addOnItemViewClickListener((view, item) -> {
                    toast("Click on whole item");
                })
                .addViewBinder((view, item) -> {
                    ((TextView) view.findViewById(R.id.first)).setText("->");
                    ((TextView) view.findViewById(R.id.second)).setText(Integer.toString(item));
                    view.findViewById(R.id.divider).setBackgroundColor(Color.CYAN);
                });

        recycler.setAdapter(listAdapter);

        listAdapter.addItems(Arrays.asList("One", 1, "Two", 2, "Three", 3));
    }

    private void toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
