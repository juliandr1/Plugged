package com.project.reshoe_fbu.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentLikedPostsBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.project.reshoe_fbu.adapters.PostsAdapter;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.User;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class LikedPostsFragment extends Fragment {

    public static final String TAG = "LikedPostsFragment";

    private List<Post> likedPosts;
    private User currentUser;
    private FragmentLikedPostsBinding binding;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_liked_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable
    @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        binding = FragmentLikedPostsBinding.bind(view);

        currentUser = new User(ParseUser.getCurrentUser());

        RecyclerView rvPosts = binding.rvLikedPosts;

        likedPosts = new ArrayList<>();
        PostsAdapter adapter = new PostsAdapter(getActivity(), likedPosts, currentUser,
                getActivity().getSupportFragmentManager(), true, false);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        rvPosts.setLayoutManager(layoutManager);
        rvPosts.setAdapter(adapter);

        binding.btnBackLikedPosts.setOnClickListener(v -> getActivity().
                getSupportFragmentManager().
                popBackStack());

        try {
            queryLikedPosts();
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

    }

    private void queryLikedPosts() throws JSONException, ParseException {
        likedPosts.addAll(currentUser.getLikedPosts());

        if (likedPosts.size() == 0) {
            binding.tvNoLikes.setVisibility(View.VISIBLE);
        }
    }
}
