package com.project.reshoe_fbu.adapters;

import android.content.Context;
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
import com.parse.ParseException;
import com.project.reshoe_fbu.models.Message;
import com.parse.ParseUser;
import com.project.reshoe_fbu.models.User;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    public static String TAG = "MessagesAdapter";
    private static final int MESSAGE_OUTGOING = 123;
    private static final int MESSAGE_INCOMING = 321;

    private final Context context;
    private final List<Message> messages;
    private final User user;

    // Pass in the context, list of messages, and the user
    public MessagesAdapter(Context context, List<Message> messages, User user) {
        this.context = context;
        this.messages = messages;
        this.user = user;
    }

    // For each row, inflate the layout
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        Log.i(TAG, ""+viewType);

        if (viewType == MESSAGE_INCOMING) {
            View contactView = inflater.inflate(R.layout.message_incoming, parent, false);
            return new IncomingMessageViewHolder(contactView);
        } else if (viewType == MESSAGE_OUTGOING) {
            View contactView = inflater.inflate(R.layout.messages_outgoing, parent, false);
            return new OutgoingMessageViewHolder(contactView);
        } else {
            throw new IllegalArgumentException("Unknown view type");
        }
    }

    // Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        // Get the data at position
        Message message = messages.get(position);
        Log.i(TAG, message.getBody() + " onBind");
        // Bind the tweet with view holder
        try {
            holder.bindMessage(message);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
    }

    // Return amount of posts
    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isMe(position)) {
            return MESSAGE_OUTGOING;
        } else {
            return MESSAGE_INCOMING;
        }
    }

    private boolean isMe(int position) {
        Message message = messages.get(position);
        return message.getAuthorId() != null && message.getAuthorId().equals(user.getObjectID());
    }

    public abstract class MessageViewHolder extends RecyclerView.ViewHolder {

        public MessageViewHolder(@NonNull View itemView) { super(itemView); }

        abstract void bindMessage(Message message) throws ParseException;
    }

    public class IncomingMessageViewHolder extends MessageViewHolder {
        ImageView imageOther;
        TextView body;
        TextView name;

        public IncomingMessageViewHolder(View itemView) {
            super(itemView);
            imageOther = (ImageView)itemView.findViewById(R.id.ivProfileOther);
            body = (TextView)itemView.findViewById(R.id.tvBody);
            name = (TextView)itemView.findViewById(R.id.tvName);
        }

        @Override
        public void bindMessage(Message message) throws ParseException {
            Glide.with(context)
                    .load(message.getAuthor().getProfilePicURL())
                    .circleCrop() // create an effect of a round profile picture
                    .into(imageOther);
            Log.i(TAG, message.getBody());
            body.setText(message.getBody());
            name.setText(message.getAuthor().getUsername());
        }
    }

    public class OutgoingMessageViewHolder extends MessageViewHolder {
        ImageView imageMe;
        TextView body;

        public OutgoingMessageViewHolder(View itemView) {
            super(itemView);
            imageMe = (ImageView) itemView.findViewById(R.id.ivProfileMe);
            body = (TextView) itemView.findViewById(R.id.tvBody);
        }

        @Override
        public void bindMessage(Message message) throws ParseException {
            Glide.with(context)
                    .load(message.getAuthor().getProfilePicURL())
                    .circleCrop() // create an effect of a round profile picture
                    .into(imageMe);
            body.setText(message.getBody());
        }

        // Clean all elements of the recycler
        public void clear() {
            messages.clear();
            notifyDataSetChanged();
        }
    }
}
