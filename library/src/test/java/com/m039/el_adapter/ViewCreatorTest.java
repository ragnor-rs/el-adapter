package com.m039.el_adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m039.el_adapter.BaseViewAdapter.ViewHolder;
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
public class ViewCreatorTest {

    public static final ElEntityTwo[] ITEMS_TWO_ARRAY_OF_3 = new ElEntityTwo[]{new ElEntityTwo("11"), new ElEntityTwo("12"), new ElEntityTwo("13")};
    private static final String TEST_ONE_POSTFIX = "Uno";
    private static final String TEST_TWO_POSTFIX = "Dos";

    private RecyclerView recycler;
    private ListItemAdapter testAdapter;

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("ResourceType")
    @Before
    public void setup() throws Exception {
        SimpleTestActivity activity = Robolectric.buildActivity(SimpleTestActivity.class)
                .create()
                .resume()
                .get();

        recycler = (RecyclerView) activity.findViewById(SimpleTestActivity.RECYCLER_VIEW_ID);
        testAdapter = new ListItemAdapter();
        recycler.setAdapter(testAdapter);

        testAdapter
                .addViewCreator(
                        ElEntityOne.class,
                        new BaseViewAdapter.ViewCreator<TestWidgetOne>() {
                            @Override
                            public TestWidgetOne onCreateView(ViewGroup parent) {
                                return new TestWidgetOne(parent.getContext());
                            }
                        }
                )
                .addViewBinder(
                        new ItemViewAdapter.ItemViewBinder<ElEntityOne, TestWidgetOne>() {
                            @Override
                            public void onBindView(TestWidgetOne view, ElEntityOne item) {
                                view.setText(item.id + TEST_ONE_POSTFIX);
                            }
                        }
                );

        testAdapter
                .addViewCreator(
                        ElEntityTwo.class,
                        new BaseViewAdapter.ViewCreator<TestWidgetTwo>() {
                            @Override
                            public TestWidgetTwo onCreateView(ViewGroup parent) {
                                return new TestWidgetTwo(parent.getContext());
                            }
                        }
                )
                .addViewBinder(
                        new ItemViewAdapter.ItemViewBinder<ElEntityTwo, TestWidgetTwo>() {
                            @Override
                            public void onBindView(TestWidgetTwo view, ElEntityTwo item) {
                                view.setText(item.id + TEST_TWO_POSTFIX);
                            }
                        }
                );
    }


    @Test
    public void testViewCreator() {
        assertNotNull(recycler.getAdapter());

        //add first item
        ElEntityOne itemOne1 = new ElEntityOne("twelve");

        testAdapter.addItem(itemOne1);
        onItemsChanged();

        Assert.assertEquals(1, recycler.getChildCount());
        Assert.assertEquals(itemOne1.id + TEST_ONE_POSTFIX, ((TextView) recycler.getChildAt(0)).getText().toString());

        //add array items
        testAdapter.addItems(ITEMS_TWO_ARRAY_OF_3);
        onItemsChanged();

        Assert.assertEquals(1 + ITEMS_TWO_ARRAY_OF_3.length, recycler.getChildCount());
        Assert.assertEquals(itemOne1.id + TEST_ONE_POSTFIX, ((TextView) recycler.getChildAt(0)).getText().toString());
        Assert.assertEquals(ITEMS_TWO_ARRAY_OF_3[0].id + TEST_TWO_POSTFIX, ((TextView) recycler.getChildAt(1)).getText().toString());
        Assert.assertEquals(ITEMS_TWO_ARRAY_OF_3[1].id + TEST_TWO_POSTFIX, ((TextView) recycler.getChildAt(2)).getText().toString());
        Assert.assertEquals(ITEMS_TWO_ARRAY_OF_3[2].id + TEST_TWO_POSTFIX, ((TextView) recycler.getChildAt(3)).getText().toString());

        //test remove items
        int itemsToRemove = 2;
        testAdapter.removeItemsRange(1, itemsToRemove);
        onItemsChanged();

        Assert.assertEquals(1 + ITEMS_TWO_ARRAY_OF_3.length - itemsToRemove, recycler.getChildCount());
        Assert.assertEquals(itemOne1.id + TEST_ONE_POSTFIX, ((TextView) recycler.getChildAt(0)).getText().toString());
        Assert.assertEquals(ITEMS_TWO_ARRAY_OF_3[2].id + TEST_TWO_POSTFIX, ((TextView) recycler.getChildAt(1)).getText().toString());
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

    public static final class TestWidgetOne extends TextView {

        public TestWidgetOne(Context context) {
            super(context);
        }
    }

    public static final class TestWidgetTwo extends TextView {

        public TestWidgetTwo(Context context) {
            super(context);
        }

    }
}
