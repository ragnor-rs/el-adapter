package com.m039.el_adapter;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.Collection;
import java.util.List;

/**
 * Created by defuera on 07/07/2016.
 * ManagingAdapter can work with different {@link ItemManager}s. Where ItemManager is simply a
 * model (or collection) wrapper
 * <p>
 * It can be configured via constructor {@link ManagingAdapter#ManagingAdapter(ItemManager)} or
 * setter {@link ManagingAdapter#setItemManager(ItemManager)}
 * <p>
 * El adapter contains few default ItemManagers - {@link ArrayListItemManager}, {@link SortedListItemManager},
 * which wraps ArrayList and SortedList accordingly.
 * <p>
 * Default constructor {@link ManagingAdapter#ManagingAdapter()} sets ArrayListItemManager
 */
public class ManagingAdapter extends ItemViewAdapter<ItemViewAdapter.ItemViewHelper> implements ItemManager {

    @NonNull
    private ItemManager itemManager;

    public ManagingAdapter() {
        this.itemManager = new ArrayListItemManager();
    }

    public ManagingAdapter(@NonNull ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public void setItemManager(@NonNull ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    protected <V extends View, VH extends BaseViewHolder<V>> ItemViewHelper createBuilder(ViewHolderCreator<VH> creator) {
        return new ItemViewHelper(creator); //todo unchecked
    }

    @Override
    public Object getItemAt(int position) {
        return itemManager.getItemAt(position);
    }

    @Override
    public int getItemCount() {
        return itemManager.getItemCount();
    }

    @Override
    public void removeItem(int position) {
        itemManager.removeItem(position);
    }

    @Override
    public void removeAllItems() {
        itemManager.removeAllItems();
    }

    @Override
    public void removeItemsRange(int positionStart, int itemCount) {
        itemManager.removeItemsRange(positionStart, itemCount);
    }

    @Override
    public <I> void addItem(I item) {
        itemManager.addItem(item);
    }

    @Override
    public <I> void addItem(int index, I item) {
        itemManager.addItem(index, item);
    }

    @Override
    public <I> void addItems(@NonNull Collection<I> items) {
        itemManager.addItems(items);
    }

    @Override
    public <I> void addItems(@NonNull I[] items) {
        itemManager.addItems(items);
    }

    @Override
    public <I> void addItems(int index, @NonNull List<I> items) {
        itemManager.addItems(index, items);
    }

    @Override
    public List<Object> getItems() {
        return itemManager.getItems();
    }

}
