package com.project.reshoe_fbu.activities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reshoe_fbu.R;
import com.project.reshoe_fbu.adapters.PostsAdapter;
import com.example.reshoe_fbu.databinding.FragmentTimelineBuyerBinding;
import com.project.reshoe_fbu.models.Post;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TimelineBuyerFragment extends Fragment {

    private PostsAdapter adapter;
    private List<Post> posts;

    public TimelineBuyerFragment() {
        // Required empty public constructor
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

        ParseUser currentUser = ParseUser.getCurrentUser();
        // Find the Recycler View
        RecyclerView rvPosts = binding.rvPosts;
        // Init the list of posts and setup the adapter.
        posts = new ArrayList<>();
        adapter = new PostsAdapter(getActivity(), posts, currentUser);
        // Recycler view setup: layout manager and the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPosts.setLayoutManager(layoutManager);
        rvPosts.setAdapter(adapter);

    }
}