package com.project.reshoe_fbu.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reshoe_fbu.R;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.project.reshoe_fbu.activities.fragments.DetailShoeFragment;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.Review;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    public static String TAG = "ReviewsAdapter";

    private final Context mContext;
    private final List<Review> reviews;
    private final FragmentManager fragmentManager;

    public ReviewsAdapter(Context context, List<Review> reviews, FragmentManager fragmentManager) {
        this.mContext = context;
        this.reviews = reviews;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_post, parent,
                false);
        return new ReviewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ReviewsAdapter.ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView ivReviewerProfile;
        private final TextView tvReviewerUser, tvReview;
        private final RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivReviewerProfile = itemView.findViewById(R.id.ivReviewerProfile);
            tvReviewerUser = itemView.findViewById(R.id.tvReviewerUser);
            tvReview = itemView.findViewById(R.id.tvReview);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            itemView.setOnClickListener(this);
        }

        public void bind(Review review) {
            tvReviewerUser.setText(review.getAuthor().getUsername());
            tvReview.setText(review.getBody());
            ratingBar.setRating(review.getRating());

            ParseFile image = review.getAuthor().getParseFile("profilePic");

            Glide.with(mContext).load(image.getUrl()).into(ivReviewerProfile);
        }

        @Override
        public void onClick(View v) {
            // Detailed review with timestamp will be added later
        }
    }

    public void clear() {
        reviews.clear();
        notifyDataSetChanged();
    }
}
