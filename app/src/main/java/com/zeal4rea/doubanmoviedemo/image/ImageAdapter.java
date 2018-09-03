package com.zeal4rea.doubanmoviedemo.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zeal4rea.doubanmoviedemo.R;

import java.util.List;

public class ImageAdapter extends PagerAdapter {

    private final LayoutInflater layoutInflater;
    private List<String> photos;

    public ImageAdapter(Context context, List<String> photos) {
        layoutInflater = LayoutInflater.from(context);
        this.photos = photos;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View layout = layoutInflater.inflate(R.layout.layout_image_item, container, false);
        final ProgressBar progressBar = layout.findViewById(R.id.image$progress_bar);
        final ImageView content = layout.findViewById(R.id.image$image_view_content);

        Glide
                .with(content.getContext().getApplicationContext())
                .load(photos.get(position))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        content.setImageDrawable(resource);
                        progressBar.setVisibility(View.GONE);
                    }
                });
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return photos == null ? 0 : photos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
