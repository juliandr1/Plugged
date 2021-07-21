package com.project.reshoe_fbu.activities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentDetailedSellerBinding;
import com.parse.ParseQuery;
import com.project.reshoe_fbu.adapters.PostsAdapter;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.User;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DetailedSellerFragment extends Fragment {

    public static final String TAG = "DetailedSellerFragment";

    private ParseUser seller;
    private PostsAdapter adapter;
    private List<Post> posts;
    private RecyclerView rvSellerPosts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the particular seller that was selected
        seller = getArguments().getParcelable("seller");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detailed_seller, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentDetailedSellerBinding binding = FragmentDetailedSellerBinding.bind(view);

        User currentUser = new User(ParseUser.getCurrentUser());

        posts = new ArrayList<>();
        adapter = new PostsAdapter(getActivity(), posts, currentUser, getActivity().getSupportFragmentManager());

        // Recycler view setup: layout manager and the adapter
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        rvSellerPosts = binding.rvSellerPosts;
        rvSellerPosts.setLayoutManager(layoutManager);
        rvSellerPosts.setAdapter(adapter);

        // Load user data
        binding.tvSellerUsername.setText("@" + seller.getUsername());
        binding.tvSellerDescription.setText(seller.getString("description"));

        Glide.with(view).load(seller.getParseFile("profilePic").getUrl()).circleCrop().into(binding.ivSellerProfilePic);

        // If the user click the like button then check if we must like or unlike
        // the user.
        binding.btnLikeSeller.setOnClickListener(v -> {
            try {
                // If the user has liked the post before then unlike it.
                if (currentUser.didLike(seller)) {
                    Log.i(TAG, "Unlike");
                    binding.btnLikeSeller.setBackgroundResource(R.drawable.heart_outline);
                    currentUser.unlike(seller);
                } else {
                    Log.i(TAG, "Like");
                    binding.btnLikeSeller.setBackgroundResource(R.drawable.heart_filled);
                    currentUser.like(seller);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        // If the user wants to message the user then go to the messaging fragment
        binding.btnMessage.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("otherUser", seller);
            bundle.putBoolean("isAuthor", false);
            Fragment messageFragment = new MessagesFragment();
            messageFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, messageFragment).addToBackStack("back").commit();
        });

        // Return back to the previous fragment (detailed shoe view)
        binding.btnBackDetail.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

        binding.btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment reviewFragment = new ReviewFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, reviewFragment).commit();
            }
        });

        queryPosts();
    }

    // Get all the posts in the database, including the ones for the current user.
    private void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo("user", seller);
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
}