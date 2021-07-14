package com.example.reshoe_fbu.activities.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.activities.PostActivity;
import com.example.reshoe_fbu.adapters.PostsAdapter;
import com.example.reshoe_fbu.databinding.FragmentTimelineSellerBinding;
import com.example.reshoe_fbu.models.Post;
import com.parse.Parse;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TimelineSellerFragment extends Fragment {

    private PostsAdapter adapter;
    private List<Post> posts;

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
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentTimelineSellerBinding binding = FragmentTimelineSellerBinding.bind(view);

        ParseUser currentUser = ParseUser.getCurrentUser();

        RecyclerView rvSellerPosts = binding.rvSellerPosts;

        // Setup the adapter and create a new list of posts
        posts = new ArrayList<>();
        adapter = new PostsAdapter(getActivity(), posts, currentUser);
        // Recycler view setup: layout manager and the adapter
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        rvSellerPosts.setLayoutManager(layoutManager);
        rvSellerPosts.setAdapter(adapter);

        // Load profile picture
        Glide.with(view).load(currentUser.getParseFile("profilePic").getUrl()).transform(new RoundedCorners(300)).into(binding.ivSellerProfilePic);
        binding.tvUserName.setText("@" + currentUser.getUsername());

        // Check to see if the user has added a description
        String description = currentUser.getString("description");
        if (description != null) {
            binding.tvSellerDescription.setText(description);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_seller, menu);
    }

    // If the seller clicks, then go to the post activity.
    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {

        if (item.getItemId() == R.id.post) {
            Intent intent = new Intent(getActivity(), PostActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}