package sun.xiaolei.baselib.utils;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import java.util.HashMap;

/**
 * @author sun
 * @emil xiaoleisun92@gmail.com
 * description:权限申请
 */
public class PermissionUtils {

    public static final String TAG = "PermissionUtils";
    private static HashMap<String, PermissionListener> listenerMap = new HashMap();


    /**
     * 申请授权
     *
     * @param context
     * @param listener
     * @param permission 需要申请授权的权限
     */
    public static void requestPermission(@NonNull Context context, @NonNull PermissionListener listener, @NonNull String... permission) {
        if (listener == null) {
            Log.e(TAG, "listener is null");
            return;
        }

        if (PermissionUtils.hasPermission(context, permission)) {
            listener.permissionGranted(permission);
        } else {
            if (Build.VERSION.SDK_INT < 23) {
                listener.permissionDenied(permission);
            } else {
                String key = String.valueOf(System.currentTimeMillis());
                listenerMap.put(key, listener);
                Intent intent = new Intent(context, PermissionActivity.class);
                intent.putExtra("permission", permission);
                intent.putExtra("key", key);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        }
    }


    /**
     * 判断权限是否授权
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {
        if (permissions.length == 0) {
            return false;
        }

        for (String per : permissions) {
            int result = PermissionChecker.checkSelfPermission(context, per);
            if (result != PermissionChecker.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断一组授权结果是否为授权通过
     *
     * @param grantResult
     * @return
     */
    public static boolean isGranted(@NonNull int... grantResult) {
        if (grantResult.length == 0) {
            return false;
        }

        for (int result : grantResult) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param key
     * @return
     */
    static PermissionListener fetchListener(String key) {
        return listenerMap.remove(key);
    }


    public interface PermissionListener {
        /**
         * 通过授权
         *
         * @param permission
         */
        void permissionGranted(@NonNull String[] permission);

        /**
         * 拒绝授权
         *
         * @param permission
         */
        void permissionDenied(@NonNull String[] permission);
    }

}
