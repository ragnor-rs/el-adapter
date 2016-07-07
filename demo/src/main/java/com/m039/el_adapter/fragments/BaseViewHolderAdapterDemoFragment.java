package com.m039.el_adapter.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.m039.el_adapter.BaseViewHolder;
import com.m039.el_adapter.BaseViewHolderAdapter;
import com.m039.el_adapter.BaseViewHolderBuilder;
import com.m039.el_adapter.views.BlueTextView;
import com.m039.el_adapter.views.GreenTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m039 on 6/1/16.
 */
public class BaseViewHolderAdapterDemoFragment extends DemoFragment {

    private static final int ANOTHER_VIEW_TYPE = 1;
    private static final int GREEN_TEXT_WIDGET_RES_ID = 12;

    @SuppressWarnings("ResourceType")
    @Override
    protected void showDemo(RecyclerView recycler) {
        MyAdapter listAdapter = new MyAdapter();

        listAdapter
                .addViewHolderCreator(
                        BaseViewHolderAdapter.DEFAULT_VIEW_TYPE,
                        parent -> new BaseViewHolder<>(new BlueTextView(parent.getContext()))
                )
                .addViewHolderClickListener(
                        viewHolder -> Toast.makeText(getActivity(), "Blue widget clicked", Toast.LENGTH_SHORT).show()
                )
                .addViewHolderBinder(viewHolder -> {
                    Integer item = listAdapter.getItemAt(viewHolder.getAdapterPosition());
                    viewHolder.itemView.setText(Integer.toString(item));
                });

        listAdapter
                .addViewHolderCreator(
                        ANOTHER_VIEW_TYPE,
                        parent -> {
                            GreenTextView greenTextView = new GreenTextView(parent.getContext());
                            greenTextView.setId(GREEN_TEXT_WIDGET_RES_ID);
                            return new BaseViewHolder<>(greenTextView);
                        }
                )
                .addViewHolderClickListener(
                        GREEN_TEXT_WIDGET_RES_ID,
                        viewHolder1 -> Toast.makeText(getActivity(), "green widget clicked", Toast.LENGTH_SHORT).show()
                )
                .addViewHolderClickListener(
                        GREEN_TEXT_WIDGET_RES_ID,
                        viewHolder1 -> Toast.makeText(getActivity(), "green widget clicked really", Toast.LENGTH_SHORT).show()
                )
                .addViewHolderBinder(viewHolder -> {
                    Integer item = listAdapter.getItemAt(viewHolder.getAdapterPosition());
                    viewHolder.itemView.setText(Integer.toString(item));
                });

        for (int i = 0; i < 1000; i++) {
            listAdapter.addItem(i);
        }

        recycler.setAdapter(listAdapter);
    }

    public static class MyAdapter extends BaseViewHolderAdapter<BaseViewHolderBuilder> {

        List<Integer> items = new ArrayList<>();

        @Override
        protected <V extends View, VH extends BaseViewHolder<V>> BaseViewHolderBuilder createBuilder(ViewHolderCreator<VH> creator) {
            return new BaseViewHolderBuilder<>(creator);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void addItem(int item) {
            items.add(item);
        }

        public Integer getItemAt(int position) {
            return items.get(position);
        }

        @Override
        public int getItemViewType(int position) {
            return position % 2 == 0 ? ANOTHER_VIEW_TYPE : super.getItemViewType(position);
        }
    }

}
