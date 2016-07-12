package com.m039.el_adapter.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by defuera on 04/07/2016.
 */

public class SimpleTestActivity extends Activity {

    public static final int RECYCLER_VIEW_ID = 100;
    private RecyclerView recyclerView;

    @SuppressWarnings("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setId(RECYCLER_VIEW_ID);

        setContentView(recyclerView);
    }
}
