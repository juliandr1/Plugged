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
import com.project.reshoe_fbu.adapters.MessagePreviewAdapter;
import com.example.reshoe_fbu.databinding.FragmentMessagePreviewBinding;
import com.project.reshoe_fbu.models.MessagePreview;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MessagePreviewFragment extends Fragment {

    private MessagePreviewAdapter adapter;
    private List<MessagePreview> messagePreviews;

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

        ParseUser currentUser = ParseUser.getCurrentUser();
        // Find the Recycler View
        RecyclerView rvPosts = binding.rvMessagePreviews;

        // Create a list of all the message previews and set up adapter
        messagePreviews = new ArrayList<>();
        adapter = new MessagePreviewAdapter(getActivity(), messagePreviews, currentUser, getParentFragmentManager());
        // Recycler view setup: layout manager and the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPosts.setLayoutManager(layoutManager);
        rvPosts.setAdapter(adapter);
    }
}