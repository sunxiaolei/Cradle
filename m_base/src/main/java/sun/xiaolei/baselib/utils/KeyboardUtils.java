package sun.xiaolei.baselib.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * 键盘相关
 */
public class KeyboardUtils {

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, final EditText editText) {
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            Flowable.timer(500, TimeUnit.MILLISECONDS)
                    .compose(RxScheduleUtils.<Long>scheduleTnansform())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            editText.setFocusable(true);
                            editText.setFocusableInTouchMode(true);
                            editText.requestFocus();
                            imm.showSoftInput(editText, 0);
                        }
                    });

        }
    }

    /**
     * 隐藏键盘
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 监听键盘状态
     *
     * @param listener
     */
    public static void addKeyBoadrListener(final Activity activity, final KeyBoardChangeListener listener) {
        activity.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        final Rect r = new Rect();
                        //r will be populated with the coordinates of your view that area still visible.
                        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);

                        final int heightDiff = activity.getWindow().getDecorView().getRootView().getHeight() - (r.bottom - r.top);
                        if (heightDiff > 300) {
                            listener.status(true);
                        } else {
                            listener.status(false);
                        }
                    }
                }
        );
    }

    public interface KeyBoardChangeListener {
        void status(boolean status);
    }
}
