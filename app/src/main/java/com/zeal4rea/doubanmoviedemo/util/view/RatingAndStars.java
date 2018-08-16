package com.zeal4rea.doubanmoviedemo.util.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.util.image.ImageUtil;

public class RatingAndStars {
    public static final int TYPE_5 = 5;
    public static final int TYPE_10 = 10;
    public static final int TYPE_50 = 50;
    public static final int TYPE_100 = 100;

    public static int correctRating(int oldRating, int type) {
        switch (type) {
            case TYPE_5:
                return oldRating * 20;
            case TYPE_10:
                return oldRating * 10;
            case TYPE_50:
                return oldRating * 2;
            case TYPE_100:
            default:
                return oldRating;
        }
    }

    public static int correctRating(float oldRating, int type) {
        switch (type) {
            case TYPE_5:
                return (int) (oldRating * 20);
            case TYPE_10:
                return (int) (oldRating * 10);
            case TYPE_50:
                return (int) (oldRating * 2);
            case TYPE_100:
            default:
                return (int) oldRating;
        }
    }

    public static boolean fillStars(Context context, ViewGroup stars, int rating100) {
        stars.removeAllViews();
        if (rating100 > 0) {
            int low = rating100 / 20;
            int high = (int) (rating100 / 20 + 0.5);
            for (int i = 0; i < low; i++) {
                addStar(context, stars, 0);
            }
            if (low != high) {
                addStar(context, stars, 1);
            }
            for (int i = 0; i < 5 - high; i++) {
                addStar(context, stars, 2);
            }
            return true;
        } else {
            return false;
        }
    }

    private static void addStar(Context context, ViewGroup stars, int type) {
        int base64 = R.string.star_empty_base64;
        switch (type) {
            case 0:
                base64 = R.string.star_full_base64;
                break;
            case 1:
                base64 = R.string.star_half_base64;
                break;
            case 2:
                base64 = R.string.star_empty_base64;
                break;
        }
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(lp);
        imageView.setImageBitmap(ImageUtil.getBase64Bitmap(context.getString(base64)));
        stars.addView(imageView);
    }
}
