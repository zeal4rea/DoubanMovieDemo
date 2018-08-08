package com.zeal4rea.doubanmoviedemo.util.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.Collection;
import java.util.Collections;

public class PermissionUtil {
    private Activity mActivity;
    private BasePermissionListener mListener;
    private Collection<String> permissions;

    private PermissionUtil(Activity activity) {
        mActivity = activity;
    }
    public static PermissionUtil withActivity(Activity activity) {
        return new PermissionUtil(activity);
    }

    public PermissionUtil withPermission(String permission) {
        permissions = Collections.singletonList(permission);
        return this;
    }

    public PermissionUtil withListener(BasePermissionListener listener) {
        mListener = listener;
        return this;
    }

    public void check() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mActivity, permissions.iterator().next()) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissions.iterator().next())) {
                    if (mListener != null) {
                        mListener.onShouldShowRequestPermissionRationale();
                    }
                } else {
                    if (mListener != null) {
                        mListener.onPermissionDenied();
                    }
                }
            } else {
                if (mListener != null) {
                    mListener.onPermissionGranted();
                }
            }
        }
    }
}
