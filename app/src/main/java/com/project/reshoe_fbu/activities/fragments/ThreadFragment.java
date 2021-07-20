package com.project.reshoe_fbu.activities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reshoe_fbu.R;
import com.parse.ParseQuery;
import com.project.reshoe_fbu.adapters.ThreadAdapter;
import com.example.reshoe_fbu.databinding.FragmentMessagePreviewBinding;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.Thread;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ThreadFragment extends Fragment {

    public static final String TAG = "ThreadFragment";

    private ThreadAdapter adapter;
    private List<Thread> threads;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentMessagePreviewBinding binding = FragmentMessagePreviewBinding.bind(view);

        // Find the Recycler View
        RecyclerView rvPosts = binding.rvMessagePreviews;

        // Create a list of all the message previews and set up adapter
        threads = new ArrayList<>();
        adapter = new ThreadAdapter(getActivity(), threads, getParentFragmentManager());
        // Recycler view setup: layout manager and the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPosts.setLayoutManager(layoutManager);
        rvPosts.setAdapter(adapter);
    }


    public void queryThreads() {
        ParseQuery<Thread> query = ParseQuery.getQuery(Thread.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        // Order the posts by date
        query.addDescendingOrder("last_message_received");
        // Get the posts
        query.findInBackground((allThreads, e) -> {
            if (e != null) {
                Log.e(TAG,"Issue with getting posts", e);
                return;
            }
            threads.addAll(allThreads);
            adapter.notifyDataSetChanged();
        });
    }

}