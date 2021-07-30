package com.project.reshoe_fbu.models;

import com.parse.ParseException;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.Comparator;
import java.util.List;

public class PostSort implements Comparable<PostSort> {

    public static int COEFFICIENT_LIKED_POST = 10;
    public static int COEFFICIENT_BOUGHT_FROM_USER = 5;
    public static int COEFFICIENT_LIKED_USER = 3;

    public static List<Post> likedPosts;
    public static List<String> likedUsers, usersBought;

    private Post post;
    private int score;

    public PostSort(Post post) throws JSONException, ParseException {
        this.post = post;
        calculateScore();
    }

    public void calculateScore() throws JSONException, ParseException {
        score = 0;
        User currentUser = new User(ParseUser.getCurrentUser());

        List<String> likedPosts = currentUser.getLikedPostsIds();
        List<String> likedUsers = currentUser.getLikedSellers();
        List<String> usersBought = currentUser.getUsersBought();

        if (likedPosts.contains(post.getObjectId())) {
            score += COEFFICIENT_LIKED_POST;
        }

        if (usersBought.contains(post.getUser().getObjectID())) {
            score += COEFFICIENT_BOUGHT_FROM_USER;
        }

        if (likedUsers.contains(post.getUser().getObjectID())) {
            score += COEFFICIENT_LIKED_USER;
        }
    }

    public Post getPost() {
        return post;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(PostSort o) {
        if (this.score > o.score) {
            return -1;
        } else if (this.score < o.score) {
            return 1;
        } else {
            return 0;
        }
    }
}
