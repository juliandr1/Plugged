package com.project.reshoe_fbu.models;

import android.util.Log;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@ParseClassName("Thread")
public class Thread extends ParseObject implements Comparable<Thread> {

    private static final String TAG = "Thread";

    // This variable will be passed to the messages fragment and will determine who is the true
    // other user, as in the thread object the "otherUser" could be the current user.
    public ParseUser otherUser;

    public static final String KEY_LAST_MESSAGE_USER = "last_message_user";
    public static final String KEY_LAST_MESSAGE_OTHER_USER = "last_message_otherUser";
    public static final String KEY_LAST_MESSAGE_SENT_USER = "last_message_sent_user";
    public static final String KEY_LAST_MESSAGE_SENT_OTHER_USER = "last_message_sent_otherUser";
    public static final String KEY_USER = "user";
    public static final String KEY_OTHER_USER = "otherUser";
    public static final String KEY_MESSAGES = "messages";

    public Date getLastMessageSentUser() { return getDate(KEY_LAST_MESSAGE_SENT_USER); }

    public void setLastMessageSentUser(Date lastSent) throws ParseException {
        put(KEY_LAST_MESSAGE_SENT_USER, lastSent);
        save();
    }

    public Date getLastMessageSentOtherUser() { return getDate(KEY_LAST_MESSAGE_SENT_OTHER_USER); }

    public void setLastMessageSentOtherUser(Date lastRead) throws ParseException {
        put(KEY_LAST_MESSAGE_SENT_OTHER_USER, lastRead);
        save();
    }

    public String getLastMessageUser() { return getString(KEY_LAST_MESSAGE_USER); }

    public void setLastMessageUser(Message lastMessage) throws ParseException {
        put(KEY_LAST_MESSAGE_USER, lastMessage.getBody());
        save();
    }

    public String getLastMessageOtherUser() { return getString(KEY_LAST_MESSAGE_OTHER_USER); }

    public void setLastMessageOtherUser(Message lastMessage) throws ParseException {
        put(KEY_LAST_MESSAGE_OTHER_USER, lastMessage.getBody());
        save();
    }

    public User getOtherUser() { return new User(getParseUser(KEY_OTHER_USER)); }

    public void setOtherUser(User otherUser) { put(KEY_OTHER_USER, otherUser.getUser()); }

    public User getUser() { return new User(getParseUser(KEY_USER)); }

    public void setUser(User user) { put(KEY_USER, user.getUser()); }

    public List<Message> getMessages() throws JSONException, ParseException {
        JSONArray jsonArray = getJSONArray(KEY_MESSAGES);
        List<String> messageId = new ArrayList<>();
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);

        for (int i = 0; i < jsonArray.length(); i++) {
            messageId.add(jsonArray.get(i).toString());
        }

        query.addDescendingOrder("createdAt");
        query.whereContainedIn("objectId", messageId);

        return query.find();
    }

    public void addMessage(Message message) throws ParseException {
        add(KEY_MESSAGES, message.getObjectId());
        save();
    }

    @Override
    public int compareTo(Thread o) {
        if (this.getLastMessageSentOtherUser().after(o.getLastMessageSentOtherUser())) {
            return -1;
        } else if (this.getLastMessageSentOtherUser().before(o.getLastMessageSentOtherUser())) {
            return 1;
        } else {
            return 0;
        }
    }
    public static Comparator<Thread> comparator = (o1, o2) -> o1.compareTo(o2);
}
