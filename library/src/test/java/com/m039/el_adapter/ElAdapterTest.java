package com.m039.el_adapter;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m039.el_adapter.view.SimpleTestActivity;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;


/**
 * Created by defuera on 04/07/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ElAdapterTest {

    private SimpleTestActivity activity;
    private RecyclerView recycler;
    private ListItemAdapter testAdapter;

    @SuppressWarnings("ResourceType")
    @Before
    public void setup() throws Exception {
        activity = Robolectric.buildActivity(SimpleTestActivity.class)
                .create()
                .resume()
                .get();


        recycler = (RecyclerView) activity.findViewById(SimpleTestActivity.RECYCLER_VIEW_ID);
        testAdapter = new ListItemAdapter();
        recycler.setAdapter(testAdapter);
    }


    @Test
    public void testAddItems() {
        assertNotNull(recycler.getAdapter());

        testAdapter
                .addViewCreator(
                        ElEntity.class,
                        new BaseViewAdapter.ViewCreator<TextView>() {
                            @Override
                            public TextView onCreateView(ViewGroup parent) {
                                return new TextView(parent.getContext());
                            }
                        }
                )
                .addViewBinder(
                        new ItemViewAdapter.ItemViewBinder<ElEntity, TextView>() {
                            @Override
                            public void onBindView(TextView view, ElEntity item) {
                                view.setText(item.id);
                            }
                        }
                );

        ElEntity item1 = new ElEntity("twelve");

        testAdapter.addItem(item1);
        onItemsChanged();

        Assert.assertEquals(1, recycler.getChildCount());
        Assert.assertEquals(item1.id, ((TextView)recycler.getChildAt(0)).getText().toString());


        ElEntity item2 = new ElEntity("fifteen");

        testAdapter.addItem(item2);
        onItemsChanged();

        Assert.assertEquals(2, recycler.getChildCount());
        Assert.assertEquals(item2.id, ((TextView)recycler.getChildAt(1)).getText().toString());

    }

    private void onItemsChanged() {

        testAdapter.notifyDataSetChanged();

        recycler.measure(
                View.MeasureSpec.makeMeasureSpec(480, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.EXACTLY));
        recycler.layout(0, 0, 480, 800);
    }


    public static class ElEntity {
        public final String id;

        public ElEntity(String id) {
            this.id = id;
        }
    }

}
