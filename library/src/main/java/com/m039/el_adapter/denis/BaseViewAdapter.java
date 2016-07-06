package com.m039.el_adapter.denis;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.m039.el_adapter.BaseViewHolderAdapter;
import com.m039.el_adapter.BaseViewHolder;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by defuera on 05/07/2016.
 * adds simple addViewCreator, addViewBinderMethods
 */
public abstract class BaseViewAdapter<B extends BaseViewAdapter.BaseViewBuilder> extends BaseViewHolderAdapter<B> {

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

    public interface ViewBinder<V extends View> {

        /**
         * To get position call view.getAdapterPosition()
         *
         * @param view view to bind
         */
        void onBindView(V view);

    }


    //region RecyclerView#Adapter

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        ViewBinder<View> viewBinder = getViewBinder(getItemViewType(position));

        if (viewBinder != null) {
            viewBinder.onBindView(holder.itemView);
        } else {
            // do nothing, don't bind a thing
        }

    }

    //endregion


    public <V extends View> BaseViewBuilder.ViewBinderChainer<V> addViewCreator(int viewType, ViewCreator<V> viewCreator) {
        addViewHolderCreator(viewType, new DefaultViewHolderCreator<>(viewCreator));
        return (BaseViewBuilder.ViewBinderChainer<V>) getBuilder(viewType).getViewBinderChainer();
    }

    protected <V extends View> ViewBinder<V> getViewBinder(int viewType) {
        return (ViewBinder<V>) getBuilder(viewType).getViewBinder();
    }

    protected static class DefaultViewHolderCreator<V extends View> implements ViewHolderCreator<BaseViewHolder<V>> {

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
        public BaseViewHolder<V> onCreateViewHolder(ViewGroup parent) {
            V view = mViewCreator.onCreateView(parent);

            setLayoutParams(view);

            return new BaseViewHolder<>(view);
        }

        protected void setLayoutParams(View view) {
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(new ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT));
            }
        }

    }

    //todo replace VH with ?
    public static class BaseViewBuilder<V extends View, VH extends BaseViewHolder<V>> extends BaseViewHolderBuilder<V, VH> {

        private ViewBinder<V> viewBinder;
        private final ViewBinderChainer<V> viewBinderChainer = new ViewBinderChainer<>(this);

        public BaseViewBuilder(ViewHolderCreator<VH> creator) {
            super(creator);
        }

        public ViewBinderChainer<V> getViewBinderChainer() {
            return viewBinderChainer;
        }

        public ViewBinder<V> getViewBinder() {
            return viewBinder;
        }


        public static class ViewBinderChainer<V extends View> {

            private final BaseViewBuilder<V, ?> elBuilder;

            public ViewBinderChainer(BaseViewBuilder<V, ?> elBuilder) {
                this.elBuilder = elBuilder;
            }

            public void addViewBinder(ViewBinder<V> viewBinder) {
                elBuilder.viewBinder = viewBinder;
            }
        }

    }

}