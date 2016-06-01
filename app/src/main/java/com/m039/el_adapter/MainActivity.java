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
    ListItemAdapter mListItemAdapter = new ListItemAdapter() {

        @Override
        protected int getTypeOfBind(int position) {
            Object object = getItemAt(position);

            if (object instanceof Title) {
                Title title = (Title) object;

                if (title.takedown) {
                    return 1;
                } else {
                    return 0;
                }
            }

            return super.getTypeOfBind(position);
        }

    };

    private static class Title {

        String title;
        boolean takedown;

        public Title(String title, boolean takedown) {
            this.title = title;
            this.takedown = takedown;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(mRecyclerView = new DemoRecyclerView(this));

        mRecyclerView.setAdapter(mListItemAdapter);

        mListItemAdapter
                .addViewCreator(Title.class, parent -> new TextView(this))
                .addViewBinder(0, (view, item) -> view.setText(item.title + " - "))
                .addViewBinder(1, (view, item) -> view.setText(item.title + " + "));

        mListItemAdapter.addItem(new Title("Not takedown", false));
        mListItemAdapter.addItem(new Title("Takedown", true));

        // sample for viewTypeOfClass/typeOfClass, viewTypeOfBind/typeOfBind
//        // todo sample (with wrapper, with no wrapper)
//        mListItemAdapter
//                .addViewCreator(Long.class, parent -> new TextView(this))
//                .addViewBinder(0, null)
//                .addViewBinder(1, null);

        // todo custom ViewHolder typization
        mListItemAdapter
                .addViewHolderCreator(Long.class, parent -> new ViewHolder<>(new TextView(this)))
                .addViewBinder(((viewHolder, item) -> viewHolder.setText("1")));

        mListItemAdapter
                .addViewCreator(Long.class, parent -> new TextView(this))
                .addViewBinder((view, item) -> view.setText(String.valueOf(item)))
                .addOnItemViewClickListener((view, item) -> {
                    Toast.makeText(this, "Long click on " + item, Toast.LENGTH_SHORT).show();
                });

        mListItemAdapter
                .addViewCreator(String.class, parent -> new Button(this))
                .addViewHolderBinder(((viewHolder, item) -> {
                    viewHolder.itemView.setText(item);
                    viewHolder.itemView.setOnClickListener(view -> onButtonClick(viewHolder));
                }));

        mListItemAdapter
                .addViewCreator(Integer.class, parent -> new TextView(this))
                .addViewBinder((view, item) -> {
                    view.setText(String.valueOf(item));
                });

        mListItemAdapter.addItems(EXAMPLES);
        mListItemAdapter.addItem(42);
        mListItemAdapter.addItem(13L);
        mListItemAdapter.notifyDataSetChanged();
    }

    void onButtonClick(ViewHolder<Button> viewHolder) {
        Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
    }

}
