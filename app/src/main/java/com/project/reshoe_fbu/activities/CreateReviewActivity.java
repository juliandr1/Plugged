package com.project.reshoe_fbu.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reshoe_fbu.databinding.ActivityCreateReviewBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.project.reshoe_fbu.models.Review;

public class CreateReviewActivity extends AppCompatActivity {

    public static final String TAG = "CreateReviewActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        ActivityCreateReviewBinding binding = ActivityCreateReviewBinding.inflate(getLayoutInflater());

        Context context = this;

        binding.btnPostReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Review review = new Review();
                review.setBody(binding.etReview.getText().toString());
                review.setAuthor(ParseUser.getCurrentUser());
                review.setRating((int) binding.userRatingBar.getRating());

                review.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving", e);
                            Toast.makeText(context, "Error while saving", Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG, "Post save was successful!");
                        binding.etReview.setText("");
                        binding.userRatingBar.setRating(0);
                        finish();
                    }
                });

            }
        });

        binding.btnCancelReview.setOnClickListener(v -> finish());
    }
}
