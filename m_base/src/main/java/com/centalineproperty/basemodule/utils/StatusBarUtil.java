package com.centalineproperty.basemodule.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 状态栏的工具类
 */
public class StatusBarUtil {

    private static final int COLOR_TRANSLUCENT = Color.parseColor("#00000000");

    public static final int DEFAULT_COLOR_ALPHA = 112;


    /**
     * 设置状态栏颜色
     *
     * @param activity
     * @param statusColor
     */
    public static void setStatusBarColor(Activity activity, int statusColor, boolean changeTextColor) {
        Window window = activity.getWindow();
        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //First translucent status bar.
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //5.0以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //After LOLLIPOP not translucent status bar
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(statusColor);//calculateStatusColor(Color.WHITE, (int) alphaValue)
                //set child View not fill the system window
                View mChildView = mContentView.getChildAt(0);
                if (mChildView != null) {
                    ViewCompat.setFitsSystemWindows(mChildView, true);
                }
            } else {
                //4.4 ~ 5.0
                int flag = activity.getWindow().getAttributes().flags;
                if ((flag & WindowManager.LayoutParams.FLAG_FULLSCREEN) != WindowManager.LayoutParams.FLAG_FULLSCREEN) {    //不是全屏的
                    ViewGroup mDecorView = (ViewGroup) window.getDecorView();
                    if (mDecorView.getTag() != null && mDecorView.getTag() instanceof Boolean && (Boolean) mDecorView.getTag()) {
                        //如果已经有了假的状态栏
                        View mStatusBarView = mDecorView.getChildAt(0);
                        if (mStatusBarView != null) {
                            mStatusBarView.setBackgroundColor(statusColor);
                        }
                    } else {
                        int statusBarHeight = getStatusBarHeight(activity);
                        //为假的toolbar预留间隙
                        View mContentChild = mContentView.getChildAt(0);
                        if (mContentChild != null) {
                            ViewCompat.setFitsSystemWindows(mContentChild, false);
                            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContentChild.getLayoutParams();
                            lp.topMargin += statusBarHeight;
                            mContentChild.setLayoutParams(lp);
                        }
                        //添加一个假的view作为状态栏
                        View mStatusBarView = new View(activity);
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
                        layoutParams.gravity = Gravity.TOP;
                        mStatusBarView.setLayoutParams(layoutParams);
                        mStatusBarView.setBackgroundColor(statusColor);
                        mDecorView.addView(mStatusBarView, 0);
                        mDecorView.setTag(true);
                    }
                }
            }

            //设置状态栏字体颜色
            StatusBarLightMode(activity, changeTextColor);
        }
    }

    public static void translucentStatusBar(Activity activity) {
        translucentStatusBar(activity, false);
    }

    /**
     * change to full screen mode
     * 头部是图片状态栏透明,内容延伸到状态栏情况使用
     *
     * @param activity
     * @param hideStatusBarBackground hide status bar alpha Background when SDK > 21, true if hide it
     */
    public static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        Window window = activity.getWindow();
        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);

        //set child View not fill the system window
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //5.0以上
            int statusBarHeight = getStatusBarHeight(activity);

            //First translucent status bar.
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //After LOLLIPOP just set LayoutParams.
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (hideStatusBarBackground) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.setStatusBarColor(COLOR_TRANSLUCENT);
                } else {
                    window.setStatusBarColor(calculateStatusBarColor(COLOR_TRANSLUCENT, DEFAULT_COLOR_ALPHA));
                }
                //must call requestApplyInsets, otherwise it will have space in screen bottom
                if (mChildView != null) {
                    ViewCompat.requestApplyInsets(mChildView);
                }
            } else {                                                        //4.4~5.0
                ViewGroup mDecorView = (ViewGroup) window.getDecorView();
                if (mDecorView.getTag() != null && mDecorView.getTag() instanceof Boolean && (Boolean) mDecorView.getTag()) {
                    mChildView = mDecorView.getChildAt(0);
                    //remove fake status bar view.
                    mContentView.removeView(mChildView);
                    mChildView = mContentView.getChildAt(0);
                    if (mChildView != null) {
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
                        //cancel the margin top
                        if (lp != null && lp.topMargin >= statusBarHeight) {
                            lp.topMargin -= statusBarHeight;
                            mChildView.setLayoutParams(lp);
                        }
                    }
                    mDecorView.setTag(false);
                }
            }
        }
    }

    //Get status bar height
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }
        return result;
    }

    //Get alpha color
    private static int calculateStatusBarColor(int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int StatusBarLightMode(Activity activity, boolean changeTextColor) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(activity.getWindow(), changeTextColor) && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), changeTextColor)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (changeTextColor) {
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }
                result = 3;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.getWindow().setStatusBarColor(Color.parseColor("#80000000"));
                result = 4;
            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window          需要设置的窗口
     * @param changeTextColor 是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean changeTextColor) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (changeTextColor) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    @TargetApi(21)
    public static boolean andoirdMLightMode(Window window, boolean dark) {
        boolean result = false;
        if (dark) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            result = true;
        }
        return result;
    }
}