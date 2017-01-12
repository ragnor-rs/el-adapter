/*
 * Copyright (C) 2016 Dmitry Mozgin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.m039.el_adapter.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.m039.el_adapter.utils.NetworkUtils;

public class PagesSource {

    private static final int COUNT_PER_PAGE = 5;
    private boolean isLoading = false;
    private LoadingListener listener;
    private Context context;
    private Handler handler = new Handler(Looper.getMainLooper());
    private PageQuery query;

    public PagesSource(Context context) {
        this.context = context;
    }

    public void setListener(LoadingListener listener) {
        this.listener = listener;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public Page getNextPage() {
        query = FabricPageQuery.getQuery(query, COUNT_PER_PAGE);
        return MockGenerator.getPage(query.page, query.offset, query.count);
    }

    public void getDelayedNextPage() {
        if (!isLoading()) {
            //imitation of network error
            if (NetworkUtils.isNetworkAvailable(context)) {
                isLoading = true;
                notifyLoading();
                //make imitation of loading
                handler.postDelayed(() -> {
                    isLoading = false;
                    notifyResult(getNextPage());
                }, 500);
            } else {
                notifyError();
            }
        }
    }

    private boolean hasListener() {
        return listener != null;
    }

    private void notifyError() {
        if (hasListener()) {
            listener.onError();
        }
    }

    private void notifyLoading() {
        if (hasListener()) {
            listener.onLoading();
        }
    }

    private void notifyResult(Page page) {
        if (hasListener()) {
            listener.onResult(page);
        }
    }

    public interface LoadingListener {
        void onResult(Page page);

        void onLoading();

        void onError();
    }
}
