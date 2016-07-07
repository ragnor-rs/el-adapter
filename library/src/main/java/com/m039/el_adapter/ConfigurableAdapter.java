package com.m039.el_adapter;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.Collection;
import java.util.List;

/**
 * Created by defuera on 07/07/2016.
 */
public class ConfigurableAdapter extends ItemViewAdapter<ItemViewAdapter.ItemViewBuilder> implements Configurator {

    private Configurator configurator;

    public ConfigurableAdapter() {
        this.configurator = new ArrayListConfigurator();
    }

    public ConfigurableAdapter(Configurator configurator) {
        this.configurator = configurator;
    }

    @Override
    protected <V extends View, VH extends BaseViewHolder<V>> ItemViewBuilder createBuilder(ViewHolderCreator<VH> creator) {
        return new ItemViewBuilder(creator); //todo unchecked
    }

    @Override
    public Object getItemAt(int position) {
        return configurator.getItemAt(position);
    }

    @Override
    public int getItemCount() {
        return configurator.getItemCount();
    }

    @Override
    public void removeItem(int position) {
        configurator.removeItem(position);
    }

    @Override
    public void removeAllItems() {
        configurator.removeAllItems();
    }

    @Override
    public void removeItemsRange(int positionStart, int itemCount) {
        configurator.removeItemsRange(positionStart, itemCount);
    }

    @Override
    public <T> void addItem(T item) {
        configurator.addItem(item);
    }

    @Override
    public <T> void addItem(int index, T item) {
        configurator.addItem(index, item);
    }

    @Override
    public <I> void addItems(@NonNull Collection<I> items) {
        configurator.addItems(items);
    }

    @Override
    public <I> void addItems(@NonNull I[] items) {
        configurator.addItems(items);
    }

    @Override
    public <I> void addItems(int index, @NonNull List<I> items) {
        configurator.addItems(index, items);
    }

    @Override
    public List<Object> getItems() {
        return configurator.getItems();
    }

}
