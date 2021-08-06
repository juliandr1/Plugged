package com.project.reshoe_fbu.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentTimelineSellerBinding;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.project.reshoe_fbu.activities.PostActivity;
import com.project.reshoe_fbu.adapters.PostsAdapter;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TimelineSellerFragment extends Fragment {

    public static final String TAG = "TimelineSellerFragment";

    private PostsAdapter adapter;
    private List<Post> posts;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline_seller, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.
            Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        FragmentTimelineSellerBinding binding = FragmentTimelineSellerBinding.bind(view);

        user = new User(ParseUser.getCurrentUser());

        RecyclerView rvSellerPosts = binding.rvSellerPosts;

        posts = new ArrayList<>();
        adapter = new PostsAdapter(getActivity().getBaseContext(), posts, user, getActivity().
                getSupportFragmentManager(), false, false);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        rvSellerPosts.setLayoutManager(layoutManager);
        rvSellerPosts.setAdapter(adapter);

        try {
            Glide.with(view).
                    load(user.getProfilePicURL()).
                    circleCrop().
                    into(binding.ivSellerProfilePic);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        try {
            binding.tvUserName.setText("@" + user.getUsername());
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }

        String description = user.getDescription();
        if (description != null) {
            binding.tvSellerDescription.setText(description);
        }

        binding.fabNewPost.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PostActivity.class);
            startActivity(intent);
        });

        queryPosts();
    }

      /*
         Get all the current seller's posts
      */
    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo("user", user.getUser());
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

    @Override
    public void onResume() {
        super.onResume();
        queryPosts();
    }
}