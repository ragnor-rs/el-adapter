package com.m039.el_adapter;

import android.support.annotation.IdRes;
import android.view.View;

import com.m039.el_adapter.BaseViewHolderAdapter.ViewHolderBinder;
import com.m039.el_adapter.BaseViewHolderAdapter.ViewHolderCreator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by defuera on 05/07/2016.
 */
public class BaseViewHolderBuilder<V extends View, VH extends BaseViewHolder<V>> {

    public static final int NO_ID_CLICK_LISTENER = 0;

    private final ViewHolderCreator<VH> viewHolderCreator;
    private ViewHolderBinder<VH> viewHolderBinder;
    private Map<Integer, ViewHolderClickListener<V>> viewHolderClickListeners = new HashMap<>();

    public BaseViewHolderBuilder(ViewHolderCreator<VH> viewHolderCreator) {
        this.viewHolderCreator = viewHolderCreator;
    }

    public ViewHolderCreator<VH> getViewHolderCreator() {
        return viewHolderCreator;
    }

    public ViewHolderBinder<VH> getViewHolderBinder() {
        return viewHolderBinder;
    }

    public void setViewHolderBinder(ViewHolderBinder<VH> viewHolderBinder) {
        this.viewHolderBinder = viewHolderBinder;
    }

    public void addViewHolderClickListener(@IdRes int resId, ViewHolderClickListener<V> viewHolderClickListener) {
        viewHolderClickListeners.put(resId, viewHolderClickListener);
    }

    public Map<Integer, ViewHolderClickListener<V>> getViewHolderClickListeners() {
        return viewHolderClickListeners;
    }

    public BindClickViewClickChainer<V, VH> chainer() {
        return new BindClickViewClickChainer<>(this);
    }

    //region Chainers

    /**
     * this Chainer can chain addViewHolderClickListener, addViewHolderClickListener, addViewHolderBinder
     *
     * @param <V>
     * @param <VH>
     */
    public static class BindClickViewClickChainer<V extends View, VH extends BaseViewHolder<V>> extends BindViewClickChainer<V, VH> {


        public BindClickViewClickChainer(BaseViewHolderBuilder<V, VH> builder) {
            super(builder);
        }

        public BindViewClickChainer<V, VH> addViewHolderClickListener(ViewHolderClickListener<V> viewHolderClickListener) {
            getBuilder().addViewHolderClickListener(NO_ID_CLICK_LISTENER, viewHolderClickListener);
            return new BindViewClickChainer<>(getBuilder());
        }

    }


    /**
     * this Chainer can chain addViewHolderClickListener, addViewHolderBinder
     *
     * @param <V>
     * @param <VH>
     */
    public static class BindViewClickChainer<V extends View, VH extends BaseViewHolder<V>> extends BindChainer<V, VH, BaseViewHolderBuilder<V, VH>> {

        public BindViewClickChainer(BaseViewHolderBuilder<V, VH> builder) {
            super(builder);
        }

        public BindViewClickChainer<V, VH> addViewHolderClickListener(@IdRes int resId, ViewHolderClickListener<V> viewHolderClickListener) {
            getBuilder().addViewHolderClickListener(resId, viewHolderClickListener);
            return new BindViewClickChainer<>(getBuilder());
        }

    }

    /**
     * this Chainer can chain addViewHolderBinder
     *
     * @param <V>
     * @param <VH>
     */
    public static class BindChainer<V extends View, VH extends BaseViewHolder<V>, B extends BaseViewHolderBuilder<V, VH>> {

        private final B builder;

        public BindChainer(B builder) {
            this.builder = builder;
        }

        public void addViewHolderBinder(ViewHolderBinder<VH> viewHolderBinder) {
            builder.setViewHolderBinder(viewHolderBinder);
        }

        public B getBuilder() {
            return builder;
        }
    }

    //endregion


    public interface ViewHolderClickListener<V extends View> {
        void onViewHolderClick(BaseViewHolder<V> viewHolder);
    }
}
