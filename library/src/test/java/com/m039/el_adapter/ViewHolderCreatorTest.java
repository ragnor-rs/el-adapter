//package com.m039.el_adapter;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.os.Build;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.m039.el_adapter.view.SimpleTestActivity;
//
//import junit.framework.Assert;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.robolectric.Robolectric;
//import org.robolectric.RobolectricGradleTestRunner;
//import org.robolectric.annotation.Config;
//
//import static org.junit.Assert.assertNotNull;
//
//
///**
// * Created by defuera on 04/07/2016.
// */
//@RunWith(RobolectricGradleTestRunner.class)
//@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
//public class ViewHolderCreatorTest {
//
//    public static final ElEntityOne[] ITEMS_ONE_ARRAY_OF_5 = new ElEntityOne[]{new ElEntityOne("one"), new ElEntityOne("two"), new ElEntityOne("three"), new ElEntityOne("four"), new ElEntityOne("five")};
//    public static final ElEntityTwo[] ITEMS_TWO_ARRAY_OF_3 = new ElEntityTwo[]{new ElEntityTwo("11"), new ElEntityTwo("12"), new ElEntityTwo("13")};
//    private static final String TEST_ONE_POSTFIX = "Uno";
//    private static final String TEST_TWO_POSTFIX = "Dos";
//
//    private RecyclerView recycler;
//    private ListItemAdapter testAdapter;
//
//    @SuppressLint("SetTextI18n")
//    @SuppressWarnings("ResourceType")
//    @Before
//    public void setup() throws Exception {
//        SimpleTestActivity activity = Robolectric.buildActivity(SimpleTestActivity.class)
//                .create()
//                .resume()
//                .get();
//
//        recycler = (RecyclerView) activity.findViewById(SimpleTestActivity.RECYCLER_VIEW_ID);
//        testAdapter = new ListItemAdapter();
//        recycler.setAdapter(testAdapter);
//
//        testAdapter
//                .addViewHolderCreator(
//                        ElEntityOne.class,
//                        new ViewHolderCreator<BaseViewHolder<TestWidgetOne>>() {
//
//                            @Override
//                            public BaseViewHolder<TestWidgetOne> onCreateViewHolder(ViewGroup parent) {
//                                return new BaseViewHolder<>(new TestWidgetOne(parent.getContext()));
//                            }
//                        }
//
//                )
//                .addViewHolderBinder(
//                        new ItemViewAdapter.ItemViewHolderBinder<ElEntityOne, TestWidgetOne>() {
//                            @Override
//                            public void onBindViewHolder(BaseViewHolder<TestWidgetOne> viewHolder, ElEntityOne item) {
//                                viewHolder.itemView.setText(item.id + TEST_ONE_POSTFIX);
//                            }
//                        }
//                );
//
//        testAdapter
//                .addViewHolderCreator(
//                        ElEntityTwo.class,
//                        new ViewHolderCreator<BaseViewHolder<TestWidgetTwo>>() {
//
//                            @Override
//                            public BaseViewHolder<TestWidgetTwo> onCreateViewHolder(ViewGroup parent) {
//                                return new BaseViewHolder<>(new TestWidgetTwo(parent.getContext()));
//                            }
//                        }
//
//                )
//                .addViewHolderBinder(
//                        new ItemViewAdapter.ItemViewHolderBinder<ElEntityTwo, TestWidgetTwo>() {
//                            @Override
//                            public void onBindViewHolder(BaseViewHolder<TestWidgetTwo> viewHolder, ElEntityTwo item) {
//                                viewHolder.itemView.setText(item.id + TEST_TWO_POSTFIX);
//                            }
//                        }
//                );
//    }
//
//
//    @Test
//    public void testAddItemsOneType() {
//        assertNotNull(recycler.getAdapter());
//
//        //add first item
//        ElEntityOne itemOne1 = new ElEntityOne("twelve");
//
//        testAdapter.addItem(itemOne1);
//        onItemsChanged();
//
//        Assert.assertEquals(1, recycler.getChildCount());
//        Assert.assertEquals(itemOne1.id + TEST_ONE_POSTFIX, ((TextView) recycler.getChildAt(0)).getText().toString());
//
//        //add second item
//        ElEntityOne itemOne2 = new ElEntityOne("fifteen");
//
//        testAdapter.addItem(itemOne2);
//        onItemsChanged();
//
//        Assert.assertEquals(2, recycler.getChildCount());
//        Assert.assertEquals(itemOne1.id + TEST_ONE_POSTFIX, ((TextView) recycler.getChildAt(0)).getText().toString());
//        Assert.assertEquals(itemOne2.id + TEST_ONE_POSTFIX, ((TextView) recycler.getChildAt(1)).getText().toString());
//
//        //add array items
//        testAdapter.addItems(ITEMS_ONE_ARRAY_OF_5);
//        onItemsChanged();
//
//        Assert.assertEquals(2 + ITEMS_ONE_ARRAY_OF_5.length, recycler.getChildCount());
//        int testArrayItemIndex = 1;
//        Assert.assertEquals(
//                ITEMS_ONE_ARRAY_OF_5[testArrayItemIndex].id + TEST_ONE_POSTFIX,
//                ((TextView) recycler.getChildAt(2 + testArrayItemIndex)).getText().toString()
//        );
//    }
//
//    @Test
//    public void testAddItemsTwoTypes() {
//        assertNotNull(recycler.getAdapter());
//
//        //add first item
//        ElEntityOne itemOne = new ElEntityOne("twelve");
//
//        testAdapter.addItem(itemOne);
//        onItemsChanged();
//
//        Assert.assertEquals(1, recycler.getChildCount());
//        Assert.assertEquals(itemOne.id + TEST_ONE_POSTFIX, ((TextView) recycler.getChildAt(0)).getText().toString());
//
//        //add second item
//        ElEntityTwo itemOne2 = new ElEntityTwo("fifteen");
//
//        testAdapter.addItem(itemOne2);
//        onItemsChanged();
//
//        Assert.assertEquals(2, recycler.getChildCount());
//        Assert.assertEquals(itemOne.id + TEST_ONE_POSTFIX, ((TextView) recycler.getChildAt(0)).getText().toString());
//        Assert.assertEquals(itemOne2.id + TEST_TWO_POSTFIX, ((TextView) recycler.getChildAt(1)).getText().toString());
//
//    }
//
//    @Test
//    public void testRemoveItemOneType() {
//        assertNotNull(recycler.getAdapter());
//
//        testAdapter.addItems(ITEMS_ONE_ARRAY_OF_5);
//        onItemsChanged();
//
//        Assert.assertEquals(ITEMS_ONE_ARRAY_OF_5.length, recycler.getChildCount());
//
//
//        //test remove range
//        int itemsToRemove = 2;
//        testAdapter.removeItemsRange(1, itemsToRemove);
//        onItemsChanged();
//
//        Assert.assertEquals(ITEMS_ONE_ARRAY_OF_5.length - itemsToRemove, recycler.getChildCount());
//        Assert.assertEquals(ITEMS_ONE_ARRAY_OF_5[0].id + TEST_ONE_POSTFIX, ((TextView) recycler.getChildAt(0)).getText().toString());
//        Assert.assertEquals(ITEMS_ONE_ARRAY_OF_5[3].id + TEST_ONE_POSTFIX, ((TextView) recycler.getChildAt(1)).getText().toString());
//
//
//        //test remove one
//        testAdapter.removeItem(1);
//        onItemsChanged();
//
//        Assert.assertEquals(ITEMS_ONE_ARRAY_OF_5.length - (itemsToRemove + 1), recycler.getChildCount());
//        Assert.assertEquals(ITEMS_ONE_ARRAY_OF_5[0].id + TEST_ONE_POSTFIX, ((TextView) recycler.getChildAt(0)).getText().toString());
//        Assert.assertEquals(ITEMS_ONE_ARRAY_OF_5[4].id + TEST_ONE_POSTFIX, ((TextView) recycler.getChildAt(1)).getText().toString());
//    }
//
//    @Test
//    public void testRemoveItemTwoTypes() {
//        assertNotNull(recycler.getAdapter());
//
//        testAdapter.addItems(ITEMS_ONE_ARRAY_OF_5);
//        testAdapter.addItems(ITEMS_TWO_ARRAY_OF_3);
//        onItemsChanged();
//
//        int sumLength = ITEMS_ONE_ARRAY_OF_5.length + ITEMS_TWO_ARRAY_OF_3.length;
//        Assert.assertEquals(sumLength, recycler.getChildCount());
//
//
//        //test remove range
//        int itemsToRemove = sumLength - 2;
//        testAdapter.removeItemsRange(1, itemsToRemove);
//        onItemsChanged();
//
//        Assert.assertEquals(sumLength - itemsToRemove, recycler.getChildCount());
//        Assert.assertEquals(ITEMS_ONE_ARRAY_OF_5[0].id + TEST_ONE_POSTFIX, ((TextView) recycler.getChildAt(0)).getText().toString());
//        Assert.assertEquals(ITEMS_TWO_ARRAY_OF_3[2].id + TEST_TWO_POSTFIX, ((TextView) recycler.getChildAt(1)).getText().toString());
//    }
//
//    private void onItemsChanged() {
//        testAdapter.notifyDataSetChanged();
//        recycler.measure(
//                View.MeasureSpec.makeMeasureSpec(480, View.MeasureSpec.EXACTLY),
//                View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.EXACTLY));
//        recycler.layout(0, 0, 480, 800);
//    }
//
//
//    public static class ElEntityOne {
//        public final String id;
//
//        public ElEntityOne(String id) {
//            this.id = id;
//        }
//    }
//
//    public static class ElEntityTwo {
//        public final String id;
//
//        public ElEntityTwo(String id) {
//            this.id = id;
//        }
//    }
//
//    public static final class TestWidgetOne extends TextView {
//
//        public TestWidgetOne(Context context) {
//            super(context);
//        }
//    }
//
//    public static final class TestWidgetTwo extends TextView {
//
//        public TestWidgetTwo(Context context) {
//            super(context);
//        }
//
//    }
//}
