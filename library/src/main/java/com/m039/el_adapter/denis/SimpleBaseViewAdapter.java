package com.m039.el_adapter.denis;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.m039.el_adapter.BaseViewHolder;
import com.m039.el_adapter.BaseViewAdapter;
import com.m039.el_adapter.ViewHolderCreator;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by defuera on 05/07/2016.
 * adds simple addViewCreator, addViewBinderMethods
 */
public abstract class SimpleBaseViewAdapter<B extends SimpleBaseViewAdapter.SimpleViewElBuilder> extends BaseViewAdapter<B> {

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

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);


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

    public <V extends View> SimpleViewElBuilder.ViewBinderChainer addViewCreator(int viewType, ViewCreator<V> viewCreator){
        addViewHolderCreator(viewType, new DefaultViewHolderCreator<>(viewCreator));
        return builderMap.get(viewType).viewBinderChainer();
    }

    public static class SimpleViewElBuilder<V extends View, VH extends BaseViewHolder<V>> extends ElBuilder<V, VH>{

        private ViewBinder<V> viewBinder;

        public SimpleViewElBuilder(ViewHolderCreator<VH> creator) {
            super(creator);
        }

        public ViewBinderChainer viewBinderChainer() {
            return new ViewBinderChainer();
        }

        public class ViewBinderChainer {

            public void addViewBinder(ViewBinder<V> viewBinder){
                SimpleViewElBuilder.this.viewBinder = viewBinder;
            }
        }
    }
}
