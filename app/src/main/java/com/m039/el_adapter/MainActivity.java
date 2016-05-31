package com.m039.el_adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.m039.el_adapter.BaseViewAdapter.ViewHolder;

public class MainActivity extends AppCompatActivity {

    static final String EXAMPLES[] = {
            "Simple"
    };

    RecyclerView mRecyclerView;
    ItemViewAdapter mItemViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(mRecyclerView = new DemoRecyclerView(this));

        mRecyclerView.setAdapter(mItemViewAdapter = new ItemViewAdapter());

        mItemViewAdapter
                .addViewCreator(String.class, parent -> new Button(this))
                .addViewHolderBinder(String.class, ((viewHolder, item) -> {
                    viewHolder.itemView.setText(item);
                    viewHolder.itemView.setOnClickListener(view -> onButtonClick(viewHolder));
                }))
                .addViewCreator(Integer.class, parent -> new TextView(this))
                .addViewBinder(Integer.class, (view, item) -> {
                    view.setText(String.valueOf(item));
                });

        mItemViewAdapter.addItems(EXAMPLES);
        mItemViewAdapter.addItem(42);
        mItemViewAdapter.notifyDataSetChanged();
    }

    void onButtonClick(ViewHolder<Button> viewHolder) {
        Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
    }

}
