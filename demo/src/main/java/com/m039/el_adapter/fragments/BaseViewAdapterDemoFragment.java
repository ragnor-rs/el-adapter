package com.m039.el_adapter.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.m039.el_adapter.BaseViewAdapter;
import com.m039.el_adapter.BaseViewAdapter.BaseViewHelper;
import com.m039.el_adapter.BaseViewHolder;
import com.m039.el_adapter.BaseViewHolderAdapter;
import com.m039.el_adapter.views.BlueTextView;
import com.m039.el_adapter.views.GreenTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m039 on 6/1/16.
 */
public class BaseViewAdapterDemoFragment extends DemoFragment {

    private static final int ANOTHER_VIEW_TYPE = 1;
    private static final int GREEN_TEXT_WIDGET_RES_ID = 12;

    @SuppressWarnings("ResourceType")
    @Override
    protected void showDemo(RecyclerView recycler) {
        MyAdapter listAdapter = new MyAdapter();

        listAdapter
                .addViewCreator(
                        BaseViewHolderAdapter.DEFAULT_VIEW_TYPE,
                        parent -> new BlueTextView(parent.getContext())
                )
                .addViewClickListener(
                        (view, position) -> toast("blue widget clicked " + listAdapter.getItemAt(position))
                )
                .addViewLongClickListener(
                        (view, position) -> toast("blue widget long clicked " + listAdapter.getItemAt(position))
                )
                .addViewBinder((view, position) -> {
                    String item = listAdapter.getItemAt(position);
                    view.setText(item);
                });

        listAdapter
                .addViewCreator(
                        ANOTHER_VIEW_TYPE,
                        parent -> {
                            GreenTextView greenTextView = new GreenTextView(parent.getContext());
                            greenTextView.setId(GREEN_TEXT_WIDGET_RES_ID);
                            return greenTextView;
                        }
                )
                .addViewHolderClickListener(
                        GREEN_TEXT_WIDGET_RES_ID,
                        viewHolder1 -> toast("green widget clicked")
                )
                .addViewHolderClickListener(
                        GREEN_TEXT_WIDGET_RES_ID,
                        viewHolder1 -> toast("green widget clicked really " + listAdapter.getItemAt(viewHolder1.getAdapterPosition()))
                )
                .addViewHolderBinder(viewHolder -> {
                    String item = listAdapter.getItemAt(viewHolder.getAdapterPosition());
                    viewHolder.itemView.setText(item);
                });

        for (int i = 0; i < 1000; i++) {
            listAdapter.addItem(i);
        }

        recycler.setAdapter(listAdapter);
    }

    public static class MyAdapter extends BaseViewAdapter<BaseViewHelper> {

        List<Integer> items = new ArrayList<>();

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void addItem(int item) {
            items.add(item);
        }

        public String getItemAt(int position) {
            return Integer.toString(items.get(position));
        }

        @Override
        public int getItemViewType(int position) {
            return position % 2 == 0 ? ANOTHER_VIEW_TYPE : super.getItemViewType(position);
        }

        @Override
        protected <V extends View, VH extends BaseViewHolder<V>> BaseViewHelper<V> createBuilder(ViewHolderCreator<VH> creator) {
            return new BaseViewHelper(creator);
        }
    }

}
