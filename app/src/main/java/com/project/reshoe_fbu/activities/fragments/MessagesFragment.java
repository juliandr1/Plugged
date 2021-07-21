package com.project.reshoe_fbu.activities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.reshoe_fbu.R;
import com.project.reshoe_fbu.adapters.MessagesAdapter;
import com.example.reshoe_fbu.databinding.FragmentMessagesBinding;
import com.project.reshoe_fbu.models.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;
import com.project.reshoe_fbu.models.Thread;
import com.project.reshoe_fbu.models.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
        //
        isAuthor = getArguments().getBoolean("isAuthor");

        if (isAuthor) {
            otherUser = new User(getArguments().getParcelable("user"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentMessagesBinding.bind(view);

        messages = new ArrayList<>();
        adapter = new MessagesAdapter(getActivity(), messages, currentUser);
        binding.rvChat.setAdapter(adapter);

        // associate the LayoutManager with the RecylcerView
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
        } catch (ParseException e) {
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

        // Connect to Parse server
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

    // Set up button event handler which posts the entered message to Parse
    public void sendMessage() {
        // When send button is clicked, create message object on Parse
            Message newMessage = new Message();
            newMessage.setAuthor(currentUser);
            newMessage.setOtherUser(otherUser);
            newMessage.setBody(binding.etMessage.getText().toString());

            newMessage.saveInBackground(e -> {
                if (e == null) {
                    Toast.makeText(getActivity(), "Successfully created message on Parse",
                            Toast.LENGTH_SHORT).show();
                    if (firstMessage) {
                        Log.i(TAG, "New Thread ");
                        Thread thread = new Thread();
                        thread.setLastSentTimestamp(new Date());
                        thread.setUser(currentUser);
                        thread.setOtherUser(otherUser);
                        thread.addMessage(newMessage);
                        currentUser.addThread(thread);
                        firstMessage = false;
                    } else {
                        try {
                            Thread thread = currentUser.getThreadWith(otherUser);
                            thread.addMessage(newMessage);
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                    }

                } else {
                    Log.e(TAG, "Failed to save message", e);
                }
            });
            binding.etMessage.setText(null);
    }

    public void refreshMessages() throws ParseException {
        if (!firstMessage) {
            if (isAuthor) {
                messages.addAll(otherUser.getThreadWith(currentUser).getThreadMessages());
            } else {
                messages = currentUser.getThreadWith(otherUser).getThreadMessages();
            }
            Log.i(TAG, "# of messages: " + messages.size());
            adapter.notifyDataSetChanged();
        }
    }

    public boolean isFirstMessage() throws ParseException {

        Log.i(TAG, "Current User: " + currentUser.getUsername());
        Log.i(TAG, "Other User: " + otherUser.getUsername());

        List<Thread> threads = currentUser.getUserThreads();

        if (threads == null) {
            return true;
        }

        String otherID = otherUser.getObjectID();

        for (int i = 0; i < threads.size(); i++) {
            if (threads.get(i).getOtherUser().getObjectId().equals(otherID)) {
                return false;
            }
        }
        return true;
    }
}