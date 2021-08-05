package com.project.reshoe_fbu.activities.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentReviewBinding;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.project.reshoe_fbu.activities.CreateReviewActivity;
import com.project.reshoe_fbu.adapters.ReviewsAdapter;
import com.project.reshoe_fbu.models.Review;
import com.project.reshoe_fbu.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReviewFragment extends Fragment {

    public static String TAG = "ReviewFragment";

    private ReviewsAdapter adapter;
    private List<Review> reviews;
    private User seller;
    private FragmentReviewBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        seller = new User(getArguments().getParcelable("seller"));
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.
            Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentReviewBinding.bind(view);

        // Find the Recycler View
        RecyclerView rvReviews = binding.rvReviews;

        reviews = new ArrayList<>();
        adapter = new ReviewsAdapter(getActivity(), reviews, getFragmentManager());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvReviews.setLayoutManager(layoutManager);
        rvReviews.setAdapter(adapter);

        binding.btnBackReview.setOnClickListener(v -> getActivity().
                getSupportFragmentManager().
                popBackStack());

        try {
            queryReviews();
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {

        if (item.getItemId() == R.id.post_review) {
            Intent intent = new Intent(getActivity(), CreateReviewActivity.class);
            intent.putExtra("seller", seller.getUser());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void queryReviews() throws ParseException {
        reviews.clear();
        reviews.addAll(User.getReviews(seller));
        adapter.notifyDataSetChanged();

        if (reviews.size() == 0) {
            binding.tvNoReviews.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            queryReviews();
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
    }
}