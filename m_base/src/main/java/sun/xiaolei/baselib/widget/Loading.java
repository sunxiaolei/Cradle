package sun.xiaolei.baselib.widget;

import android.app.Activity;

import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

/**
 * @author sun
 * @emil xiaoleisun92@gmail.com
 * description:
 */
public class Loading {

    private static LoadingDialog loadingDialog;

    public static void showLoading(Activity activity) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(activity);
        }
        loadingDialog.show();
    }

    public static void disLoading() {
        if (loadingDialog != null) {
            loadingDialog.close();
            loadingDialog = null;
        }
    }
}
