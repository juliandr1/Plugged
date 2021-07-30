package com.project.reshoe_fbu.models;

import com.parse.ParseException;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.Comparator;
import java.util.List;

public class PostSort implements Comparable<PostSort> {

    public static int COEFFICIENT_LIKED = 10;
    public static int COEFFICIENT_BOUGHT_FROM_USER = 5;

    private Post post;
    private int score;

    public PostSort(Post post) throws JSONException, ParseException {
        this.post = post;
        calculateScore();
    }

    public void calculateScore() throws JSONException, ParseException {
        score = 0;
        User currentUser = new User(ParseUser.getCurrentUser());

        List<Post> likedPosts = currentUser.getLikedPosts();
        List<String> likedUsers = currentUser.getLikedSellers();
        List<String> usersBought = currentUser.getUsersBought();

        if (likedPosts.contains(post)) {
            score += 10;
        }

        if (likedUsers.contains(post.getUser().getObjectID())) {
            score += 3;
        }

        if (usersBought.contains(post.getUser().getObjectID())) {
            score += 5;
        }
    }

    public Post getPost() {
        return post;
    }

    @Override
    public int compareTo(PostSort o) {
        if (this.score > o.score) {
            return 1;
        } else if (this.score < o.score) {
            return -1;
        } else {
            return 0;
        }
    }

    public static Comparator<PostSort> comparator = new Comparator<PostSort>() {
        @Override
        public int compare(PostSort o1, PostSort o2) {
            return o1.compareTo(o2);
        }
    };
}
