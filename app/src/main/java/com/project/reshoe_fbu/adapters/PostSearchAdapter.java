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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reshoe_fbu.R;
import com.project.reshoe_fbu.activities.fragments.DetailShoeFragment;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.PostSort;
import com.project.reshoe_fbu.models.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.util.List;

public class PostSearchAdapter extends RecyclerView.Adapter<PostSearchAdapter.ViewHolder>  {

    public static String TAG = "PostSearchAdapter";
    public static int SEARCH_CODE = 771;

    private final Context mContext;
    private final List<PostSort> searchPosts;
    private final FragmentManager fragmentManager;

    public PostSearchAdapter(Context context, List<PostSort> searchPosts, FragmentManager fm) {
        this.mContext = context;
        this.searchPosts = searchPosts;
        this.fragmentManager = fm;
    }

    @Override
    public PostSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_search_post, parent,
                false);

        return new PostSearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostSearchAdapter.ViewHolder holder,
                                 int position) {
        PostSort postSort = searchPosts.get(position);

        try {
            holder.bind(postSort);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return searchPosts.size();
    }

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

        public void bind(PostSort postSort) throws JSONException {
            tvShoeTitle.setText(postSort.getPost().getShoeName());
            tvSearchDescription.setText(postSort.getPost().getDescription());

            Glide.with(mContext).load(postSort.
                    getPost().
                    getImageUrls().
                    get(0)).
                    into(ivPhotoPreview);
        }

        @Override
        public void onClick(View v) {
            Log.e(TAG, "Clicked");
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                PostSort postSort = searchPosts.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("post", postSort.getPost());
                bundle.putInt("code", SEARCH_CODE);
                Fragment detailShoeFragment = new DetailShoeFragment();
                detailShoeFragment.setArguments(bundle);

                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                ft.replace(R.id.flContainer, detailShoeFragment, "post")
                        .addToBackStack(null)
                        .commit();
            }
        }
    }
}
