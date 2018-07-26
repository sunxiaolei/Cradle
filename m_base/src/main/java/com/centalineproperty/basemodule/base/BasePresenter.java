package com.centalineproperty.basemodule.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by sun on 18-3-19.
 */

public class BasePresenter<T extends IView> implements IPresenter<T> {

    protected T mView;
    private CompositeDisposable mDisposable;

    protected void addDisposable(Disposable disposable) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(disposable);
    }

    protected void removeDisposable() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
        removeDisposable();
    }
}
