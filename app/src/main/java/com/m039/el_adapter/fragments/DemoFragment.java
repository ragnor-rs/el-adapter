package com.m039.el_adapter.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by m039 on 6/1/16.
 */
public abstract class DemoFragment extends Fragment {

    protected RecyclerView recycler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Activity activity = getActivity();

        recycler = new RecyclerView(activity);
        recycler.setLayoutManager(new LinearLayoutManager(activity));

        return recycler = recycler;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showDemo(recycler);
    }

    protected abstract void showDemo(RecyclerView recycler);

}
