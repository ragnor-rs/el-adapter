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
public class SingleCreatorTest {

    public static final ElEntityOne[] ITEMS_ONE_ARRAY_OF_5 = new ElEntityOne[]{new ElEntityOne("one"), new ElEntityOne("two"), new ElEntityOne("three"), new ElEntityOne("four"), new ElEntityOne("five")};
    public static final ElEntityTwo[] ITEMS_TWO_ARRAY_OF_3 = new ElEntityTwo[]{new ElEntityTwo("11"), new ElEntityTwo("12"), new ElEntityTwo("13")};

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
                        ElEntityOne.class,
                        new BaseViewAdapter.ViewCreator<TextView>() {
                            @Override
                            public TextView onCreateView(ViewGroup parent) {
                                return new TextView(parent.getContext());
                            }
                        }
                )
                .addViewBinder(
                        new ItemViewAdapter.ItemViewBinder<ElEntityOne, TextView>() {
                            @Override
                            public void onBindView(TextView view, ElEntityOne item) {
                                view.setText(item.id);
                            }
                        }
                );

        testAdapter
                .addViewCreator(
                        ElEntityTwo.class,
                        new BaseViewAdapter.ViewCreator<TextView>() {
                            @Override
                            public TextView onCreateView(ViewGroup parent) {
                                return new TextView(parent.getContext());
                            }
                        }
                )
                .addViewBinder(
                        new ItemViewAdapter.ItemViewBinder<ElEntityTwo, TextView>() {
                            @Override
                            public void onBindView(TextView view, ElEntityTwo item) {
                                view.setText(item.id);
                            }
                        }
                );
    }


    @Test
    public void testAddItemsOneType() {
        assertNotNull(recycler.getAdapter());

        //add first item
        ElEntityOne itemOne1 = new ElEntityOne("twelve");

        testAdapter.addItem(itemOne1);
        onItemsChanged();

        Assert.assertEquals(1, recycler.getChildCount());
        Assert.assertEquals(itemOne1.id, ((TextView) recycler.getChildAt(0)).getText().toString());

        //add second item
        ElEntityOne itemOne2 = new ElEntityOne("fifteen");

        testAdapter.addItem(itemOne2);
        onItemsChanged();

        Assert.assertEquals(2, recycler.getChildCount());
        Assert.assertEquals(itemOne2.id, ((TextView) recycler.getChildAt(1)).getText().toString());

        //add array items
        testAdapter.addItems(ITEMS_ONE_ARRAY_OF_5);
        onItemsChanged();

        Assert.assertEquals(2 + ITEMS_ONE_ARRAY_OF_5.length, recycler.getChildCount());
        int testArrayItemIndex = 1;
        Assert.assertEquals(
                ITEMS_ONE_ARRAY_OF_5[testArrayItemIndex].id,
                ((TextView) recycler.getChildAt(2 + testArrayItemIndex)).getText().toString()
        );
    }

    @Test
    public void testAddItemsTwoTypes() {
        assertNotNull(recycler.getAdapter());

        //add first item
        ElEntityOne itemOne = new ElEntityOne("twelve");

        testAdapter.addItem(itemOne);
        onItemsChanged();

        Assert.assertEquals(1, recycler.getChildCount());
        Assert.assertEquals(itemOne.id, ((TextView) recycler.getChildAt(0)).getText().toString());

        //add second item
        ElEntityTwo itemOne2 = new ElEntityTwo("fifteen");

        testAdapter.addItem(itemOne2);
        onItemsChanged();

        Assert.assertEquals(2, recycler.getChildCount());
        Assert.assertEquals(itemOne2.id, ((TextView) recycler.getChildAt(1)).getText().toString());

    }

    @Test
    public void testRemoveItemOneType() {
        assertNotNull(recycler.getAdapter());

        testAdapter.addItems(ITEMS_ONE_ARRAY_OF_5);
        onItemsChanged();

        Assert.assertEquals(ITEMS_ONE_ARRAY_OF_5.length, recycler.getChildCount());


        //test remove range
        int itemsToRemove = 2;
        testAdapter.removeItemsRange(1, itemsToRemove);
        onItemsChanged();

        Assert.assertEquals(ITEMS_ONE_ARRAY_OF_5.length - itemsToRemove, recycler.getChildCount());
        Assert.assertEquals(ITEMS_ONE_ARRAY_OF_5[0].id, ((TextView) recycler.getChildAt(0)).getText().toString());
        Assert.assertEquals(ITEMS_ONE_ARRAY_OF_5[3].id, ((TextView) recycler.getChildAt(1)).getText().toString());


        //test remove one
        testAdapter.removeItem(1);
        onItemsChanged();

        Assert.assertEquals(ITEMS_ONE_ARRAY_OF_5.length - (itemsToRemove + 1), recycler.getChildCount());
        Assert.assertEquals(ITEMS_ONE_ARRAY_OF_5[0].id, ((TextView) recycler.getChildAt(0)).getText().toString());
        Assert.assertEquals(ITEMS_ONE_ARRAY_OF_5[4].id, ((TextView) recycler.getChildAt(1)).getText().toString());
    }

    @Test
    public void testRemoveItemTwoTypes() {
        assertNotNull(recycler.getAdapter());

        testAdapter.addItems(ITEMS_ONE_ARRAY_OF_5);
        testAdapter.addItems(ITEMS_TWO_ARRAY_OF_3);
        onItemsChanged();

        int sumLength = ITEMS_ONE_ARRAY_OF_5.length + ITEMS_TWO_ARRAY_OF_3.length;
        Assert.assertEquals(sumLength, recycler.getChildCount());


        //test remove range
        int itemsToRemove = sumLength - 2;
        testAdapter.removeItemsRange(1, itemsToRemove);
        onItemsChanged();

        Assert.assertEquals(sumLength - itemsToRemove, recycler.getChildCount());
        Assert.assertEquals(ITEMS_ONE_ARRAY_OF_5[0].id, ((TextView) recycler.getChildAt(0)).getText().toString());
        Assert.assertEquals(ITEMS_TWO_ARRAY_OF_3[2].id, ((TextView) recycler.getChildAt(1)).getText().toString());
    }

    private void onItemsChanged() {
        testAdapter.notifyDataSetChanged();
        recycler.measure(
                View.MeasureSpec.makeMeasureSpec(480, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.EXACTLY));
        recycler.layout(0, 0, 480, 800);
    }


    public static class ElEntityOne {
        public final String id;

        public ElEntityOne(String id) {
            this.id = id;
        }
    }

    public static class ElEntityTwo {
        public final String id;

        public ElEntityTwo(String id) {
            this.id = id;
        }
    }

}
