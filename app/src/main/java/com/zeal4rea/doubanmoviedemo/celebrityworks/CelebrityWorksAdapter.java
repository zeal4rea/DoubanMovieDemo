package com.zeal4rea.doubanmoviedemo.celebrityworks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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
import com.zeal4rea.doubanmoviedemo.bean.rexxar.CelebrityWorkWrapper;
import com.zeal4rea.doubanmoviedemo.util.view.RatingAndStars;

import java.util.ArrayList;
import java.util.List;

public class CelebrityWorksAdapter extends RecyclerView.Adapter<CelebrityWorksAdapter.ViewHolder> {
    private final Context mContext;
    private final OnItemClickListener mListener;
    private List<CelebrityWorkWrapper> mWorks;

    public CelebrityWorksAdapter(Context context, List<CelebrityWorkWrapper> works, OnItemClickListener listener) {
        mContext = context;
        mWorks = works;
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CelebrityWorkWrapper workWrapper = mWorks.get(position);
        final CelebrityWork work = workWrapper.work;
        Glide.with(mContext).load(work.pic.normal).into(holder.cover);
        holder.title.setText(work.title);
        holder.title.setTextSize(16);
        holder.meta.setText(buildMeta(work));
        holder.roles.setText(workWrapper.roles[0]);
        holder.meta.setVisibility(View.VISIBLE);
        holder.roles.setVisibility(View.VISIBLE);
        if (work.rating != null) {
            if (!RatingAndStars.fillStars(mContext, holder.layoutStars, RatingAndStars.correctRating(work.rating.value, (int) work.rating.max))) {
                RatingAndStars.fillEmptyStars(mContext, holder.layoutStars);
            }
            holder.rating.setText(String.valueOf(work.rating.value));
            holder.noRating.setVisibility(View.GONE);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(work);
                }
            }
        });
    }

    private String buildMeta(CelebrityWork work) {
        String separator = " / ";
        StringBuilder sb = new StringBuilder();
        if (work.genres != null && work.genres.length > 0) {
            for (String s : work.genres) {
                sb.append(s).append(separator);
            }
        }
        if (work.pubdate != null && work.pubdate.length > 0) {
            for (String s : work.pubdate) {
                sb.append(s).append(separator);
            }
        }
        if (work.actors != null && work.actors.length > 0) {
            for (CelebrityWork.PersonWithName p : work.actors) {
                sb.append(p.name).append(separator);
            }
        }
        String result = sb.toString();
        if (result.endsWith(separator)) {
            result = result.substring(0, result.length() - 3);
        }
        return result;
    }

    public void setData(List<CelebrityWorkWrapper> works, boolean add) {
        if (works == null || works.isEmpty()) {
            return;
        }

        if (add) {
            if (mWorks == null) {
                mWorks = new ArrayList<>();
            }
            mWorks.addAll(works);
        } else {
            mWorks = works;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_subjects_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mWorks == null ? 0 : mWorks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView cover;
        TextView title;
        TextView rating;
        TextView noRating;
        TextView meta;
        TextView roles;
        LinearLayout layoutStars;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.subjects_item$card_view);
            cover = itemView.findViewById(R.id.subjects_item$image_view_cover);
            title = itemView.findViewById(R.id.subjects_item$text_view_title);
            rating = itemView.findViewById(R.id.subjects_item$text_view_rating);
            noRating = itemView.findViewById(R.id.subjects_item$text_view_no_rating);
            meta = itemView.findViewById(R.id.subjects_item$text_view_spare1);
            roles = itemView.findViewById(R.id.subjects_item$text_view_spare2);
            layoutStars = itemView.findViewById(R.id.subjects_item$layout_rating_stars);
        }
    }

    interface OnItemClickListener {
        void onItemClick(CelebrityWork work);
    }
}
