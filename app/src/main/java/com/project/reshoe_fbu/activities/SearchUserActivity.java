package com.project.reshoe_fbu.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reshoe_fbu.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.project.reshoe_fbu.adapters.PostSearchAdapter;
import com.project.reshoe_fbu.adapters.UserSearchAdapter;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.User;

import java.util.ArrayList;
import java.util.List;

// To be changed after messaging feature fully finished
public class SearchUserActivity extends AppCompatActivity {
/*
    public static final String TAG = "SearchUserActivity";

    private RecyclerView rvSearchUsers;
    private Context context;
    private UserSearchAdapter adapter;
    private List<ParseUser> searches;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_post);

        context = this;

        searches = new ArrayList<>();
        adapter = new UserSearchAdapter(context, searches);
        rvSearchUsers = findViewById(R.id.rvSearches);
        rvSearchUsers.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvSearchUsers.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { return false; }

            @Override
            public boolean onQueryTextChange(String s) {
                // perform query here
                queryUsers(s);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void queryUsers(String str) {
        ParseQuery<ParseUser> query = ParseQuery.getQuery("User");
        query.whereContains("user_search", str.toLowerCase());
        query.setLimit(20);
        // Order the posts by date
        query.addDescendingOrder("createdAt");
        // Get the posts
        query.findInBackground((users, e) -> {
            if (e != null) {
                Log.e(TAG,"Issue with getting posts", e);
                return;
            }
            searches.addAll(users);
            adapter.notifyDataSetChanged();
        });
    }

 */
}
