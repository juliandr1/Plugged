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
    private List<String> likedPosts, likedUsers, usersBought;

    private Post post;
    private int score;

    public PostSort(Post post, List<String> likedPosts, List<String> likedUsers,
                    List<String> usersBought) throws JSONException, ParseException {
        this.post = post;
        this.likedPosts = likedPosts;
        this.likedUsers = likedUsers;
        this.usersBought = usersBought;
        calculateScore();
    }

    public void calculateScore() {
        score = 0;

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
