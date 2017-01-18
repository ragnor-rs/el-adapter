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

package com.m039.el_adapter.perpage;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by defuera on 07/07/2016.
 */
public abstract class FooterLoaderWidget extends FrameLayout {

    public FooterLoaderWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FooterLoaderWidget(Context context) {
        super(context);
    }

    public abstract void showState(State state);

    /**
     * @param errorMessage message to be displayed on state == ERROR
     */
    public void setErrorMessage(String errorMessage) {
    }

    public enum State {
        LOADING, HIDDEN, ERROR
    }

    public abstract void setMessage(@StringRes int message);
}
