package com.zeal4rea.doubanmoviedemo.util;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;

import com.zeal4rea.doubanmoviedemo.BuildConfig;
import com.zeal4rea.doubanmoviedemo.base.BaseApplication;
import com.zeal4rea.doubanmoviedemo.base.BaseContants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static float sNoncompatDensity;
    private static float sNoncompatScaledDensity;

    public static void setCustomDensity(Activity activity) {
        setCustomDensity(activity, BaseApplication.getInstance(), 360);
    }

    public static void setCustomDensity(Activity activity, final Application application, int designDpi) {
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();

        if (sNoncompatDensity == 0) {
            sNoncompatDensity = appDisplayMetrics.density;
            sNoncompatScaledDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration configuration) {
                    if (configuration != null && configuration.fontScale > 0) {
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {
                }
            });
        }

        final float targetDensity = appDisplayMetrics.widthPixels / designDpi;
        final float targetScaledDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity);
        final int targetDensityDpi = (int) (targetDensity * 160);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

    public static SparseArray<String> getSparseArraySplitBy(@NonNull String[] arr, String regex) {
        SparseArray<String> result = new SparseArray<>(arr.length);
        for (String s : arr) {
            String[] temp = s.split(regex);
            result.put(Integer.valueOf(temp[0]), temp[1]);
        }
        return result;
    }

    public static CharSequence getString(@StringRes int id) {
        return BaseApplication.getInstance().getString(id);
    }

    public static boolean isTextEmpty(String str) {
        return TextUtils.isEmpty(str) || TextUtils.isEmpty(str.trim()) || "null".equals(str.trim());
    }

    public static boolean isTextEmpty(CharSequence s) {
        return isTextEmpty(s.toString());
    }

    public static int dp2px(int dp) {
        return (int) (BaseApplication.getContext().getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    public static int px2dp(int px) {
        return (int) (px / BaseApplication.getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    public static String[] regexMatch(String regex, String input) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        if (m.find()) {
            if (m.groupCount() >= 1) {
                List<String> groups = new ArrayList<>();
                for (int i = 0; i <= m.groupCount(); i++) {
                    groups.add(m.group(i));
                }
                String[] strings = new String[groups.size()];
                groups.toArray(strings);
                return strings;
            }
            return new String[]{m.group()};
        }
        return new String[]{""};
    }

    public static void logWithDebugingTag(String message) {
        if (BuildConfig.showLog) {
            Log.d(BaseContants.DEBUGING_TAG, message);
        }
    }

    public static void logWithTag(String tag, String message) {
        if (BuildConfig.showLog) {
            Log.d(tag, message);
        }
    }

    public static void sharePreferencePutBoolean(String key, boolean value) {
        BaseApplication.getContext().getSharedPreferences(BaseApplication.getInstance().getPackageName(), Context.MODE_PRIVATE).edit().putBoolean(key, value).apply();
    }

    public static boolean sharePreferenceGetBoolean(String key, boolean defaultValue) {
        return BaseApplication.getContext().getSharedPreferences(BaseApplication.getInstance().getPackageName(), Context.MODE_PRIVATE).getBoolean(key, defaultValue);
    }
}
