package com.project.reshoe_fbu.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.project.reshoe_fbu.activities.fragments.SearchPostFragment;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.PostSort;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PostSearchAdapter extends RecyclerView.Adapter<PostSearchAdapter.ViewHolder>  {

    public static String TAG = "PostSearchAdapter";

    public static int SEARCH_CODE = 771;

    private final Context mContext;

    private final List<PostSort> searchPosts;

    private final FragmentManager fragmentManager;

    private SearchPostFragment fragment;

    public PostSearchAdapter(Context context, List<PostSort> searchPosts, FragmentManager fm,
                             SearchPostFragment fragment) {
        this.mContext = context;
        this.searchPosts = searchPosts;
        this.fragmentManager = fm;
        this.fragment = fragment;
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
        private final FrameLayout sold;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPhotoPreview = itemView.findViewById(R.id.ivPhotoPreview);
            tvShoeTitle = itemView.findViewById(R.id.tvShoeTitle);
            tvSearchDescription = itemView.findViewById(R.id.tvSearchDescription);
            sold = itemView.findViewById(R.id.soldBackground);

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

            if (postSort.getPost().getIsSold()) {
                sold.setVisibility(View.VISIBLE);
            }
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
                bundle.putString("str", fragment.getSearchStr());

                ArrayList<Post> queryItems = new ArrayList<>();
                queryItems.addAll(fragment.getSortedItems());

                bundle.putParcelableArrayList("items", queryItems);

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

    public void clear() {
        searchPosts.clear();
        notifyDataSetChanged();
    }
}
