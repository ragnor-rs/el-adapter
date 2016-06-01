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

import java.util.HashMap;
import java.util.Map;

/**
 * ViewTypeHelper manges to create viewType based on several parameters for usage in RecyclerView.Adapter
 * <p>
 * Tha main purpose of ViewTypeHelper is to create or get already created viewType for specified class,
 * look at {@link #getViewType(Class, int)}
 * <p>
 * Created by m039 on 3/3/16.
 */
/* package */ class ViewTypeHelper {

    private final Map<String, Integer> mCachedViewTypes = new HashMap<>();
    private int mLastViewType = 0;

    /**
     * @param clazz       for which find viewType
     * @param typeOfClass type of this class
     * @return merged viewType of class and typeOfClass
     */
    public int getViewType(Class clazz, int typeOfClass) {
        // todo issue with different class names

        String key = clazz.getName() + ";" + typeOfClass;
        Integer viewType = mCachedViewTypes.get(key);

        if (viewType == null) {
            viewType = mLastViewType++;
            mCachedViewTypes.put(key, viewType);
        }

        return viewType;
    }

    /**
     * Try to find name of a class for a specified typeOfClass
     *
     * @param viewType type of a class
     * @return name of a class or null
     */
    /* package */ String findClassName(int viewType) {
        for (Map.Entry<String, Integer> e: mCachedViewTypes.entrySet()) {
            if (e.getValue() == viewType) {
                return e.getKey();
            }
        }

        return null;
    }

}
