package sun.xiaolei.baselib.base;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

import sun.xiaolei.baselib.Cradle;

/**
 * @author sun
 * @emil xiaoleisun92@gmail.com
 * description:
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Cradle.init(this);
        ARouter.init(this);
    }
}
