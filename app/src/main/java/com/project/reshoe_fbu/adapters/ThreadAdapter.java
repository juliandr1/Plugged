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
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.reshoe_fbu.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.project.reshoe_fbu.activities.fragments.MessagesFragment;
import com.project.reshoe_fbu.models.Message;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.Thread;
import com.project.reshoe_fbu.models.User;

import java.util.List;

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ViewHolder> {

    public static String TAG = "MessagesAdapter";

    private final Context context;
    private final List<Thread> threads;
    private FragmentManager fragmentManager;
    private ParseUser otherUser;
    private boolean isAuthor;

    // Pass in the context, list of message previews, the user, and the fragment manager
    public ThreadAdapter(Context context, List<Thread> threads, FragmentManager fragmentManager) {
        this.context = context;
        this.threads = threads;
        this.fragmentManager = fragmentManager;
    }

    // For each row, inflate the layout
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_thread, parent, false);
        return new ViewHolder(view);
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at position
        Thread thread = threads.get(position);
        // Bind the tweet with view holder
        holder.bind(thread);
    }

    // Return amount of previews
    @Override
    public int getItemCount() {
        return threads.size();
    }

    // Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Not sure if binding is allowed in adapter. Potentially change
        private final ImageView ivMessageProfilePic;
        private final TextView tvMessagePreview, tvOtherUsername;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivMessageProfilePic = itemView.findViewById(R.id.ivMessageProfilePic);
            tvMessagePreview = itemView.findViewById(R.id.tvMessagePreview);
            tvOtherUsername = itemView.findViewById(R.id.tvOtherUsername);

            // Set an onClickListener for individual post
            itemView.setOnClickListener(this);
        }

        // Bind data
        public void bind(Thread thread) {
            otherUser = thread.getOtherUser();

            Message message = thread.getLastMessage();

            if (message == null) {
                tvMessagePreview.setText("");
            } else {
                tvMessagePreview.setText(message.getBody());
            }

            try {
                String username;
                ParseFile profilePic;
                if (otherUser.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                    username = thread.getUser().fetchIfNeeded().getUsername();
                    profilePic = thread.getUser().fetchIfNeeded().getParseFile("profilePic");
                    isAuthor = true;
                } else {
                    username = thread.getOtherUser().fetchIfNeeded().getUsername();
                    profilePic = thread.getOtherUser().fetchIfNeeded().getParseFile("profilePic");
                    isAuthor = false;
                };
                tvOtherUsername.setText("@" + username);
                Glide.with(context).load(profilePic.getUrl()).circleCrop().into(ivMessageProfilePic);
            } catch (ParseException e) {
                Log.e(TAG, "Something has gone terribly wrong with Parse", e);
            }
        }

        // If clicked then go to the messages fragment with the other user
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("otherUser", otherUser);
            bundle.putBoolean("isAuthor", isAuthor);
            if (isAuthor) {
                bundle.putParcelable("user", ParseUser.getCurrentUser());
            }
            Fragment messagesFragment = new MessagesFragment();
            messagesFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.flContainer, messagesFragment).addToBackStack("back").commit();
        }
    }
}
