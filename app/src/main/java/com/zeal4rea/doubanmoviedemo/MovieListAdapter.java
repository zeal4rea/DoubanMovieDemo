package com.zeal4rea.doubanmoviedemo;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zeal4rea.doubanmoviedemo.bean.Subject;
import com.zeal4rea.doubanmoviedemo.util.image.ImageUtil;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private Context mContext;
    private List<Subject> mData;

    public MovieListAdapter(Context context, List<Subject> data) {
        mContext = context;
        mData = data;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_movie_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Subject subject = mData.get(position);
        holder.title.setText(subject.title);
        displayStars(holder, subject);
        Glide.with(mContext).load(subject.images.small).into(holder.cover);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Snackbar.make(view, subject.title, BaseTransientBottomBar.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, subject.title, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void displayStars(ViewHolder holder, Subject subject) {
        holder.starsLayout.removeAllViews();
        if (subject.rating.average > 0) {
            holder.noRating.setVisibility(View.GONE);
            int low = (int) subject.rating.average / 2;
            int high = (int) (subject.rating.average / 2 + 0.5);
            for (int i = 0; i < low; i++) {
                addStar(holder.starsLayout, 0);
            }
            if (low != high) {
                addStar(holder.starsLayout, 1);
            }
            for (int i = 0; i < 5 - high; i++) {
                addStar(holder.starsLayout, 2);
            }
        } else {
            holder.noRating.setVisibility(View.VISIBLE);
        }
    }

    private void addStar(LinearLayout layout, int type) {
        int base64 = R.string.star_empty_base64;
        switch (type) {
            case 0:
                base64 = R.string.star_full_base64;
                break;
            case 1:
                base64 = R.string.star_half_base64;
                break;
            case 2:
                base64 = R.string.star_empty_base64;
                break;
        }
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(lp);
        imageView.setImageBitmap(ImageUtil.getBase64Bitmap(mContext.getString(base64)));
        layout.addView(imageView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Subject> data) {
        mData = data;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView cover;
        TextView title;
        TextView noRating;
        LinearLayout starsLayout;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.movie_item$card_view);
            cover = itemView.findViewById(R.id.movie_item$image_view_cover);
            title = itemView.findViewById(R.id.movie_item$text_view_title);
            noRating = itemView.findViewById(R.id.movie_item$text_view_no_rating);
            starsLayout = itemView.findViewById(R.id.movie_item$layout_rating_stars);
        }
    }
}
