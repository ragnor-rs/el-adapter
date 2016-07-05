//package com.m039.el_adapter.fragments;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.m039.el_adapter.ListItemAdapter;
//import com.m039.el_adapter.demo.R;
//
//import java.util.Arrays;
//
///**
// * Created by m039 on 6/10/16.
// */
//public class ClicksDemoFragment extends DemoFragment {
//
//    @Override
//    protected void showDemo(RecyclerView recycler) {
//        ListItemAdapter listAdapter = new ListItemAdapter();
//
//        listAdapter
//                .addViewCreator(String.class, parent -> {
//                    LayoutInflater inflater = (LayoutInflater) getActivity()
//                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                    return inflater.inflate(R.layout.two_items, parent, false);
//                })
//                .addViewBinder((view, item) -> {
//                    ((TextView) view.findViewById(R.id.first)).setText("->");
//                    ((TextView) view.findViewById(R.id.second)).setText(item);
//                })
//                .addOnItemViewClickListener((view, item) -> {
//                    toast("Click on whole item");
//                })
//                .addOnItemViewClickListener(R.id.first, (view, item) -> {
//                    toast("Click on first");
//                })
//                .addOnItemViewClickListener(R.id.second, (view, item) -> {
//                    toast("Click on second");
//                });
//
//        recycler.setAdapter(listAdapter);
//
//        listAdapter.addItems(Arrays.asList("One", "Two", "Three"));
//    }
//
//    private void toast(String message) {
//        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//    }
//
//}
