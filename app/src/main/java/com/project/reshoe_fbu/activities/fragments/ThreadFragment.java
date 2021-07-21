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
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.project.reshoe_fbu.adapters.ThreadAdapter;
import com.example.reshoe_fbu.databinding.FragmentMessagePreviewBinding;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.Thread;
import com.project.reshoe_fbu.models.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ThreadFragment extends Fragment {

    public static final String TAG = "ThreadFragment";

    private ThreadAdapter adapter;
    private List<Thread> threads;
    private User currentUser;

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

        currentUser = new User(ParseUser.getCurrentUser());

        // Find the Recycler View
        RecyclerView rvThreads = binding.rvMessagePreviews;

        // Create a list of all the message previews and set up adapter
        threads = new ArrayList<>();
        adapter = new ThreadAdapter(getActivity(), threads, getParentFragmentManager());
        // Recycler view setup: layout manager and the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvThreads.setLayoutManager(layoutManager);
        rvThreads.setAdapter(adapter);

        try {
            queryThreads();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void queryThreads() throws ParseException {

        if (currentUser.getIsSeller()) {
            ParseQuery<Thread> query = ParseQuery.getQuery(Thread.class);
            query.whereEqualTo(Thread.KEY_OTHER_USER, currentUser.getUser());
            List<Thread> newThreads = query.find();
            if (newThreads != null) {
                for (int i = 0; i < newThreads.size(); i++) {
                    currentUser.addThread(newThreads.get(i));
                }
            }
        }

        threads.addAll(currentUser.getUserThreads());
        Log.i(TAG, threads.size() + "");
        adapter.notifyDataSetChanged();
    }
}