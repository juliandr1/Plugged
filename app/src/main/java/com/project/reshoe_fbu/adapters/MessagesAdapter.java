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
import com.project.reshoe_fbu.models.User;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    public static String TAG = "MessagesAdapter";
    private static final int MESSAGE_OUTGOING = 123;
    private static final int MESSAGE_INCOMING = 321;

    private final Context mContext;
    private final List<Message> mMessages;
    private final User mUser;

    public MessagesAdapter(Context context, List<Message> messages, User user) {
        this.mContext = context;
        this.mMessages = messages;
        this.mUser = user;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        Log.i(TAG, "" + viewType);

        if (viewType == MESSAGE_INCOMING) {

            View contactView = inflater.inflate(R.layout.message_incoming, parent,
                    false);
            return new IncomingMessageViewHolder(contactView);
        } else if (viewType == MESSAGE_OUTGOING) {
            View contactView = inflater.inflate(R.layout.messages_outgoing, parent,
                    false);
            return new OutgoingMessageViewHolder(contactView);
        } else {
            throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        // Get the data at position
        Message message = mMessages.get(position);
        // Bind the tweet with view holder
        try {
            holder.bindMessage(message);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
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
        Message message = mMessages.get(position);
        return message.getAuthorId() != null && message.getAuthorId().equals(mUser.getObjectID());
    }

    public abstract class MessageViewHolder extends RecyclerView.ViewHolder {

        public MessageViewHolder(@NonNull View itemView) { super(itemView); }

        abstract void bindMessage(Message message) throws ParseException;
    }

    public class IncomingMessageViewHolder extends MessageViewHolder {
        ImageView imageOther;
        TextView tvBodyIncoming;
        TextView name;

        public IncomingMessageViewHolder(View itemView) {
            super(itemView);
            imageOther = itemView.findViewById(R.id.ivProfileOther);
            tvBodyIncoming = itemView.findViewById(R.id.tvBody);
            name = itemView.findViewById(R.id.tvName);
        }

        @Override
        public void bindMessage(Message message) throws ParseException {
            Glide.with(mContext)
                    .load(message.getAuthor().getProfilePicURL())
                    .circleCrop()
                    .into(imageOther);
            Log.i(TAG, message.getBody());
            tvBodyIncoming.setText(message.getBody());
            name.setText("@" + message.getAuthor().getUsername());
        }
    }

    public class OutgoingMessageViewHolder extends MessageViewHolder {
        ImageView imageMe;
        TextView tvBodyMe;

        public OutgoingMessageViewHolder(View itemView) {
            super(itemView);
            imageMe = itemView.findViewById(R.id.ivProfileMe);
            tvBodyMe = itemView.findViewById(R.id.tvBody);
        }

        @Override
        public void bindMessage(Message message) throws ParseException {
            Glide.with(mContext)
                    .load(message.getAuthor().getProfilePicURL())
                    .circleCrop()
                    .into(imageMe);
            tvBodyMe.setText(message.getBody());
        }

        public void clear() {
            mMessages.clear();
            notifyDataSetChanged();
        }
    }
}
