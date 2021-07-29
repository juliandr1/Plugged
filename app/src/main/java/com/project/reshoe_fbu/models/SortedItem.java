package com.project.reshoe_fbu.models;

public class SortedItem {

    private Post post;
    private int score;

    public SortedItem(Post post) {
        this.post = post;
        calculateScore();
    }

    public void calculateScore() {

    }
}
