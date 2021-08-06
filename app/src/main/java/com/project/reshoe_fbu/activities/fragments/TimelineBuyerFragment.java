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
import com.project.reshoe_fbu.activities.CartActivity;
import com.project.reshoe_fbu.activities.TimelineActivity;
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

    public TimelineBuyerFragment() {}

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
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable
    @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        FragmentTimelineBuyerBinding binding = FragmentTimelineBuyerBinding.bind(view);

        User currentUser = new User(ParseUser.getCurrentUser());

        RecyclerView rvPosts = binding.rvPosts;

        posts = new ArrayList<>();
        adapter = new PostsAdapter(getActivity(), posts, currentUser, getActivity().
                getSupportFragmentManager(), false, true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPosts.setLayoutManager(layoutManager);
        rvPosts.setAdapter(adapter);

        binding.fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("filter", false);
                bundle.putBoolean("posts", false);
                Fragment searchPost = new SearchPostFragment();
                searchPost.setArguments(bundle);
                getActivity().
                        getSupportFragmentManager().
                        beginTransaction().
                        replace(R.id.flContainer, searchPost).
                        addToBackStack("back").
                        commit();
            }
        });

        queryPosts();
    }

    /*
        Get all the posts in the database.
     */
    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

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
        if (item.getItemId() == R.id.cart) {
            Intent intent = new Intent(getActivity(), CartActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.liked) {
            Fragment likedPost = new LikedPostsFragment();
            getActivity().
                    getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.flContainer, likedPost).
                    addToBackStack("back").
                    commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.clear();
        queryPosts();
    }
}