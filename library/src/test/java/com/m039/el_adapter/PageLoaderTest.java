/**
 * Copyright (C) 2016 Dmitry Mozgin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m039.el_adapter.perpage.PerPageItemViewAdapter;
import com.m039.el_adapter.view.SimpleTestActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by 4xes on 17/11/2016.
 */
@RunWith(RobolectricGradle3TestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class PageLoaderTest {

    private RecyclerView recycler;
    private PerPageItemViewAdapter testAdapter;

    @Mock
    private PerPageItemViewAdapter.PageLoader pageLoader;

    public Entity[] testEntities;

    public static Entity[] createEntities(int size){
        Entity[] elEntities = new Entity[size];
        for(int i = 0; i < size; i++){
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
        testAdapter = new PerPageItemViewAdapter(pageLoader, new ListItemAdapter());
        recycler.setAdapter(testAdapter);


        testAdapter
            .addViewHolderCreator(Entity.class, new BaseViewHolderAdapter.ViewHolderCreator<BaseViewHolder<TestWidget>>() {
                @Override
                public BaseViewHolder<TestWidget> onCreateViewHolder(ViewGroup parent) {
                    return new BaseViewHolder<>(new TestWidget(parent.getContext()));
                }
            })
            .addViewHolderBinder(new ItemViewAdapter.ItemViewHolderBinder<Entity, TestWidget>() {
                @Override
                public void onBindViewHolder(BaseViewHolder<TestWidget> viewHolder, Entity item) {
                    viewHolder.itemView.setText(item.id);
                }
            });

        testEntities = createEntities(30);
    }


    @Test
    public void testPageLoader() {
        assertNotNull(recycler.getAdapter());

        Entity[] insertEntities = Arrays.copyOfRange(testEntities, 0, 20);
        testAdapter.addItems(insertEntities);
        onItemsChanged();

        verify(pageLoader, times(10)).startPageLoading();
        verify(pageLoader, times(10)).isPageLoading();

        reset(pageLoader);

        doReturn(true).when(pageLoader).isPageLoading();

        insertEntities = Arrays.copyOfRange(testEntities, 20, 30);
        testAdapter.addItems(insertEntities);
        onItemsChanged();

        verify(pageLoader, times(10)).isPageLoading();
        verify(pageLoader, never()).startPageLoading();
    }

    private void onItemsChanged() {
        testAdapter.notifyDataSetChanged();
        recycler.measure(
                View.MeasureSpec.makeMeasureSpec(480, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.EXACTLY));
        recycler.layout(0, 0, 480, 800);
    }


    public static class Entity {
        private final String id;

        private Entity(String id) {
            this.id = id;
        }
    }


    public static final class TestWidget extends TextView {

        public TestWidget(Context context) {
            super(context);
        }
    }
}
