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

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.widget.Button;

import com.m039.el_adapter.ListItemAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.util.Pair.create;

/**
 * Created by m039 on 6/1/16.
 */
public class DemosFragment extends DemoFragment {

    private static List<Pair<String, DemoFragment>> DEMOS = new ArrayList<>();

    static {
        DEMOS.add(create("PerPageFragment",
                new PerPageFragment()));
        DEMOS.add(create("PerPageViewFragment",
                new PerPageViewFragment()));
        DEMOS.add(create("BaseViewHolderAdapter test",
                new BaseViewHolderAdapterDemoFragment()));
        DEMOS.add(create("BaseViewAdapter test",
                new BaseViewAdapterDemoFragment()));
        DEMOS.add(create("Simple example",
                new SimpleDemoFragment()));
        DEMOS.add(create("typeOfClass example",
                new TypeOfClassDemoFragment()));
        DEMOS.add(create("typeOfClass (wrapper) example",
                new TypeOfClassWrapperDemoFragment()));
        DEMOS.add(create("clicks example",
                new ClicksDemoFragment()));
    }

    @Override
    protected void showDemo(RecyclerView recycler) {
        ListItemAdapter listAdapter = new ListItemAdapter();

        listAdapter
                .addViewCreator(Pair.class, parent -> new Button(getActivity()))
                .addOnItemViewHolderClickListener((view, item) -> getFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, (Fragment) item.second)
                        .addToBackStack(null)
                        .commit())
                .addViewBinder((view, item) -> view.setText((String) item.first));

        listAdapter.addItems(DEMOS);

        recycler.setAdapter(listAdapter);
    }
}
