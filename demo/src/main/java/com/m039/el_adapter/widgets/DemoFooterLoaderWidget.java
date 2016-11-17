package com.m039.el_adapter.widgets;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;

import com.m039.el_adapter.demo.R;
import com.m039.el_adapter.perpage.FooterLoaderWidget;
import com.m039.el_adapter.utils.NetworkUtils;

public class DemoFooterLoaderWidget extends FooterLoaderWidget {

    View networkError;
    View progress;

    @Nullable
    private OnClickListener retryClickListener;

    /**
     * Layout must contain view with id = networ_error
     * This widget will handled as Network error stub
     * and retry will be called on click
     */
    public DemoFooterLoaderWidget(Context context, @LayoutRes int layoutResId) {
        super(context);
        init(layoutResId);
    }

    public DemoFooterLoaderWidget(Context context, AttributeSet attrs, @LayoutRes int layoutResId) {
        super(context, attrs);
        init(layoutResId);
    }

    private void init(@LayoutRes int layoutResId) {
        inflate(getContext(), layoutResId, this);
        networkError = findViewById(R.id.network_error);
        progress = findViewById(R.id.progress);

        networkError.setOnClickListener(v -> {
            if (retryClickListener != null && NetworkUtils.isNetworkAvailable(getContext())) {
                retryClickListener.onClick(v);
                showState(State.LOADING);
            }
        });

        showState(State.LOADING);
    }

    @Override
    public void showState(State state) {
        switch (state) {
            case LOADING:
                progress.setVisibility(VISIBLE);
                networkError.setVisibility(GONE);
                break;
            case HIDDEN:
                progress.setVisibility(GONE);
                networkError.setVisibility(GONE);
                break;
            case ERROR:
                progress.setVisibility(GONE);
                networkError.setVisibility(VISIBLE);
                break;
        }
    }

    @Override
    public void setMessage(@StringRes int message) {

    }

    public void setOnRetryClickListener(OnClickListener retryClickListener) {
        this.retryClickListener = retryClickListener;
    }
}
