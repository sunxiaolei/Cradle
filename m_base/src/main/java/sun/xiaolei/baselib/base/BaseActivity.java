package sun.xiaolei.baselib.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;

import com.alibaba.android.arouter.launcher.ARouter;
import sun.xiaolei.baselib.utils.StatusBarUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by sun on 18-3-16.
 */

public abstract class BaseActivity<T extends IPresenter> extends RxAppCompatActivity implements IView {

    protected T mPresenter;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected abstract int createContentView();

    protected abstract T createPresenter();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(createContentView());

        StatusBarUtil.setStatusBarColor(this, Color.WHITE, true);

        if (mPresenter == null) mPresenter = createPresenter();
        if (mPresenter != null) mPresenter.attachView(this);

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.detachView();
    }

}
