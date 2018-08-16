package com.zeal4rea.doubanmoviedemo.subjectdetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.bean.api.Celebrity;

import java.util.List;

public class CelebritiesAdapter extends RecyclerView.Adapter<CelebritiesAdapter.ViewHolder> {
    private final Context context;
    private final List<Celebrity> celebrities;

    public CelebritiesAdapter(Context context, List<Celebrity> celebrities) {
        this.context = context;
        this.celebrities = celebrities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_subjectdetail_content_celebrities_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Celebrity celebrity = celebrities.get(position);
        holder.name.setText(celebrity.name);
        holder.role.setText(celebrity.role);
        Glide.with(context).load(celebrity.avatars.small).into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return celebrities.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView name;
        TextView role;
        public ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.subjectdetail_content$image_view_celebrity_avatar);
            name = itemView.findViewById(R.id.subjectdetail_content$text_view_celebrity_name);
            role = itemView.findViewById(R.id.subjectdetail_content$text_view_celebrity_role);
        }
    }
}
