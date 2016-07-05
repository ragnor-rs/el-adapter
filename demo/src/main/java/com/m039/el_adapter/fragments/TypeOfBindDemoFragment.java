//package com.m039.el_adapter.fragments;
//
//import android.support.v7.widget.RecyclerView;
//import android.widget.TextView;
//
//import com.m039.el_adapter.ListItemAdapter;
//
///**
// * Created by m039 on 6/1/16.
// */
//public class TypeOfBindDemoFragment extends DemoFragment {
//
//    @Override
//    protected void showDemo(RecyclerView recycler) {
//        ListItemAdapter listAdapter = new ListItemAdapter() {
//
//            @Override
//            protected int getTypeOfBind(int position) {
//                return position % 3;
//            }
//
//        };
//
//        listAdapter
//                .addViewCreator(Integer.class, parent -> new TextView(getActivity()))
//                .addViewBinder(0, (view, item) -> view.setText("0 -> " + item))
//                .addViewBinder(1, (view, item) -> view.setText("1 -> " + item))
//                .addViewBinder(2, (view, item) -> view.setText("2 -> " + item));
//
//        for (int i = 0; i < 1000; i++) {
//            listAdapter.addItem(i);
//        }
//
//        recycler.setAdapter(listAdapter);
//    }
//
//}
