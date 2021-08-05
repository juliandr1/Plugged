package com.project.reshoe_fbu.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.reshoe_fbu.R;
import com.project.reshoe_fbu.activities.fragments.DetailShoeFragment;
import com.project.reshoe_fbu.activities.fragments.DetailShoeSellerFragment;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.User;

import org.json.JSONException;

import java.text.NumberFormat;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    public static String TAG = "PostsAdapter";

    public static final int BUYER_CODE = 991;
    public static final int SELLER_CODE = 881;

    private final Context mContext;
    private final List<Post> posts;
    private final User user;
    private final FragmentManager fragmentManager;
    private final boolean isDetailedSeller;

    public PostsAdapter(Context context, List<Post> posts, User user, FragmentManager
            fragmentManager, boolean isDetailedSeller, boolean showLike) {
        this.mContext = context;
        this.posts = posts;
        this.user = user;
        this.fragmentManager = fragmentManager;
        this.isDetailedSeller = isDetailedSeller;
    }

    // For each row, inflate the layout
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (user.getIsSeller() || isDetailedSeller) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_post, parent,
                    false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_post_buyer, parent,
                    false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);

        try {
            holder.bind(post);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView ivImage;
        private final TextView tvDescription, tvCondition, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvShoeName);
            tvCondition = itemView.findViewById(R.id.tvCondition);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            // Set an onClickListener for individual post
            itemView.setOnClickListener(this);
        }

        public void bind(Post post) throws JSONException {
            tvDescription.setText(post.getShoeName());
            tvCondition.setText(post.getCondition() + "/10");
            double price = post.getPrice();
            String currencyString = NumberFormat.getCurrencyInstance().format(price);
            // Handle the weird exception of formatting whole dollar amounts with no decimal
            currencyString = currencyString.replaceAll("\\.00", "");
            tvPrice.setText(currencyString);

            Glide.with(mContext).
                    load(post.getImageUrls().get(0)).
                    transform(new CenterCrop(), new RoundedCorners(10)).
                    into(ivImage);
        }

        @Override
        public void onClick(View v) {
            Log.e(TAG, "Clicked");
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("post", post);

                Fragment detailShoeFragment;

                if (user.getIsSeller()) {
                    detailShoeFragment = new DetailShoeSellerFragment();
                    bundle.putInt("code", SELLER_CODE);
                } else {
                    detailShoeFragment = new DetailShoeFragment();
                    bundle.putInt("code", BUYER_CODE);
                }
                detailShoeFragment.setArguments(bundle);

                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                ft.replace(R.id.flContainer, detailShoeFragment, "post")
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }
}
