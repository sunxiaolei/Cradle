package com.centalineproperty.basemodule.utils;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * rx切换线程
 */

public class RxScheduleUtils {

    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> scheduleTnansform() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> flowable) {
                return flowable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }

        };
    }

}
