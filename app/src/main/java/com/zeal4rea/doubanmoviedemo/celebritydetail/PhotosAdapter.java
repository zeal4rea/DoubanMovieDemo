package com.zeal4rea.doubanmoviedemo.celebritydetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Photo;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    private final Context context;
    private final List<Photo> photos;
    private OnItemClickListener mListener;

    public PhotosAdapter(Context context, List<Photo> photos, OnItemClickListener listener) {
        this.context = context;
        this.photos = photos;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_subjectdetail_content_photos_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (position < 10) {
            holder.photo.setImageDrawable(null);
            holder.photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClick(photos, holder.getAdapterPosition());
                    }
                }
            });
            Glide.with(context).load(photos.get(position).image.small.url).into(holder.photo);
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

    interface OnItemClickListener {
        void onItemClick(List<Photo> photos, int position);
    }
}
