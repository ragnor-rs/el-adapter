package com.m039.el_adapter.fragments;

import android.support.v7.widget.RecyclerView;

import com.m039.el_adapter.ItemViewAdapter;
import com.m039.el_adapter.ListItemAdapter;
import com.m039.el_adapter.views.BlueTextView;
import com.m039.el_adapter.views.GreenTextView;
import com.m039.el_adapter.views.RedTextView;

/**
 * Created by m039 on 6/1/16.
 */
public class TypeOfClassWrapperDemoFragment extends TypeOfClassDemoFragment {

    private static class IntegerWrapper {

        int value;
        int typeOfClass;

        public IntegerWrapper(int value, int typeOfClass) {
            this.value = value;
            this.typeOfClass = typeOfClass;
        }

    }

    @Override
    protected void showDemo(RecyclerView recycler) {
        ListItemAdapter listAdapter = new ListItemAdapter() {

            @Override
            protected int getTypeOfClass(int position) {
                Object object = getItemAt(position);
                if (object instanceof IntegerWrapper) {
                    return ((IntegerWrapper) object).typeOfClass;
                } else {
                    return super.getTypeOfClass(position);
                }
            }
        };

        listAdapter
                .addViewCreator(IntegerWrapper.class, 0, parent -> new RedTextView(getActivity()))
                .addItemViewBinder((view, item) -> view.setText(String.valueOf(item.value)));

        listAdapter
                .addViewCreator(IntegerWrapper.class, 1, parent -> new GreenTextView(getActivity()))
                .addItemViewBinder((view, item) -> view.setText(String.valueOf(item.value)));

        listAdapter
                .addViewCreator(IntegerWrapper.class, 2, parent -> new BlueTextView(getActivity()))
                .addItemViewBinder((view, item) -> view.setText(String.valueOf(item.value)));

        for (int i = 0; i < 1000; i++) {
            listAdapter.addItem(new IntegerWrapper(i, i % 3));
        }

        recycler.setAdapter(listAdapter);
    }
}
