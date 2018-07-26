package sun.xiaolei.baselib.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.centalineproperty.basemodule.R;

import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import sun.xiaolei.baselib.Cradle;

/**
 * Toast
 */

public class ToastUtils {

    public static Toast toast;

    public static void showLong(String msg) {
        Toast.makeText(Cradle.getContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static void showShort(String msg) {
        Toast.makeText(Cradle.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(int msgres) {
        Toast.makeText(Cradle.getContext(), Cradle.getContext().getString(msgres), Toast.LENGTH_SHORT).show();
    }

    public static void showTop(Activity activity, View view, String msg) {
//        if (toast == null) {
//            toast = new Toast(view.getContext());
//            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View v = inflater.inflate(R.layout.view_toast_top, null);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(), SizeUtils.dp2px(30));
//            TextView tv = v.findViewById(R.id.tv_toast);
//            tv.setLayoutParams(layoutParams);
//            tv.setText(msg);
//            toast.setView(v);
//            toast.setDuration(Toast.LENGTH_SHORT);
//        } else {
//            TextView tv = toast.getView().findViewById(R.id.tv_toast);
//            tv.setText(msg);
//        }
//        int[] pos = new int[2];
//        view.getLocationOnScreen(pos);
//        toast.setGravity(Gravity.TOP, 0, pos[1] - StatusBarUtil.getStatusBarHeight(view.getContext()));
//        toast.show();
        Style style = new Style.Builder()
                .setTextColorValue(Color.WHITE)
                .setBackgroundColorValue(Color.parseColor("#99000000"))
                .setConfiguration(new Configuration.Builder().setDuration(1000).build())
                .build();

        Crouton.makeText(activity, msg, style, (ViewGroup) view.getParent()).show();
    }
    public static void showTop(Activity activity, int view, String msg) {
        Style style = new Style.Builder()
                .setTextColorValue(Color.WHITE)
                .setBackgroundColorValue(Color.parseColor("#99000000"))
                .setConfiguration(new Configuration.Builder().setDuration(1000).build())
                .build();

        Crouton.makeText(activity, msg, style, view).show();
    }

}
