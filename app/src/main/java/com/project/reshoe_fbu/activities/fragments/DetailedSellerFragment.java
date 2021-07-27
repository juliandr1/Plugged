package com.project.reshoe_fbu.activities.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentDetailedSellerBinding;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.project.reshoe_fbu.adapters.PostsAdapter;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.User;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailedSellerFragment extends Fragment {

    public static final String TAG = "DetailedSellerFragment";

    private User seller;
    private PostsAdapter adapter;
    private List<Post> posts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the particular seller that was selected
        seller = new User(getArguments().getParcelable("seller"));
        return inflater.inflate(R.layout.fragment_detailed_seller, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.
            Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        FragmentDetailedSellerBinding binding = FragmentDetailedSellerBinding.bind(view);

        User currentUser = new User(ParseUser.getCurrentUser());

        posts = new ArrayList<>();
        adapter = new PostsAdapter(getActivity(), posts, currentUser, getActivity().
                getSupportFragmentManager(), true, false);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        RecyclerView rvSellerPosts = binding.rvSellerPosts;
        rvSellerPosts.setLayoutManager(layoutManager);
        rvSellerPosts.setAdapter(adapter);

        try {
            binding.tvSellerUsername.setText("@" + seller.getUsername());
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }

        binding.tvSellerDescription.setText(seller.getDescription());

        try {
            Glide.with(view).
                    load(seller.getProfilePicURL()).
                    circleCrop().
                    into(binding.ivSellerProfilePic);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }

        try {
            if (currentUser.didLike(seller.getUser())) {
                binding.btnLikeSeller.setBackgroundResource(R.drawable.heart_filled);
            } else {
                binding.btnLikeSeller.setBackgroundResource(R.drawable.heart_outline);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // If the user click the like button then check if we must like or unlike
        // the user.
        binding.btnLikeSeller.setOnClickListener(v -> {
            try {
                // If the user has liked the post before then unlike it.
                if (currentUser.didLike(seller.getUser())) {
                    Log.i(TAG, "Unlike");
                    binding.btnLikeSeller.setBackgroundResource(R.drawable.heart_outline);
                    currentUser.unlike(seller.getUser());
                } else {
                    Log.i(TAG, "Like");
                    binding.btnLikeSeller.setBackgroundResource(R.drawable.heart_filled);
                    currentUser.like(seller.getUser());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        // If the user wants to message the user then go to the messaging fragment
        binding.btnMessage.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("otherUser", seller.getUser());
            bundle.putBoolean("isAuthor", false);
            Fragment messageFragment = new MessagesFragment();
            messageFragment.setArguments(bundle);
            getActivity().
                    getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.flContainer, messageFragment).
                    addToBackStack("back").
                    commit();
        });

        binding.btnBackDetailSeller.setOnClickListener(v -> getActivity().
                getSupportFragmentManager().
                popBackStack());

        binding.btnReview.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("seller", seller.getUser());

            Fragment reviewFragment = new ReviewFragment();
            reviewFragment.setArguments(bundle);
            getActivity().
                    getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.flContainer, reviewFragment).
                    addToBackStack("back").
                    commit();
        });

        DecimalFormat df = new DecimalFormat("#.##");
        double rating = 0;
        try {
            rating = User.getRating(seller);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        if (!(rating == 0.0)) {
            binding.tvRating.setText(df.format(rating));
        }

        queryPosts();
    }

    /*
        Get all the current seller's posts
     */
    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo("user", seller.getUser());
        query.setLimit(20);
        // Order the posts by date
        query.addDescendingOrder("createdAt");
        // Get the posts
        query.findInBackground((newPosts, e) -> {
            if (e != null) {
                Log.e(TAG,"Issue with getting posts", e);
                return;
            }

            posts.addAll(newPosts);
            adapter.notifyDataSetChanged();
        });
    }
}