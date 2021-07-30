package com.project.reshoe_fbu.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.reshoe_fbu.databinding.ActivityCreateReviewBinding;
import com.example.reshoe_fbu.databinding.ActivitySignupBinding;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
public class User  {
    public static final String TAG = "User";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_USERNAME_SEARCH = "user_search";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_IS_SELLER = "isSeller";
    public static final String KEY_PROFILE_PIC = "profilePic";
    public static final String KEY_LIKED_SELLERS = "likedSellers";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_THREADS = "threads";
    public static final String KEY_CART = "cart";
    public static final String KEY_LIKED_POSTS = "likedPosts";
    public static final String KEY_USERS_BOUGHT_FROM = "users_bought_from";

    private ParseUser user;

    public User (ParseUser user) {
        this.user = user;
    }

    public String getUsername() throws ParseException {
        return user.fetchIfNeeded().getString(KEY_USERNAME);
    }

    public void setUsername(String username) {
        user.put(KEY_USERNAME, username);
        user.put(KEY_USERNAME_SEARCH, username.toLowerCase()); }

    public void setPassword(String password) { user.put(KEY_PASSWORD, password); }

    public String getEmail() { return user.getString(KEY_EMAIL); }

    public void setEmail(String email) {
        user.put(KEY_EMAIL, email);
    }

    public String getDescription() { return user.getString(KEY_DESCRIPTION); }

    public void setDescription(String description) {
        user.put(KEY_DESCRIPTION, description);
    }

    public String getFirstName() {
        return user.getString(KEY_FIRST_NAME);
    }

    public void setFirstName(String firstName) { user.put(KEY_FIRST_NAME, firstName); }

    public String getLastName() {
        return user.getString(KEY_LAST_NAME);
    }

    public void setLastName(String lastName) { user.put(KEY_LAST_NAME, lastName); }

    public boolean getIsSeller() { return user.getBoolean("isSeller"); }

    public void setIsSeller(Boolean isSeller) { user.put(KEY_IS_SELLER, isSeller); }

    public String getProfilePicURL() throws ParseException { return user.fetchIfNeeded().
            getParseFile(KEY_PROFILE_PIC).getUrl(); }

    public void setProfilePic(ParseFile file) { user.put(KEY_PROFILE_PIC, file); }

    public static double getRating(User seller) throws ParseException {
        List<Review> reviews = getReviews(seller);

        double rating = 0;

        for (int i = 0; i < reviews.size(); i++) {
            rating += reviews.get(i).getRating();
        }

        return rating / reviews.size();
    }

    public String getObjectID() { return user.getObjectId(); }

    public ParseUser getUser() { return user; }

    public JSONArray getLikes() {return user.getJSONArray(KEY_LIKED_SELLERS);}

    public List<Thread> getUserThreads() throws ParseException {

        ParseQuery<Thread> query = ParseQuery.getQuery(Thread.class);

        query.whereEqualTo(Thread.KEY_USER, user);

        List<Thread> userThreads = query.find();

        query = ParseQuery.getQuery(Thread.class);

        query.whereEqualTo(Thread.KEY_OTHER_USER, user);

        userThreads.addAll(query.find());

        /* fix thread sorting
        userThreads.sort(new Comparator<Thread>() {
            @Override
            public int compare(Thread o1, Thread o2) {
                Date d1 = o1.get
                if (o1.getLastMessageSentOtherUser())
            }
        });

         */

        return userThreads;
    }

    public Thread getThreadWith(User otherUser) throws ParseException {
        List<Thread> threads = getUserThreads();
        String objectId = otherUser.getObjectID();

        // Brute force. Will implement a hashtable in future to find threads faster.
        for (int i = 0; i < threads.size(); i++) {
            if (threads.get(i).getOtherUser().getObjectID().equals(objectId)) {
                return threads.get(i);
            }
        }

        return null;
    }

    public void addThread(Thread thread) {
        user.add(KEY_THREADS, thread);
        user.saveInBackground(e -> {
            if (e != null) {
                e.printStackTrace();
            }
            Log.i(TAG, "thread added");
        });
    }

    public void addToCart(Post post, Context context) throws JSONException, ParseException {
        List<String> cart = getCartIds();
        if (cart.contains(post.getObjectId())) {
            // Add string resource
            Toast.makeText(context, post.getShoeName() + " are already in the cart",
                    Toast.LENGTH_SHORT).show();
        } else {
            user.add(KEY_CART, post.getObjectId());
            // String resource needs to be added.
            Toast.makeText(context, post.getShoeName() + " added to cart",
                    Toast.LENGTH_SHORT).show();

            user.saveInBackground(e -> {
                if (e != null) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void removeFromCart(String postId) {
        user.removeAll(KEY_CART, Arrays.asList(postId));
        user.saveInBackground(e -> {
            if (e != null) {
                e.printStackTrace();
            }
            Log.i(TAG, "removed from cart");
        });
    }

    public void clearCart(List<Post> cartItems) throws JSONException {
        List<String> cartIds = getCartIds();
        user.removeAll(KEY_CART, cartIds);

        List<String> usersBought = getUsersBought();

        if (usersBought == null) {
            usersBought = new ArrayList<>();
        }

        for (int i = 0; i < cartItems.size(); i++) {
            String id = cartItems.get(i).getUser().getObjectID();
            if (!usersBought.contains(i)) {
                Log.i(TAG, "add bought user");
                // For local usage
                usersBought.add(id);
                // For database
                addBoughtUser(id);
            }
        }
    }

    public void setItemsSold(List<Post> itemsSold) {
        for (int i = 0; i < itemsSold.size(); i++) {
            itemsSold.get(i).setIsSold();
        }
    }

    public List<Post> getCart() throws JSONException, ParseException {
        List<String> postIds = getCartIds();

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        query.addDescendingOrder("createdAt");
        query.whereContainedIn("objectId", postIds);

        return query.find();
    }

    public List<String> getCartIds() throws JSONException{
        JSONArray jsonArray = user.getJSONArray(KEY_CART);
        List<String> postIds = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            postIds.add(jsonArray.get(i).toString());
        }

        return postIds;
    }

    public static List<Review> getReviews(User seller) throws ParseException {
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);

        query.addDescendingOrder("createdAt");
        query.whereEqualTo(Review.KEY_REVIEWEE, seller.getUser());

        return query.find();
    }

    public List<Post> getLikedPosts() throws JSONException, ParseException {
        JSONArray jsonArray = user.getJSONArray(KEY_LIKED_POSTS);

        List<String> postIds = new ArrayList<>();

        if (jsonArray == null) {
            return new ArrayList<Post>();
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            postIds.add(jsonArray.get(i).toString());
        }

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        query.addDescendingOrder("createdAt");
        query.whereContainedIn("objectId", postIds);;

        return query.find();
    }

    public List<String> getLikedSellers() throws JSONException {
        JSONArray jsonArray = user.getJSONArray(KEY_LIKED_SELLERS);

        List<String> userIds = new ArrayList<>();

        if (jsonArray == null) {
            return new ArrayList<String>();
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            userIds.add(jsonArray.get(i).toString());
        }

        return userIds;
    }

    public void addBoughtUser(String userId) {
        user.add(KEY_USERS_BOUGHT_FROM, userId);
        user.saveInBackground();
    }

    public List<String> getUsersBought() throws JSONException {
        JSONArray jsonArray = user.getJSONArray(KEY_USERS_BOUGHT_FROM);

        if (jsonArray == null) {
            return null;
        }

        List<String> usersBoughtId = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            usersBoughtId.add(jsonArray.get(i).toString());
        }

        return usersBoughtId;
    }

    public void addLike(String postId) {
        user.add(KEY_LIKED_POSTS, postId);
        user.saveInBackground();
    }

    public void removeLike(String postId) {
        user.removeAll(KEY_LIKED_POSTS, Collections.singletonList(postId));
        user.saveInBackground();
    }

    public void like(ParseUser otherUser) {
        user.add(KEY_LIKED_SELLERS, otherUser.getObjectId());
        user.saveInBackground();
    }

    public void unlike(ParseUser otherUser) {
        user.removeAll(KEY_LIKED_SELLERS, Collections.singletonList(otherUser.getObjectId()));
        user.saveInBackground();
    }

    public boolean didLike(ParseUser otherUser) throws JSONException {
        JSONArray jsonArray = getLikes();

        // Check if the likes list is null
        if (jsonArray == null) {
            Log.i(TAG, "Empty");
            return false;
        }

        // Get the list of userIds and check if the current user has liked the post.
        List<String> userIds = User.fromJsonArray(jsonArray);
        return userIds.contains(otherUser.getObjectId());
    }

    public static List<String> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<String> likedUsers = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            likedUsers.add(jsonArray.get(i).toString());
        }

        return likedUsers;
    }
}
