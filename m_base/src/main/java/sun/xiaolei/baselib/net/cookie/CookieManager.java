package sun.xiaolei.baselib.net.cookie;


import sun.xiaolei.baselib.Cradle;
import sun.xiaolei.baselib.net.cookie.cache.SetCookieCache;
import sun.xiaolei.baselib.net.cookie.persistence.SharedPrefsCookiePersistor;

public class CookieManager {

    private static ClearableCookieJar cookieJar;

    public static ClearableCookieJar getCookieJar() {
        if (cookieJar == null) {
            cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Cradle.getContext()));
        }
        return cookieJar;
    }

    public static void clearCookie() {
        if (cookieJar != null) {
            cookieJar.clear();
        }
    }
}
