package com.project.reshoe_fbu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reshoe_fbu.R;
import com.parse.ParseException;
import com.project.reshoe_fbu.models.Review;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    public static String TAG = "ReviewsAdapter";

    private final Context mContext;

    private final List<Review> reviews;


    public ReviewsAdapter(Context context, List<Review> reviews) {
        this.mContext = context;
        this.reviews = reviews;
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_review, parent,
                false);
        return new ReviewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ReviewsAdapter.ViewHolder holder, int position) {
        Review review = reviews.get(position);
        try {
            holder.bind(review);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivReviewerProfile;
        private final TextView tvReviewerUser, tvReview;
        private final RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivReviewerProfile = itemView.findViewById(R.id.ivReviewerProfile);
            tvReviewerUser = itemView.findViewById(R.id.tvReviewerUser);
            tvReview = itemView.findViewById(R.id.tvReview);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }

        public void bind(Review review) throws ParseException {
            tvReviewerUser.setText("@" + review.getAuthor().getUsername());
            tvReview.setText(review.getBody());
            ratingBar.setRating((float) review.getRating());

            Glide.with(mContext)
                    .load(review.getAuthor()
                    .getProfilePicURL())
                    .circleCrop()
                    .into(ivReviewerProfile);
        }
    }

    public void clear() {
        reviews.clear();
        notifyDataSetChanged();
    }
}
