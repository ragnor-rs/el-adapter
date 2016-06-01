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

}
