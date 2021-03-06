package com.zeal4rea.doubanmoviedemo.gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeal4rea.doubanmoviedemo.R;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private final Context context;
    private List<String> photos;
    private OnItemClickListener onItemClickListener;

    public GalleryAdapter(Context context, @NonNull List<String> photos, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.photos = photos;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_gallery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (photos != null && !photos.isEmpty()) {
            String url = photos.get(position);
            Glide.with(context).load(url).into(holder.photo);
            if (onItemClickListener != null) {
                holder.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onItemClick(photos, holder.getAdapterPosition());
                    }
                });
            }
        }
    }

    public void setData(List<String> data, boolean add) {
        if (data == null || data.isEmpty())
            return;

        if (add) {
            if (photos == null) {
                photos = new ArrayList<>();
            }
            photos.addAll(data);
        } else {
            photos = data;
        }
        //notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return photos == null ? 0 : photos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;

        ViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.gallery$image_view_item_photo);
        }
    }

    interface OnItemClickListener {
        void onItemClick(List<String> photos, int position);
    }
}
