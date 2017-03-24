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

package com.m039.el_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m039.el_adapter.perpage.FooterLoaderWidget;
import com.m039.el_adapter.perpage.PerPageItemViewAdapter;
import com.m039.el_adapter.perpage.PerPageWithFooterLoaderItemViewAdapter;
import com.m039.el_adapter.view.SimpleTestActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;

/**
 * Created by 4xes on 17/11/2016.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class FooterLoaderWidgetTest {

    private RecyclerView recycler;
    private PerPageWithFooterLoaderItemViewAdapter testAdapter;

    @Mock
    private PerPageItemViewAdapter.PageLoader pageLoader;

    @Mock
    private FooterLoaderWidget footerLoaderWidget;

    private Entity[] testEntities;

    public static Entity[] createEntities(int size) {
        Entity[] elEntities = new Entity[size];
        for (int i = 0; i < size; i++) {
            elEntities[i] = new Entity(String.valueOf(i));
        }
        return elEntities;
    }

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("ResourceType")
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        SimpleTestActivity activity = Robolectric.buildActivity(SimpleTestActivity.class)
                .create()
                .resume()
                .get();

        recycler = (RecyclerView) activity.findViewById(SimpleTestActivity.RECYCLER_VIEW_ID);
        testAdapter = new PerPageWithFooterLoaderItemViewAdapter(pageLoader, footerLoaderWidget, new ListItemAdapter());
        recycler.setAdapter(testAdapter);

        testAdapter.addViewHolderCreator(Entity.class, new BaseViewHolderAdapter.ViewHolderCreator<BaseViewHolder<TestWidget>>() {
            @Override
            public BaseViewHolder<TestWidget> onCreateViewHolder(ViewGroup parent) {
                return new BaseViewHolder<>(new TestWidget(parent.getContext()));
            }
        }).addViewHolderBinder(new ItemViewAdapter.ItemViewHolderBinder<Entity, TestWidget>() {
            @Override
            public void onBindViewHolder(BaseViewHolder<TestWidget> viewHolder, Entity item) {
                viewHolder.getItemView().setText(item.id);
            }
        });

        testEntities = createEntities(5);
    }

    @Test
    public void testStates() {
        assertNotNull(recycler.getAdapter());

        Entity[] entities = Arrays.copyOfRange(testEntities, 0, 5);
        testAdapter.addItems(entities);
        onItemsChanged();

        testAdapter.setFooterState(FooterLoaderWidget.State.HIDDEN);
        verify(footerLoaderWidget).showState(FooterLoaderWidget.State.HIDDEN);

        int lastPosition = testAdapter.getItemCount() - 1;
        Entity lastInsertEntity = entities[entities.length - 1];

        //test item have to be TestWidget
        Assert.assertEquals(TestWidget.class, recycler.getChildAt(lastPosition).getClass());
        //test penultimate item must equals last insert entity
        Assert.assertEquals(lastInsertEntity.id, ((TextView) recycler.getChildAt(lastPosition)).getText().toString());

        testAdapter.setFooterState(FooterLoaderWidget.State.ERROR);
        verify(footerLoaderWidget).showState(FooterLoaderWidget.State.ERROR);

        lastPosition = testAdapter.getItemCount() - 1;
        //test penultimate item have to be TestWidget
        Assert.assertEquals(TestWidget.class, recycler.getChildAt(lastPosition - 1).getClass());
        //test penultimate item must equals last insert entity
        Assert.assertEquals(lastInsertEntity.id, ((TextView) recycler.getChildAt(lastPosition - 1)).getText().toString());

        //test last item item have to be FooterLoaderWidget
        //TODO why npe?
        //Assert.assertEquals(FooterLoaderWidget.class, recycler.getChildAt(lastPosition).getClass());

        testAdapter.setFooterState(FooterLoaderWidget.State.LOADING);
        verify(footerLoaderWidget).showState(FooterLoaderWidget.State.LOADING);

        @StringRes int messageRes = 1;
        testAdapter.setFooterMessage(messageRes);
        verify(footerLoaderWidget).setMessage(messageRes);
    }

    private void onItemsChanged() {
        testAdapter.notifyDataSetChanged();
        recycler.measure(View.MeasureSpec.makeMeasureSpec(480, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.EXACTLY));
        recycler.layout(0, 0, 480, 800);
    }

    public static class Entity {
        private final String id;

        private Entity(String id) {
            this.id = id;
        }
    }

    public static final class TestWidget extends TextView {

        private TestWidget(Context context) {
            super(context);
        }
    }
}
