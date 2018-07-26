package com.centalineproperty.basemodule.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;


/**
 * Created by sun on 18-3-16.
 */

public abstract class BaseFragment<T extends IPresenter> extends RxFragment implements IView {

    protected T mPresenter;

    protected Activity mActivity;

    protected abstract int createContentView();

    protected abstract T createPresenter();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract boolean isLazyLoad();

    private View rootView;

    // 标志位，标志已经初始化完成，因为setUserVisibleHint是在onCreateView之前调用的，
    // 在视图未初始化的时候，在lazyLoad当中就使用的话，就会有空指针的异常
    private boolean isPrepared;
    //标志当前页面是否可见
    private boolean isVisible;
    //是否已经加载过
    private boolean isLoaded;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(createContentView(), null);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = getActivity();
        if (mPresenter == null) mPresenter = createPresenter();
        if (mPresenter != null) mPresenter.attachView(this);
        initView();
        isPrepared = true;
        if (isLazyLoad()) {
            lazyLoad();
        } else {
            initData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //懒加载
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }

    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    protected void lazyLoad() {
        if (!isVisible || !isPrepared) {
            return;
        }

        if (!isLoaded) {
            initData();
            isLoaded = true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) mPresenter.detachView();
    }

    protected <V extends View> V findViewById(int id) {
        return rootView.findViewById(id);
    }
}
