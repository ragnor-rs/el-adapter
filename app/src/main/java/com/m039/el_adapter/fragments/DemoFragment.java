package com.m039.el_adapter.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.m039.el_adapter.DemoRecyclerView;

/**
 * Created by m039 on 6/1/16.
 */
public abstract class DemoFragment extends Fragment {

    protected DemoRecyclerView recycler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return recycler = new DemoRecyclerView(getActivity());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showDemo(recycler);
    }

    protected abstract void showDemo(RecyclerView recycler);
}
