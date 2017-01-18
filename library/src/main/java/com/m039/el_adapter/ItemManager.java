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
