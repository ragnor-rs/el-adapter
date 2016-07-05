package com.m039.el_adapter.fragments;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m039.el_adapter.BaseViewHolderAdapter;
import com.m039.el_adapter.BaseViewHolder;
import com.m039.el_adapter.ViewHolderBinder;
import com.m039.el_adapter.ViewHolderCreator;
import com.m039.el_adapter.denis.BaseViewHolderBuilder;

import java.util.ArrayList;
import java.util.List;

import static android.util.Pair.create;

/**
 * Created by m039 on 6/1/16.
 */
public class DemosFragment extends DemoFragment {

    private static List<Pair<String, DemoFragment>> DEMOS = new ArrayList<>();

    static {
        DEMOS.add(create("Simple example",
                new SimpleDemoFragment()));
//        DEMOS.add(create("typeOfClass example",
//                new TypeOfClassDemoFragment()));
//        DEMOS.add(create("typeOfClass (wrapper) example",
//                new TypeOfClassWrapperDemoFragment()));
//        DEMOS.add(create("typeOfBind example",
//                new TypeOfBindDemoFragment()));
//        DEMOS.add(create("typeOfBind and typeOfClass example",
//                new TypesDemoFragment()));
//        DEMOS.add(create("clicks example",
//                new ClicksDemoFragment()));
    }

    @Override
    protected void showDemo(RecyclerView recycler) {
//        ListItemAdapter listAdapter = new ListItemAdapter();
//
//        listAdapter
//                .addViewCreator(Pair.class, parent -> new Button(getActivity()))
//                .addViewBinder((view, item) -> view.setText((String) item.first))
//                .addOnItemViewClickListener((view, item) -> {
//                    getFragmentManager()
//                            .beginTransaction()
//                            .replace(android.R.id.content, (Fragment) item.second)
//                            .addToBackStack(null)
//                            .commit();
//                });
//
//        listAdapter.addItems(DEMOS);
//
//        recycler.setAdapter(listAdapter);


        MyAdapter baseAdapter = new MyAdapter();

        baseAdapter.addViewHolderCreator(
                0,
                new ViewHolderCreator<BaseViewHolder<TextView>>() {
                    @Override
                    public BaseViewHolder<TextView> onCreateViewHolder(ViewGroup parent) {
                        return new BaseViewHolder<TextView>(new TextView(parent.getContext()));
                    }
                }
        ).addViewHolderBinder(new ViewHolderBinder<BaseViewHolder<TextView>>() {
            @Override
            public void onBindViewHolder(BaseViewHolder<TextView> viewHolder) {

            }
        });

        baseAdapter.addItem("one");
        baseAdapter.addItem("two");
        recycler.setAdapter(baseAdapter);
    }


    public static class MyAdapter extends BaseViewHolderAdapter<BaseViewHolderBuilder<?, ?>> {

        List<String> items = new ArrayList<>();

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        protected <V extends View, VH extends BaseViewHolder<V>> BaseViewHolderBuilder createBuilder(ViewHolderCreator<VH> creator) {
            return new BaseViewHolderBuilder<>(creator);
        }

        public void addItem(String one) {
            items.add(one);
            notifyDataSetChanged();
        }
    }
}
