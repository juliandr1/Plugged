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

import org.json.JSONException;

import java.util.List;

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ViewHolder> {

    public static String TAG = "MessagesAdapter";

    private final Context mContext;
    private final List<Thread> threads;
    private FragmentManager fragmentManager;
    // This variable will be passed to the messages fragment and will determine who is the true
    // other user, as in the thread object the "otherUser" could be the current user.
    private ParseUser otherUser;
    private boolean isAuthor;

    public ThreadAdapter(Context context, List<Thread> threads, FragmentManager fragmentManager) {
        this.mContext = context;
        this.threads = threads;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_thread, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Thread thread = threads.get(position);
        try {
            holder.bind(thread);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return threads.size();
    }

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

        public void bind(Thread thread) throws JSONException {
            // Used for checking if isAuthor and binding information on the thread adapter
            ParseUser otherUserTemp = thread.getOtherUser().getUser();

            try {
                String username;
                ParseFile profilePic;
                if (otherUserTemp.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                    username = thread.getUser().getUser().fetchIfNeeded().getUsername();
                    // Get Parse File is needed as fetchIfNeeded() is needed and is only
                    // resolved for parse users.
                    profilePic = thread.getUser().getUser().fetchIfNeeded().
                            getParseFile("profilePic");
                    otherUser = thread.getUser().getUser();
                    isAuthor = true;
                } else {
                    username = thread.getOtherUser().getUser().fetchIfNeeded().getUsername();
                    profilePic = thread.getOtherUser().getUser().fetchIfNeeded().
                            getParseFile("profilePic");
                    otherUser = thread.getOtherUser().getUser();
                    isAuthor = false;
                };
                tvOtherUsername.setText("@" + username);
                Glide.with(mContext).
                        load(profilePic.getUrl()).
                        circleCrop().
                        into(ivMessageProfilePic);
            } catch (ParseException e) {
                Log.e(TAG, "Something has gone terribly wrong with Parse", e);
            }

            String body;

            // Ternary usage here
            if (isAuthor) {
                body = thread.getLastMessageOtherUser();
            } else {
                body = thread.getLastMessageUser();
            }

            if (body == null) {
                tvMessagePreview.setText("");
            } else {
                tvMessagePreview.setText(body);
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
            fragmentManager.
                    beginTransaction().
                    replace(R.id.flContainer, messagesFragment).
                    addToBackStack("back").
                    commit();
        }
    }
}
