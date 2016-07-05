package com.m039.el_adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.m039.el_adapter.denis.BaseViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (savedInstanceState == null) {
//            getFragmentManager()
//                    .beginTransaction()
//                    .add(android.R.id.content, new DemosFragment())
//                    .commit();
//        }

        //todo move to BaseViewHolderAdapterTestFragment
        MyAdapter adapter = new MyAdapter();

        adapter.addItem("one");
        adapter.addItem("two");

        RecyclerView recycler = new RecyclerView(this);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        setContentView(recycler);
    }

    public static class MyAdapter extends BaseViewAdapter<BaseViewAdapter.BaseViewBuilder> {

        public static final int VIEW_TYPE_ONE = 0;

        List<String> items = new ArrayList<>();

        public MyAdapter() {

            addViewCreator(
                    VIEW_TYPE_ONE,
                    parent -> {

                        Log.i("DensTest", "onCreateView");
                        return new TextView(parent.getContext());
                    }
            )
                    .addViewBinder(view -> {
                        view.setText("ONE");
                        Log.i("DensTest", "onBindView");
                    });

        }

        @Override
        protected <V extends View, VH extends BaseViewHolder<V>> BaseViewBuilder createBuilder(ViewHolderCreator<VH> creator) {
            return new BaseViewBuilder<>(creator);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void addItem(String item) {
            items.add(item);
            notifyDataSetChanged();
        }
    }

//    public static class MyAdapter extends BaseViewHolderAdapter<BaseViewHolderBuilder> {
//
//        public static final int VIEW_TYPE = 0;
//        List<String> items = new ArrayList<>();
//
//        public MyAdapter() {
//
//            addViewHolderCreator(
//                    VIEW_TYPE,
//                    parent -> new BaseViewHolder<>(new TextView(parent.getContext()))
//            )
//                    .addViewHolderBinder(
//                            viewHolder -> {
//                                String item = items.get(viewHolder.getAdapterPosition());
//                                viewHolder.itemView.setText(item);
//                            }
//                    );
//        }
//
//        @Override
//        protected <V extends View, VH extends BaseViewHolder<V>> BaseViewHolderBuilder createBuilder(ViewHolderCreator<VH> creator) {
//            return new BaseViewHolderBuilder<>(creator);
//        }
//
//        @Override
//        public int getItemCount() {
//            return items.size();
//        }
//
//        public void addItem(String item) {
//            items.add(item);
//            notifyDataSetChanged();
//        }
//    }

}
