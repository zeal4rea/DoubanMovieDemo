package com.zeal4rea.doubanmoviedemo.util.permission;

public interface BasePermissionListener {
    void onPermissionGranted();
    void onPermissionDenied();
    void onShouldShowRequestPermissionRationale();
}
