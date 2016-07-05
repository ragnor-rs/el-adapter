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

    public static final ElEntity[] ITEMS_ARRAY_OF_5 = new ElEntity[]{new ElEntity("one"), new ElEntity("two"), new ElEntity("three"), new ElEntity("four"), new ElEntity("five")};
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
    }


    @Test
    public void testAddItems() {
        assertNotNull(recycler.getAdapter());

        //add first item
        ElEntity item1 = new ElEntity("twelve");

        testAdapter.addItem(item1);
        onItemsChanged();

        Assert.assertEquals(1, recycler.getChildCount());
        Assert.assertEquals(item1.id, ((TextView)recycler.getChildAt(0)).getText().toString());

        //add second item
        ElEntity item2 = new ElEntity("fifteen");

        testAdapter.addItem(item2);
        onItemsChanged();

        Assert.assertEquals(2, recycler.getChildCount());
        Assert.assertEquals(item2.id, ((TextView)recycler.getChildAt(1)).getText().toString());

        //add array items
        testAdapter.addItems(ITEMS_ARRAY_OF_5);
        onItemsChanged();

        Assert.assertEquals(2 + ITEMS_ARRAY_OF_5.length, recycler.getChildCount());
        int testArrayItemIndex = 1;
        Assert.assertEquals(
                ITEMS_ARRAY_OF_5[testArrayItemIndex].id,
                ((TextView)recycler.getChildAt(2 + testArrayItemIndex)).getText().toString()
        );
    }

    @Test
    public void testRemoveItem(){
        assertNotNull(recycler.getAdapter());

        testAdapter.addItems(ITEMS_ARRAY_OF_5);
        onItemsChanged();

        Assert.assertEquals(ITEMS_ARRAY_OF_5.length, recycler.getChildCount());


        //test remove range
        int itemsToRemove = 2;
        testAdapter.removeItemsRange(1, itemsToRemove);
        onItemsChanged();

        Assert.assertEquals(ITEMS_ARRAY_OF_5.length - itemsToRemove , recycler.getChildCount());
        Assert.assertEquals(ITEMS_ARRAY_OF_5[0].id, ((TextView)recycler.getChildAt(0)).getText().toString());
        Assert.assertEquals(ITEMS_ARRAY_OF_5[3].id, ((TextView)recycler.getChildAt(1)).getText().toString());


        //test remove one
        testAdapter.removeItem(1);
        onItemsChanged();

        Assert.assertEquals(ITEMS_ARRAY_OF_5.length - (itemsToRemove + 1) , recycler.getChildCount());
        Assert.assertEquals(ITEMS_ARRAY_OF_5[0].id, ((TextView)recycler.getChildAt(0)).getText().toString());
        Assert.assertEquals(ITEMS_ARRAY_OF_5[4].id, ((TextView)recycler.getChildAt(1)).getText().toString());
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
