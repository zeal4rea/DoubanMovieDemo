package com.zeal4rea.doubanmoviedemo.celebritydetail;

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
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Celebrity;

import java.util.List;

public class RelatedCelebritiesAdapter extends RecyclerView.Adapter<RelatedCelebritiesAdapter.ViewHolder> {
    private final Context context;
    private final List<Celebrity.RelatedCelebrity> celebrities;
    private OnItemClickListener mListener;

    public RelatedCelebritiesAdapter(Context context, List<Celebrity.RelatedCelebrity> celebrities, OnItemClickListener listener) {
        this.context = context;
        this.celebrities = celebrities;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_subjectdetail_content_celebrities_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Celebrity.RelatedCelebrity celebrity = celebrities.get(position);
        holder.name.setText(celebrity.celebrity.name);
        holder.role.setText(celebrity.info);
        Glide.with(context).load(celebrity.celebrity.cover_url).into(holder.avatar);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClicked(celebrity);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return celebrities.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView name;
        TextView role;
        View itemLayout;

        ViewHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.subjectdetail_content$layout_celebrity_item);
            avatar = itemView.findViewById(R.id.subjectdetail_content$image_view_celebrity_avatar);
            name = itemView.findViewById(R.id.subjectdetail_content$text_view_celebrity_name);
            role = itemView.findViewById(R.id.subjectdetail_content$text_view_celebrity_role);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Celebrity.RelatedCelebrity celebrity);
    }
}
