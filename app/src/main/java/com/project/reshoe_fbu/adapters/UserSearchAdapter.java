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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reshoe_fbu.R;
import com.parse.ParseUser;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.ViewHolder> {

    public static String TAG = "UserSearchAdapter";

    private final Context mContext;
    private final List<ParseUser> searchUsers;

    public UserSearchAdapter(Context context, List<ParseUser> searchUsers) {
        this.mContext = context;
        this.searchUsers = searchUsers;
    }

    @Override
    public UserSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user_message_preview,
                parent, false);

        return new UserSearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserSearchAdapter.ViewHolder holder, int position) {
        ParseUser user = searchUsers.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return searchUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView ivSearchUser;
        private final TextView tvSearchUsername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivSearchUser = itemView.findViewById(R.id.ivSearchUser);
            tvSearchUsername = itemView.findViewById(R.id.tvSearchUsername);

            // Set an onClickListener for individual post
            itemView.setOnClickListener(this);
        }

        public void bind(ParseUser user) {
            tvSearchUsername.setText("@" + user.getUsername());

            Glide.with(mContext).
                    load(user.getParseFile("profilePic").
                            getUrl()).
                    into(ivSearchUser);
        }

        @Override
        public void onClick(View v) {
            // Go to messages screen
        }
    }
}
