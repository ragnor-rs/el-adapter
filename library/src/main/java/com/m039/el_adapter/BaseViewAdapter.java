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
public abstract class BaseViewAdapter<B extends CommandBuilder> extends RecyclerView.Adapter<BaseViewAdapter.ViewHolder<?>>

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

    final protected CommandBuilder commandBuilder;

    protected BaseViewAdapter(Class<? extends CommandBuilder> clazz) {
        try {
            commandBuilder = clazz.getConstructor(BaseViewAdapter.class).newInstance(this);
        } catch (Exception e) {
            throw new RuntimeException("Can't instantiate the class", e);
        }
    }

    private final ViewTypeHelper mViewTypeHelper = new ViewTypeHelper();
    private final Map<Integer, ViewHolderCreator> mOnCreteViewHolderByViewType = new HashMap<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderCreator viewHolderCreator = mOnCreteViewHolderByViewType.get(viewType);

        if (viewHolderCreator == null) {
            throw new IllegalStateException("Can't create view of type " + viewType + "." +
                    " You should register " +
                    ViewCreator.class.getSimpleName() +
                    " or " +
                    ViewHolderCreator.class.getSimpleName() +
                    " for that type."
            );
        }

        return viewHolderCreator.onCreateViewHolder(parent);
    }

    /**
     * @param clazz       <code>clazz</code> will be used to create new view type if if doesn't
     *                    exist yet, and for this viewType <code>viewCreator</code> will be used
     * @param viewCreator creator of views
     */
    @SuppressWarnings("unchecked")
    public <I, V extends View>
    B addViewCreator(Class<I> clazz, ViewCreator<V> viewCreator) {
        addViewHolderCreator(clazz, new DefaultViewHolderCreator<>(viewCreator));
        return (B) commandBuilder;
    }

    /**
     * @param clazz           <code>clazz</code> and <code>viewTypeOfClass</code> will be used
     *                        to create new view type if if doesn't exist yet and for this viewType
     *                        <code>viewCreator</code> will be used
     * @param viewTypeOfClass viewType of <code>clazz</code>. This is additional type to make more
     *                        types for same <code>clazz</code>
     * @param viewCreator     creator of views
     */
    @SuppressWarnings("unchecked")
    public <I, V extends View>
    B addViewCreator(Class<I> clazz, int viewTypeOfClass, ViewCreator<V> viewCreator) {
        addViewHolderCreator(clazz, viewTypeOfClass, new DefaultViewHolderCreator<>(viewCreator));
        return (B) commandBuilder;
    }

    /**
     * The main method to add viewCreator to this class, other methods like
     * {@link #addViewCreator(Class, int, ViewCreator)} uses helper class to create or get viewType.
     *
     * @param viewType    viewType for which <code>viewCreator</code> will be used
     * @param viewCreator creator of views
     */
    @SuppressWarnings("unchecked")
    public <I, V extends View>
    B addViewCreator(int viewType, ViewCreator<V> viewCreator) {
        addViewHolderCreator(viewType, new DefaultViewHolderCreator<>(viewCreator));
        return (B) commandBuilder;
    }

    @SuppressWarnings("unchecked")
    public <I, V extends View>
    B addViewHolderCreator(Class<I> clazz, ViewHolderCreator<V> viewHolderCreator) {
        addViewHolderCreator(clazz, DEFAULT_VIEW_TYPE, viewHolderCreator);
        return (B) commandBuilder;
    }

    @SuppressWarnings("unchecked")
    public <I, V extends View>
    B addViewHolderCreator(Class<I> clazz, int viewTypeOfClass, ViewHolderCreator<V> viewHolderCreator) {
        addViewHolderCreator(getItemViewType(clazz, viewTypeOfClass), viewHolderCreator);
        return (B) commandBuilder;
    }

    /**
     * @param viewType for which <code>viewHolderCreator</code> will be used
     * @param viewHolderCreator creator of viewHolders
     */
    @SuppressWarnings("unchecked")
    public <I, V extends View>
    B addViewHolderCreator(int viewType, ViewHolderCreator<V> viewHolderCreator) {
        mOnCreteViewHolderByViewType.put(viewType, viewHolderCreator);
        return (B) commandBuilder;
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
    protected ViewHolderCreator<?> getViewHolderCreator(int viewType) {
        return mOnCreteViewHolderByViewType.get(viewType);
    }

    /**
     * Same as {@link #getItemViewType(Class, int)} but with <code>viewTypeOfClass</code> is 0.
     */
    protected <T> int getItemViewType(Class<T> clazz) {
        return getItemViewType(clazz, DEFAULT_VIEW_TYPE);
    }

    /**
     * This function will create or get viewType based on <code>class</code> and
     * <code>viewTypeOfClass</code>
     */
    protected <T> int getItemViewType(Class<T> clazz, int viewTypeOfClass) {
        return getViewTypeOffset() + mViewTypeHelper.getViewType(clazz, viewTypeOfClass);
    }

    /**
     * You will need this function if you have custom viewTypes and you need that others values
     * doesn't clash with them.
     *
     * @return offset from which functions like {@link #getItemViewType(Class, int)} or
     * {@link #getItemViewType(Class)} will return the value.
     */
    protected int getViewTypeOffset() {
        return 0;
    }

}
