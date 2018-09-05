package com.zeal4rea.doubanmoviedemo.celebritydetail;

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
import com.zeal4rea.doubanmoviedemo.bean.rexxar.CelebrityWork;
import com.zeal4rea.doubanmoviedemo.util.view.RatingAndStars;

import java.util.List;

public class WorksAdapter extends RecyclerView.Adapter<WorksAdapter.ViewHolder> {
    private Context mContext;
    private List<CelebrityWork> mWorks;
    private OnItemClickListener mListener;

    public WorksAdapter(Context context, List<CelebrityWork> works, OnItemClickListener listener) {
        mContext = context;
        mWorks = works;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_celebrity_works_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CelebrityWork work = mWorks.get(position);
        holder.title.setText(work.title);
        if (work.rating != null && RatingAndStars.fillStars(mContext, holder.layoutStars, RatingAndStars.correctRating(work.rating.value, (int) work.rating.max))) {
            holder.rating.setText(String.valueOf(work.rating.value));
        } else {
            RatingAndStars.fillEmptyStars(mContext, holder.layoutStars);
            holder.rating.setText(String.valueOf(0f));
        }
        Glide.with(mContext).load(work.pic.normal).into(holder.cover);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(work);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWorks == null ? 0 : mWorks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View layout;
        ImageView cover;
        TextView title;
        TextView rating;
        LinearLayout layoutStars;

        public ViewHolder(View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.celebrity_works$image_view_cover);
            title = itemView.findViewById(R.id.celebrity_works$text_view_title);
            rating = itemView.findViewById(R.id.celebrity_works$text_view_rating);
            layoutStars = itemView.findViewById(R.id.celebrity_works$layout_stars);
            layout = itemView.findViewById(R.id.celebrity_works$layout);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(CelebrityWork work);
    }
}
