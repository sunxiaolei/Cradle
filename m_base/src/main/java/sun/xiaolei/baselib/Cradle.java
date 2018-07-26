package sun.xiaolei.baselib;

import android.content.Context;

/*
 * @author sun
 * @emil sunxl8@centaline.com.cn
 * create at 18-7-26
 * description:
 */
public class Cradle {

    private static Context context;

    public static void init(Context context) {
        Cradle.context = context.getApplicationContext();
    }

    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("Should init Cradle first");
    }
}
