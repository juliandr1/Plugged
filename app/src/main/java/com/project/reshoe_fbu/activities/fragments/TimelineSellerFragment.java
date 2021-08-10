package com.project.reshoe_fbu.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.text.DecimalFormat;
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
                getSupportFragmentManager(), false);

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

        binding.fabNewPost.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PostActivity.class);
            startActivity(intent);
        });

        DecimalFormat df = new DecimalFormat("#.##");
        double rating = 0;
        try {
            rating = User.getRating(user);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        if (rating > 0.0) {
            binding.tvRating.setText(df.format(rating));
        } else {
            binding.tvRating.setText("");
            binding.ivStar.setVisibility(View.INVISIBLE);
        }

        queryPosts();
    }

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
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull
            MenuInflater inflater) {
        inflater.inflate(R.menu.menu_review, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == R.id.reviews) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("seller", user.getUser());

            Fragment reviewFragment = new ReviewFragment();
            reviewFragment.setArguments(bundle);
            getActivity().
                    getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.flContainer, reviewFragment).
                    addToBackStack("back").
                    commit();
        }
        return super.onOptionsItemSelected(item);
    }
}