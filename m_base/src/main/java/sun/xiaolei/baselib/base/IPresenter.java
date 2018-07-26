package sun.xiaolei.baselib.base;

/**
 * Created by sun on 18-3-19.
 */

public interface IPresenter<T extends IView> {

    void attachView(T view);

    void detachView();
}
