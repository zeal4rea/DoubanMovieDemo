package com.zeal4rea.doubanmoviedemo.subjects;

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
import com.zeal4rea.doubanmoviedemo.bean.api.Subject;
import com.zeal4rea.doubanmoviedemo.util.view.RatingAndStars;

import java.util.ArrayList;
import java.util.List;

public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder> {
    private Context mContext;
    private List<Subject> mData;
    private SubjectItemListener mListener;

    public SubjectsAdapter(Context context, List<Subject> data, @NonNull SubjectItemListener listener) {
        mContext = context;
        mData = data;
        mListener = listener;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_subjects_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Subject subject = mData.get(position);
        holder.title.setText(subject.title);
        boolean hasStars = RatingAndStars.fillStars(mContext, holder.starsLayout, RatingAndStars.correctRating(subject.rating.average, subject.rating.max));
        if (hasStars) {
            holder.rating.setText(String.valueOf(subject.rating.average));
            holder.rating.setVisibility(View.VISIBLE);
            holder.starsLayout.setVisibility(View.VISIBLE);
            holder.noRating.setVisibility(View.GONE);
        } else {
            holder.rating.setVisibility(View.GONE);
            holder.starsLayout.setVisibility(View.GONE);
            holder.noRating.setVisibility(View.VISIBLE);
        }
        Glide.with(mContext).load(subject.images.small).into(holder.cover);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSubjectItemClick(subject);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Subject> data, boolean add) {
        if (data == null || data.isEmpty())
            return;

        if (add) {
            if (mData == null) {
                mData = new ArrayList<>();
            }
            mData.addAll(data);
        } else {
            mData = data;
        }
        //notifyDataSetChanged();
    }

    public void clear() {
        int itemCount = getItemCount();
        if (itemCount > 0) {
            mData = new ArrayList<>();
            //notifyItemRangeRemoved(0, itemCount);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView cover;
        TextView title;
        TextView noRating;
        TextView rating;
        LinearLayout starsLayout;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.subjects_item$card_view);
            cover = itemView.findViewById(R.id.subjects_item$image_view_cover);
            title = itemView.findViewById(R.id.subjects_item$text_view_title);
            noRating = itemView.findViewById(R.id.subjects_item$text_view_no_rating);
            rating = itemView.findViewById(R.id.subjects_item$text_view_rating);
            starsLayout = itemView.findViewById(R.id.subjects_item$layout_rating_stars);
        }
    }

    public interface SubjectItemListener {
        void onSubjectItemClick(Subject clickedSubject);
    }
}
