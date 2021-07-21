package com.project.reshoe_fbu.activities.fragments;

import android.content.Context;
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

import androidx.appcompat.widget.SearchView;

import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentSearchPostBinding;
import com.parse.ParseQuery;
import com.project.reshoe_fbu.adapters.PostSearchAdapter;
import com.project.reshoe_fbu.models.Post;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SearchPostFragment extends Fragment {

    public static final String TAG = "SearchPostActivity";

    private PostSearchAdapter adapter;
    private List<Post> searches;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        FragmentSearchPostBinding binding = FragmentSearchPostBinding.bind(view);

        Context context = getActivity();

        // Init
        searches = new ArrayList<>();
        adapter = new PostSearchAdapter(context, searches, getActivity().getSupportFragmentManager());
        RecyclerView rvSearches = binding.rvSearches;
        rvSearches.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvSearches.setLayoutManager(layoutManager);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Instantiate the search view
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search for shoes...");
        // When somebody clicks on the searchview, query posts with those shoe names
        // as the user changes the input text.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Here is where we are going to implement the filter logic
                querySearch(newText);

                return true;
            }
        });
        return true;
    }

    // Query for posts that contain the desired shoe.
    private void querySearch(String str) {
        if (!str.isEmpty()) {
            ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

            query.whereContains("shoeNameSearch", str.toLowerCase());
            query.setLimit(20);
            // Order the posts by date
            query.addDescendingOrder("createdAt");
            // Get the posts
            query.findInBackground((newPosts, e) -> {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                searches.clear();
                adapter.notifyDataSetChanged();
                searches.addAll(newPosts);
                adapter.notifyDataSetChanged();
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_post, container, false);
    }
}