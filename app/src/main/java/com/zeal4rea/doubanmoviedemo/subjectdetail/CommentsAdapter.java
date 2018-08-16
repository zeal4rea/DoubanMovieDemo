package com.zeal4rea.doubanmoviedemo.subjectdetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Comment4J;
import com.zeal4rea.doubanmoviedemo.util.view.RatingAndStars;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private final Context context;
    private final List<Comment4J> comments;

    public CommentsAdapter(Context context, List<Comment4J> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_subjectdetail_content_comments_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < 5) {
            Comment4J comment = comments.get(position);
            holder.name.setText(comment.user.name);
            holder.time.setText(comment.date);
            holder.votes.setText(comment.votes);
            holder.content.setText(comment.content);
            Glide.with(context).load(comment.user.iconUrl).into(holder.icon);
            RatingAndStars.fillStars(context, holder.stars, RatingAndStars.correctRating(Integer.valueOf(comment.rating), RatingAndStars.TYPE_50));
        }
    }

    @Override
    public int getItemCount() {
        return comments.size() > 5 ? 5 : comments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        TextView time;
        TextView content;
        TextView votes;
        LinearLayout stars;
        ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.subjectdetail_content$image_view_commenter_icon);
            name = itemView.findViewById(R.id.subjectdetail_content$text_view_commenter_name);
            time = itemView.findViewById(R.id.subjectdetail_content$text_view_comment_time);
            content = itemView.findViewById(R.id.subjectdetail_content$text_view_comment_content);
            votes = itemView.findViewById(R.id.subjectdetail_content$text_view_comment_votes);
            stars = itemView.findViewById(R.id.subjectdetail_content$linear_layout_comment_stars);
        }
    }
}
