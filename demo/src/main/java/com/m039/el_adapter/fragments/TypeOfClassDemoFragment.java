//package com.m039.el_adapter.fragments;
//
//import android.app.Activity;
//import android.support.v7.widget.RecyclerView;
//
//import com.m039.el_adapter.ListItemAdapter;
//import com.m039.el_adapter.views.BlueTextView;
//import com.m039.el_adapter.views.GreenTextView;
//import com.m039.el_adapter.views.RedTextView;
//
///**
// * Created by m039 on 6/1/16.
// */
//public class TypeOfClassDemoFragment extends DemoFragment {
//
//    @Override
//    protected void showDemo(RecyclerView recycler) {
//        ListItemAdapter listAdapter = new ListItemAdapter() {
//
//            @Override
//            protected int getTypeOfClass(int position) {
//                return position % 3;
//            }
//
//        };
//
//        Activity activity = getActivity();
//
//        listAdapter
//                .addViewCreator(Integer.class, 0, parent -> new RedTextView(activity))
//                .addViewBinder((view, item) -> view.setText(String.valueOf(item)));
//
//        listAdapter
//                .addViewCreator(Integer.class, 1, parent -> new BlueTextView(activity))
//                .addViewBinder((view, item) -> view.setText(String.valueOf(item)));
//
//        listAdapter
//                .addViewCreator(Integer.class, 2, parent -> new GreenTextView(activity))
//                .addViewBinder((view, item) -> view.setText(String.valueOf(item)));
//
//        for (int i = 0; i < 1000; i++) {
//            listAdapter.addItem(i);
//        }
//
//        recycler.setAdapter(listAdapter);
//    }
//}
