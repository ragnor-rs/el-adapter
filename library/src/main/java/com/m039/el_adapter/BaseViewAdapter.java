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

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Base class for adapters.
 * <p/>
 * The main difference between this class and other that it doesn't know anything about data only
 * how to create view.
 * <p/>
 * BaseViewAdapter is only responsible for creating view, not binding them.
 * <p/>
 * Logic for binding should be in another/inherited classes.
 * <p/>
 * Tha main idea to separate creation and binding cause creation always returns same view for a same
 * viewType, but binding may differ.
 * <p/>
 * Created by m039 on 3/3/16.
 */
public abstract class BaseViewAdapter<B extends ViewBindingBuilder> extends RecyclerView.Adapter<BaseViewAdapter.ViewHolder<?>>
        implements ViewCreatorDelegate<B>
{

    public static final int DEFAULT_VIEW_TYPE = 0;

    public static class ViewHolder<V extends View> extends RecyclerView.ViewHolder {

        public V itemView; // parameterize

        public ViewHolder(V itemView) {
            super(itemView);
            this.itemView = itemView;
        }

    }

    /**
     * This interface is used to create views in {@link #onCreateViewHolder(ViewGroup, int)}
     */
    public interface ViewCreator<V extends View> {

        /**
         * @param parent parent of a new view
         * @return should be a new created view
         */
        V onCreateView(ViewGroup parent);

    }

    /**
     * This interface is used to create views in {@link #onCreateViewHolder(ViewGroup, int)}
     */
    public interface ViewHolderCreator<V extends View> {

        /**
         * @param parent parent of a new view
         * @return should be a new created viewHolder
         */
        ViewHolder<V> onCreateViewHolder(ViewGroup parent);

    }

    protected static class DefaultViewHolderCreator<V extends View> implements ViewHolderCreator<V> {

        @NonNull
        private final ViewCreator<V> mViewCreator;

        DefaultViewHolderCreator(@NonNull ViewCreator<V> viewCreator) {
            mViewCreator = viewCreator;
        }

        @NonNull
        public ViewCreator<V> getViewCreator() {
            return mViewCreator;
        }

        @Override
        public ViewHolder<V> onCreateViewHolder(ViewGroup parent) {
            V view = mViewCreator.onCreateView(parent);

            setLayoutParams(view);

            return new ViewHolder<>(view);
        }

        protected void setLayoutParams(View view) {
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(new ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT));
            }
        }

    }

    private final ViewBindingBuilderCreator<B> bindingBuilderCreator;
    private final Map<Integer, ViewHolderCreator> mOnCreteViewHolderByViewType = new HashMap<>();

    protected BaseViewAdapter(ViewBindingBuilderCreator<B> bindingBuilderCreator) {
        this.bindingBuilderCreator = bindingBuilderCreator;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderCreator viewHolderCreator = mOnCreteViewHolderByViewType.get(viewType);

        if (viewHolderCreator == null) {
            String className = null;

            if (this instanceof ItemViewAdapter) {
                className = ((ItemViewAdapter) this).findClassName(viewType);
            }

            throw new IllegalStateException("Can't create view of type " +
                    viewType + (className != null ? " or '" + className : "'") + "." +
                    " You should register " +
                    ViewCreator.class.getSimpleName() +
                    " or " +
                    ViewHolderCreator.class.getSimpleName() +
                    " for that type."
            );
        }

        return viewHolderCreator.onCreateViewHolder(parent);
    }

    @SuppressWarnings("unchecked")
    protected B newCommandBuilder(int viewType) {
        return bindingBuilderCreator.newBindingBuilder(this, viewType);
    }


    public interface ViewBindingBuilderCreator<C extends ViewBindingBuilder> {
        C newBindingBuilder(BaseViewAdapter<C> adapter, int viewType);
    }

    /**
     * The main method to add viewCreator to this class
     *
     * @param viewType    viewType for which <code>viewCreator</code> will be used
     * @param viewCreator creator of views
     */
    @Override
    public <V extends View>
    B addViewCreator(int viewType, ViewCreator<V> viewCreator) {
        return addViewHolderCreator(viewType, new DefaultViewHolderCreator<>(viewCreator));
    }

    /**
     * @param viewType          for which <code>viewHolderCreator</code> will be used
     * @param viewHolderCreator creator of viewHolders
     */
    @Override
    public <V extends View>
    B addViewHolderCreator(int viewType, ViewHolderCreator<V> viewHolderCreator) {
        mOnCreteViewHolderByViewType.put(viewType, viewHolderCreator);
        return newCommandBuilder(viewType);
    }

    /**
     * @return viewCreator associated with this <code>viewType</code> or null
     */
    protected ViewCreator<?> getViewCreator(int viewType) {
        ViewHolderCreator viewHolderCreator = getViewHolderCreator(viewType);
        if (viewHolderCreator instanceof DefaultViewHolderCreator) {
            return ((DefaultViewHolderCreator) viewHolderCreator).getViewCreator();
        } else {
            throw new IllegalStateException("Can't find viewCreator");
        }
    }

    /**
     * @return viewHolderCreator associated this <code>viewType</code> or null
     */
    protected ViewHolderCreator getViewHolderCreator(int viewType) {
        return mOnCreteViewHolderByViewType.get(viewType);
    }

}
