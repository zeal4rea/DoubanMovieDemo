package com.zeal4rea.doubanmoviedemo.util;

import android.support.annotation.NonNull;
import android.util.SparseArray;

public class Utils {
    public static SparseArray<String> getSparseArraySplitBy(@NonNull String[] arr, String regex) {
        SparseArray<String> result = new SparseArray<>(arr.length);
        for (String s : arr) {
            String[] temp = s.split(regex);
            result.put(Integer.valueOf(temp[0]), temp[1]);
        }
        return result;
    }
}
