package com.project.reshoe_fbu.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.ActivityCreateReviewBinding;
import com.parse.ParseUser;
import com.project.reshoe_fbu.models.Review;
import com.project.reshoe_fbu.models.User;

public class CreateReviewActivity extends AppCompatActivity {

    public static final String TAG = "CreateReviewActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCreateReviewBinding binding = ActivityCreateReviewBinding
                .inflate(getLayoutInflater());

        Log.i(TAG, "Here");

        View view = binding.getRoot();
        setContentView(view);

        User seller = new User(getIntent().getParcelableExtra("seller"));

        Context mContext = this;

        binding.btnPostReview.setOnClickListener(v -> {
            Review review = new Review();
            review.setBody(binding.etReview.getText().toString());
            review.setAuthor(new User(ParseUser.getCurrentUser()));
            review.setRating(binding.userRatingBar.getRating());
            review.setReviewee(seller);

            review.saveInBackground(e -> {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(mContext, getString(R.string.error_saving),
                            Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful!");
                binding.etReview.setText("");
                binding.userRatingBar.setRating(0);
                finish();
            });

        });
        binding.btnCancelReview.setOnClickListener(v -> finish());
    }
}
