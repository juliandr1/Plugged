package com.project.reshoe_fbu.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reshoe_fbu.R;
import com.project.reshoe_fbu.activities.fragments.DetailShoeFragment;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.util.List;

public class PostSearchAdapter extends RecyclerView.Adapter<PostSearchAdapter.ViewHolder>  {

    public static String TAG = "PostSearchAdapter";

    private final Context context;
    private final List<Post> searchPosts;
    private final FragmentManager fragmentManager;

    // Pass in the context, list of posts, and user
    public PostSearchAdapter(Context context, List<Post> searchPosts, FragmentManager fm) {
        this.context = context;
        this.searchPosts = searchPosts;
        this.fragmentManager = fm;
    }

    // For each row, inflate the layout
    @Override
    public PostSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_post, parent, false);

        return new PostSearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostSearchAdapter.ViewHolder holder, int position) {
        // Get the data at position
        Post post = searchPosts.get(position);
        // Bind the tweet with view holder
        try {
            holder.bind(post);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Return amount of posts
    @Override
    public int getItemCount() {
        return searchPosts.size();
    }

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView ivPhotoPreview;
        private final TextView tvShoeTitle, tvSearchDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPhotoPreview = itemView.findViewById(R.id.ivPhotoPreview);
            tvShoeTitle = itemView.findViewById(R.id.tvShoeTitle);
            tvSearchDescription = itemView.findViewById(R.id.tvSearchDescription);

            // Set an onClickListener for individual post
            itemView.setOnClickListener(this);
        }

        public void bind(Post post) throws JSONException {
            tvShoeTitle.setText(post.getShoeName());
            tvSearchDescription.setText(post.getDescription());

            Glide.with(context).load(post.getImageUrls().get(0)).into(ivPhotoPreview);
        }

        @Override
        public void onClick(View v) {
            Log.e(TAG, "Clicked");
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post post = searchPosts.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("post", post);
                Fragment detailShoeFragment = new DetailShoeFragment();
                detailShoeFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.flContainer, detailShoeFragment).addToBackStack("back").commit();
            }
        }
    }
}
