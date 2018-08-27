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
import com.bumptech.glide.request.RequestOptions;
import com.zeal4rea.doubanmoviedemo.R;
import com.zeal4rea.doubanmoviedemo.bean.rexxar.Comment;
import com.zeal4rea.doubanmoviedemo.util.view.GlideCircleTransform;
import com.zeal4rea.doubanmoviedemo.util.view.RatingAndStars;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private final Context context;
    private List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_subjectdetail_content_comments_item, parent, false);
        return new ViewHolder(view);
    }

    public void setData(List<Comment> comments, boolean add) {
        if (add) {
            int positionStart = getItemCount();
            this.comments.addAll(comments);
            notifyItemRangeInserted(positionStart, comments.size());
        } else {
            this.comments = comments;
            notifyDataSetChanged();
        }
    }

    public void clear() {
        int itemCount = getItemCount();
        if (itemCount > 0) {
            comments.clear();
            notifyItemRangeRemoved(0, itemCount);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.name.setText(comment.user.name);
        holder.time.setText(comment.create_time);
        holder.votes.setText(comment.vote_count);
        holder.content.setText(comment.comment);
        Glide.with(context).load(comment.user.avatar).apply(new RequestOptions().transform(new GlideCircleTransform())).into(holder.icon);
        RatingAndStars.fillStars(context, holder.stars, RatingAndStars.correctRating(comment.rating.value, (int) comment.rating.max));
    }

    @Override
    public int getItemCount() {
        return comments == null ? 0 : comments.size();
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
