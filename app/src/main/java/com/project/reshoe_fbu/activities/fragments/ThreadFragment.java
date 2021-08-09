package com.project.reshoe_fbu.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentMessagePreviewBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.project.reshoe_fbu.adapters.ThreadAdapter;
import com.project.reshoe_fbu.models.Thread;
import com.project.reshoe_fbu.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ThreadFragment extends Fragment {

    public static final String TAG = "ThreadFragment";

    private ThreadAdapter adapter;

    private List<Thread> threads;

    private User currentUser;

    private SwipeRefreshLayout refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.
            Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentMessagePreviewBinding binding = FragmentMessagePreviewBinding.bind(view);

        currentUser = new User(ParseUser.getCurrentUser());

        RecyclerView rvThreads = binding.rvMessagePreviews;

        threads = new ArrayList<>();
        adapter = new ThreadAdapter(getActivity(), threads, getParentFragmentManager());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvThreads.setLayoutManager(layoutManager);
        rvThreads.setAdapter(adapter);

        try {
            queryThreads();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        refresh = getActivity().findViewById(R.id.swipeContainer);
        refresh.setOnRefreshListener(() -> {
            try {
                queryThreads();
            } catch (ParseException parseException) {
                parseException.printStackTrace();
            }
            refresh.setRefreshing(false);
        });
    }

    public void queryThreads() throws ParseException {
        threads.clear();
        threads.addAll(currentUser.getUserThreads());
        adapter.notifyDataSetChanged();
    }
}