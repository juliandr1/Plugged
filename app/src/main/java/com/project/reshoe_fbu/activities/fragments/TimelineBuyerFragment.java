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
import com.parse.ParseQuery;
import com.project.reshoe_fbu.activities.SearchActivity;
import com.project.reshoe_fbu.adapters.PostsAdapter;
import com.example.reshoe_fbu.databinding.FragmentTimelineBuyerBinding;
import com.project.reshoe_fbu.models.Post;
import com.parse.ParseUser;
import com.project.reshoe_fbu.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TimelineBuyerFragment extends Fragment {

    public static final String TAG = "TimelineBuyerFragment";

    private PostsAdapter adapter;
    private List<Post> posts;

    public TimelineBuyerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline_buyer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentTimelineBuyerBinding binding = FragmentTimelineBuyerBinding.bind(view);

        User currentUser = new User(ParseUser.getCurrentUser());
        // Find the Recycler View
        RecyclerView rvPosts = binding.rvPosts;
        // Init the list of posts and setup the adapter.
        posts = new ArrayList<>();
        adapter = new PostsAdapter(getActivity(), posts, currentUser, getActivity().getSupportFragmentManager());
        // Recycler view setup: layout manager and the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPosts.setLayoutManager(layoutManager);
        rvPosts.setAdapter(adapter);

        queryPosts();
    }
    // Get all the posts in the database, including the ones for the current user.
    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
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
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_buyer, menu);
    }

    // If the seller clicks, then go to the post activity.
    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {

        if (item.getItemId() == R.id.searchBuyer) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}