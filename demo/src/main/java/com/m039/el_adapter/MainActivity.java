package com.m039.el_adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.m039.el_adapter.fragments.DemosFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, new DemosFragment())
                    .commit();
        }
    }

}
