package com.zeal4rea.doubanmoviedemo.subjectdetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Photo4J;
import com.zeal4rea.doubanmoviedemo.util.Utils;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    private final Context context;
    private final List<Photo4J> photos;

    public PhotosAdapter(Context context, List<Photo4J> photos) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_subjectdetail_content_photos_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < 10) {
            holder.photo.setImageDrawable(null);
            Glide.with(context).load(photos.get(position).small).into(holder.photo);
        }
    }

    @Override
    public int getItemCount() {
        return photos.size() > 10 ? 10 : photos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView photo;

        ViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.subjectdetail_content_photots$image_view_item);
        }
    }
}
