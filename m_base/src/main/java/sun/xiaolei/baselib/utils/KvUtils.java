package sun.xiaolei.baselib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.tencent.mmkv.MMKV;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author sun
 * @emil xiaoleisun92@gmail.com
 * description:key-value存储
 */
public class KvUtils {

    private static MMKV kv;
    private static SharedPreferences.Editor editor;

    public static String SP_NAME = "SP";

    public static void init(Context context) {
        MMKV.initialize(context);
        kv = MMKV.mmkvWithID(SP_NAME);
        //旧数据迁移
        SharedPreferences old_sp = context.getSharedPreferences(SP_NAME, MODE_PRIVATE);
        kv.importFromSharedPreferences(old_sp);
        old_sp.edit().clear();
        editor = kv.edit();
    }

    public static void setInt(String key, int value) {
        editor.putInt(key, value);
    }

    public static int getInt(String key, int defVale) {
        return kv.getInt(key, defVale);
    }

    public static void setString(String key, String value) {
        editor.putString(key, value);
    }

    public static String getString(String key, String defVale) {
        return kv.getString(key, defVale);
    }

    public static void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
    }

    public static boolean getBoolean(String key, boolean defVale) {
        return kv.getBoolean(key, defVale);
    }

    public static void setLong(String key, long value) {
        editor.putLong(key, value);
    }

    public static long getLong(String key, long value) {
        return kv.getLong(key, value);
    }

    public static void setFloat(String key, float value) {
        editor.putFloat(key, value);
    }

    public static float getFloat(String key, float defVale) {
        return kv.getFloat(key, defVale);
    }

    public static void remove(String key) {
        editor.remove(key);
    }

    public static void clean() {
        editor.clear();
    }

}
