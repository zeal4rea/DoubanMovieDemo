package com.zeal4rea.doubanmoviedemo.util.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;

public class ImageUtil {
    public static Bitmap getBase64Bitmap(@NonNull String str) {
        Bitmap bitmap = null;
        try {
            byte[] bytes = Base64.decode(str.split(",")[1], Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
