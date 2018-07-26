package com.centalineproperty.basemodule.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.centalineproperty.basemodule.utils.StatusBarUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * Created by sun on 18-3-16.
 */

public abstract class BaseSwipeBackActivity<T extends IPresenter> extends RxAppCompatActivity implements IView, SwipeBackActivityBase {

    protected T mPresenter;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected abstract int createContentView();

    protected abstract T createPresenter();

    protected abstract void initView();

    protected abstract void initData();

    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        setContentView(createContentView());

        StatusBarUtil.setStatusBarColor(this, Color.WHITE, true);

        if (mPresenter == null) mPresenter = createPresenter();
        if (mPresenter != null) mPresenter.attachView(this);

        initView();
        initData();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.detachView();
    }

}
