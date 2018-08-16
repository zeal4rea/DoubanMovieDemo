package com.zeal4rea.doubanmoviedemo.util;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;
import android.util.SparseArray;

import com.zeal4rea.doubanmoviedemo.BuildConfig;
import com.zeal4rea.doubanmoviedemo.base.BaseApplication;
import com.zeal4rea.doubanmoviedemo.base.BaseContants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
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

    public static int dp2px(Context context, int dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    public static int px2dp(Context context, int px) {
        return (int) (px / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static String[] regexMatch(String regex, String input) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        if (m.find()) {
            if (m.groupCount() > 1) {
                List<String> groups = new ArrayList<>();
                for (int i = 0; i <= m.groupCount(); i++) {
                    groups.add(m.group(i));
                }
                return (String[]) groups.toArray();
            }
            return new String[] {m.group()};
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
}
