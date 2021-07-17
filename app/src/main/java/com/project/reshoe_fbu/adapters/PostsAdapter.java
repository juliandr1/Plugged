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
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.project.reshoe_fbu.models.User;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public static String TAG = "PostsAdapter";

    private final Context context;
    private final List<Post> posts;
    private final User user;
    private final FragmentManager fragmentManager;

    // Pass in the context, list of posts, and user
    public PostsAdapter(Context context, List<Post> posts, User user, FragmentManager fragmentManager) {
        this.context = context;
        this.posts = posts;
        this.user = user;
        this.fragmentManager = fragmentManager;
    }

    // For each row, inflate the layout
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (user.getIsSeller()) {
            view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_post_buyer, parent, false);
        }

        return new ViewHolder(view);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at position
        Post post = posts.get(position);
        // Bind the tweet with view holder
        try {
            holder.bind(post);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // Return amount of posts
    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView ivImage;
        private final TextView tvDescription, tvCondition, tvPrice, tvLikes;
        private final Button btnLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvShoeName);
            tvCondition = itemView.findViewById(R.id.tvCondition);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            btnLike = itemView.findViewById(R.id.btnLike);

            // Set an onClickListener for individual post
            itemView.setOnClickListener(this);

            if (!user.getIsSeller()) {
                // Like or unlike posts and update the color of the heart.
                btnLike.setOnClickListener(v -> {
                    try {
                        Post post = posts.get(getAdapterPosition());
                        // If the user has liked the post before then unlike it.
                        if (post.didLike(user)) {
                            Log.i(TAG, "Unlike");
                            btnLike.setBackgroundResource(R.drawable.heart_outline);
                            post.unlike();
                            int numLikes = post.getNumLikes();
                            checkLikes(numLikes);
                        } else {
                            Log.i(TAG, "Like");
                            btnLike.setBackgroundResource(R.drawable.heart_filled);
                            post.like();
                            int numLikes = post.getNumLikes();
                            Log.i("TAG", "" + numLikes);
                            checkLikes(numLikes);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                btnLike.setVisibility(View.INVISIBLE);
            }
        }

        public void bind(Post post) throws JSONException, MalformedURLException, URISyntaxException {
            tvDescription.setText(post.getShoeName());
            tvCondition.setText(post.getCondition() + "/10");
            double price = post.getPrice();
            String currencyString = NumberFormat.getCurrencyInstance().format(price);
            // Handle the weird exception of formatting whole dollar amounts with no decimal
            currencyString = currencyString.replaceAll("\\.00", "");
            tvPrice.setText(currencyString);

            Glide.with(context).load(post.getImageUrls().get(0)).into(ivImage);

            if (!user.getIsSeller()) {
                try {
                    if (post.didLike(user)) {
                        btnLike.setBackgroundResource(R.drawable.heart_filled);
                    } else {
                        btnLike.setBackgroundResource(R.drawable.heart_outline);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            int numLikes = post.getNumLikes();
            checkLikes(numLikes);
        }

        @Override
        public void onClick(View v) {
            Log.e(TAG, "Clicked");
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("post", post);
                Fragment detailShoeFragment = new DetailShoeFragment();
                detailShoeFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.flContainer, detailShoeFragment).commit();
            }
        }

        // Check how the number of likes affects the text describing likes
        public void checkLikes(int numLikes) {
            if (numLikes == 0) {
                tvLikes.setText("");
            } else {
                tvLikes.setText("" + numLikes);
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }
}
