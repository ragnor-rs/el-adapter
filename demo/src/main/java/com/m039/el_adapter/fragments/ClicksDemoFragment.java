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
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
                .addViewBinder((view, item) -> {
                    ((TextView) view.findViewById(R.id.first)).setText("->");
                    ((TextView) view.findViewById(R.id.second)).setText(item);
                    view.findViewById(R.id.divider).setBackgroundColor(Color.GREEN);
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
        ;


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
        Log.w("CLICK", Long.toString(System.currentTimeMillis()));
    }

}
