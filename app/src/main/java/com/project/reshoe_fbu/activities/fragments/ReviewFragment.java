package com.project.reshoe_fbu.activities.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentMessagePreviewBinding;
import com.example.reshoe_fbu.databinding.FragmentReviewBinding;
import com.parse.ParseUser;
import com.project.reshoe_fbu.activities.CreateReviewActivity;
import com.project.reshoe_fbu.activities.PostActivity;
import com.project.reshoe_fbu.adapters.MessagePreviewAdapter;
import com.project.reshoe_fbu.adapters.ReviewsAdapter;
import com.project.reshoe_fbu.models.MessagePreview;
import com.project.reshoe_fbu.models.Review;
import com.project.reshoe_fbu.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReviewFragment extends Fragment {

    private ReviewsAdapter adapter;
    private List<Review> reviews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentReviewBinding binding = FragmentReviewBinding.bind(view);

        // Find the Recycler View
        RecyclerView rvReviews = binding.rvReviews;

        // Create a list of all the message previews and set up adapter
        reviews = new ArrayList<>();
        adapter = new ReviewsAdapter(getActivity(), reviews, getFragmentManager());
        // Recycler view setup: layout manager and the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvReviews.setLayoutManager(layoutManager);
        rvReviews.setAdapter(adapter);

        binding.btnBackReview.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_review, menu);
    }

    // Implement review creation view
    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {

        if (item.getItemId() == R.id.post_review) {
            Intent intent = new Intent(getActivity(), CreateReviewActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}