package com.m039.el_adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.m039.el_adapter.denis.ElBuilder;

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

    public static class MyAdapter extends BaseViewAdapter<ElBuilder> {

        public static final int VIEW_TYPE = 0;
        List<String> items = new ArrayList<>();

        public MyAdapter() {

            addViewHolderCreator(
                    VIEW_TYPE,
                    parent -> new BaseViewHolder<>(new TextView(parent.getContext()))
            )
                    .addViewHolderBinder(
                            viewHolder -> {
                                String item = items.get(viewHolder.getAdapterPosition());
                                viewHolder.itemView.setText(item);
                            }
                    );
        }

        @Override
        protected <V extends View, VH extends BaseViewHolder<V>> ElBuilder createBuilder(ViewHolderCreator<VH> creator) {
            return new ElBuilder<>(creator);
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

}
