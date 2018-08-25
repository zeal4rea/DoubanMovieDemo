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
import com.zeal4rea.doubanmoviedemo.bean.jsoup.Review4J;
import com.zeal4rea.doubanmoviedemo.util.view.GlideCircleTransform;
import com.zeal4rea.doubanmoviedemo.util.view.RatingAndStars;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private final Context context;
    private final List<Review4J> reviews;

    public ReviewsAdapter(Context context, List<Review4J> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_subjectdetail_content_reviews_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < 5) {
            Review4J review = reviews.get(position);
            holder.title.setText(review.title);
            holder.useful.setText(review.useful);
            holder.content.setText(review.content);
            RatingAndStars.fillStars(context, holder.stars, RatingAndStars.correctRating(Integer.valueOf(review.rating), RatingAndStars.TYPE_100));
            Glide.with(context).load(review.user.iconUrl).apply(new RequestOptions().transform(new GlideCircleTransform())).into(holder.icon);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size() > 5 ? 5 : reviews.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        TextView content;
        TextView useful;
        LinearLayout stars;
        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.subjectdetail_content$text_view_review_title);
            icon = itemView.findViewById(R.id.subjectdetail_content$image_view_review_reviewer_icon);
            content = itemView.findViewById(R.id.subjectdetail_content$text_view_review_content);
            useful = itemView.findViewById(R.id.subjectdetail_content$text_view_review_useful);
            stars = itemView.findViewById(R.id.subjectdetail_content$linear_layout_review_stars);
        }
    }
}
