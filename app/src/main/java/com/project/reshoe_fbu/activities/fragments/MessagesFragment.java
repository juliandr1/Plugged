package com.project.reshoe_fbu.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentMessagesBinding;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.project.reshoe_fbu.adapters.MessagesAdapter;
import com.project.reshoe_fbu.models.Message;
import com.project.reshoe_fbu.models.Thread;
import com.project.reshoe_fbu.models.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessagesFragment extends Fragment {

    public static final String TAG = "MessagesFragment";

    private User otherUser, currentUser;
    private List<Message> messages;
    private MessagesAdapter adapter;
    private FragmentMessagesBinding binding;
    private boolean firstMessage, isAuthor;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        otherUser = new User(getArguments().getParcelable("otherUser"));
        currentUser = new User(ParseUser.getCurrentUser());
        // isAuthor checks if the current user is the "otherUser" who will now become an author.
        isAuthor = getArguments().getBoolean("isAuthor");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentMessagesBinding.bind(view);

        messages = new ArrayList<>();
        adapter = new MessagesAdapter(getActivity(), messages, currentUser);
        binding.rvChat.setAdapter(adapter);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        binding.rvChat.setLayoutManager(linearLayoutManager);

        try {
            firstMessage = isFirstMessage();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Is first message?: " + firstMessage);

        try {
            refreshMessages();
        } catch (ParseException | JSONException e) {
            e.printStackTrace();
        }

        String websocketUrl = getString(R.string.back4app_server_uri);

        ParseLiveQueryClient parseLiveQueryClient = null;
        try {
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(websocketUrl));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);
        parseQuery = parseQuery.whereEqualTo(Message.KEY_OTHER_ID, otherUser.getObjectID());

        SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        // Listen for CREATE events on the Message class
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, object) -> {
            Log.i(TAG, "ADDED!");
            messages.add(0, object);
            // RecyclerView updates need to be run on the UI thread
            getActivity().runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
                binding.rvChat.scrollToPosition(0);
            });
        });

        binding.ibSend.setOnClickListener(view1 -> sendMessage());
    }

    public void sendMessage() {
        // When send button is clicked, create message object on Parse
            Message newMessage = new Message();
            newMessage.setAuthor(currentUser);
            newMessage.setOtherUser(otherUser);
            newMessage.setBody(binding.etMessage.getText().toString());

            newMessage.saveInBackground(e -> {
                if (e == null) {
                    if (firstMessage) {
                        Log.i(TAG, "New Thread ");
                        Thread thread = new Thread();
                        thread.setUser(currentUser);
                        thread.setOtherUser(otherUser);
                        try {
                            thread.addMessage(newMessage);
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                        try {
                            setMessageData(thread, newMessage);
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                        currentUser.addThread(thread);
                        firstMessage = false;
                    } else {
                        try {
                            Thread thread;
                            if (isAuthor) {
                                thread = otherUser.getThreadWith(currentUser);
                            } else {
                                thread = currentUser.getThreadWith(otherUser);
                            }
                            thread.addMessage(newMessage);
                            setMessageData(thread, newMessage);
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                    }
                    messages.add(0, newMessage);
                    adapter.notifyDataSetChanged();

                } else {
                    Log.e(TAG, "Failed to save message", e);
                }
            });
            binding.etMessage.setText(null);
        // Temp
        try {
            refreshMessages();
        } catch (ParseException | JSONException parseException) {
            parseException.printStackTrace();
        }
    }

    public void refreshMessages() throws ParseException, JSONException {
        if (!firstMessage) {
            messages.clear();
            if (isAuthor) {
                messages.addAll(otherUser.getThreadWith(currentUser).getMessages());
            } else {
                messages.addAll(currentUser.getThreadWith(otherUser).getMessages());
            }
            Log.i(TAG, "# of messages: " + messages.size());
            adapter.notifyDataSetChanged();
        }
    }

    /*
        Check to see if this is the first message between the two users.
     */
    public boolean isFirstMessage() throws ParseException {

        Log.i(TAG, "Current User: " + currentUser.getUsername());
        Log.i(TAG, "Other User: " + otherUser.getUsername());

        List<Thread> threads = currentUser.getUserThreads();

        if (threads == null) {
            return true;
        }

        // Use ternary operator
        String id;

        if (isAuthor) {
            id = currentUser.getObjectID();
        } else {
            id = otherUser.getObjectID();
        }

        for (int i = 0; i < threads.size(); i++) {
            if (threads.get(i).getOtherUser().getObjectID().equals(id)) {
                return false;
            }
        }
        return true;
    }

    private void setMessageData(Thread thread, Message newMessage) throws ParseException {
        Log.i(TAG, "send");
        if (isAuthor) {
            thread.setLastMessageSentOtherUser(new Date());
            thread.setLastMessageUser(newMessage);
        } else {
            thread.setLastMessageSentUser(new Date());
            thread.setLastMessageOtherUser(newMessage);
        }
    }
}