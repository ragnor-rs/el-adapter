package com.m039.el_adapter;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.List;

/**
 * Created by defuera on 07/07/2016.
 * Abstract model which allows usage of {@link ItemViewAdapter} with different models.
 * For Example ArrayList via {@link ArrayListItemManager} of SortedList list {@link SortedListItemManager}
 */
public interface ItemManager {

    int getItemCount();

    void removeItem(int position);

    void removeAllItems();

    void removeItemsRange(int positionStart, int itemCount);

    <T> void addItem(T item);

    <T> void addItem(int index, T item);

    <I> void addItems(@NonNull Collection<I> items);

    <I> void addItems(@NonNull I items[]);

    <I> void addItems(int index, @NonNull List<I> items);

    List<Object> getItems(); //todo parametrize

    Object getItemAt(int position);
}
