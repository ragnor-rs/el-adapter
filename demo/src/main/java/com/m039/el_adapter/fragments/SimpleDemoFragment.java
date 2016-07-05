package com.m039.el_adapter.fragments;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.m039.el_adapter.BaseViewAdapter;
import com.m039.el_adapter.ListItemAdapter;
import com.m039.el_adapter.ViewHolderCreator;
import com.m039.el_adapter.denis.BaseAdapter;
import com.m039.el_adapter.denis.BaseAdapter.ViewHolder;
import com.m039.el_adapter.denis.ElBuilder;

/**
 * Created by m039 on 6/1/16.
 */
public class SimpleDemoFragment extends DemoFragment {

    @Override
    protected void showDemo(RecyclerView recycler) {
//        ListItemAdapter listAdapter = new ListItemAdapter();
//
//        listAdapter
//                .addViewCreator(Integer.class, parent -> new TextView(getActivity()))
//                .addViewBinder((view, item) -> view.setText(String.valueOf(item)));
//
//        for (int i = 0; i < 1000; i++) {
//            listAdapter.addItem(i);
//        }
//
//        recycler.setAdapter(listAdapter);

        BaseViewAdapter<ElBuilder<?, ?>> baseAdapter = new BaseViewAdapter<ElBuilder<?, ?>>() {
            @Override
            protected ElBuilder<?, ?> createBuilder(ViewHolderCreator<VH> creator) {
                return new ElBuilder<>();
            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };

        baseAdapter.addViewHolderCreator(
                1,
                parent -> new ViewHolder<>(new TextView(parent.getContext()))
        )
        .addViewHolderBinder(
                new BaseAdapter.ViewHolderBinder<ViewHolder<TextView>>() {
                    @Override
                    public void onBindViewHolder(ViewHolder<TextView> viewHolder) {

                    }
                }
        );
    }

}
