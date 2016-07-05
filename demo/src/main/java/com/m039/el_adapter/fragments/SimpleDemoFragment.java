package com.m039.el_adapter.fragments;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.m039.el_adapter.BaseViewAdapter;
import com.m039.el_adapter.BaseViewHolder;
import com.m039.el_adapter.ViewHolderCreator;
import com.m039.el_adapter.denis.ElBuilder;

import java.util.ArrayList;
import java.util.List;

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

        MyAdapter baseAdapter = new MyAdapter();

                baseAdapter.addViewHolderCreator(
                0,
                parent -> {
                    TextView textView = new TextView(parent.getContext());
                    textView.setBackgroundColor(Color.CYAN);
                    return new BaseViewHolder<>(textView);
                }
        );

        Log.i("DensTest", "one");
        baseAdapter.addItem("one");
        baseAdapter.addItem("two");
        baseAdapter.notifyDataSetChanged();
        recycler.setAdapter(baseAdapter);
    }

    public static class MyAdapter extends BaseViewAdapter<ElBuilder<?, ?>> {

        List<String> items = new ArrayList<>();

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        protected <V extends View, VH extends BaseViewHolder<V>> ElBuilder createBuilder(ViewHolderCreator<VH> creator) {
            return new ElBuilder<>(creator);
        }

        public void addItem(String item) {
            items.add(item);
        }
    }


}
