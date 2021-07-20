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
import com.parse.ParseQuery;
import com.project.reshoe_fbu.adapters.PostSearchAdapter;
import com.project.reshoe_fbu.models.Post;

import java.util.ArrayList;
import java.util.List;

public class SearchPostActivity extends AppCompatActivity {

    public static final String TAG = "SearchPostActivity";

    private RecyclerView rvSearches;
    private Context context;
    private PostSearchAdapter adapter;
    private List<Post> searches;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_post);

        context = this;

        Log.i(TAG, "Search Activity");

        searches = new ArrayList<>();
        adapter = new PostSearchAdapter(context, searches);
        rvSearches = findViewById(R.id.rvSearches);
        rvSearches.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvSearches.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searches.clear();
                adapter.notifyDataSetChanged();
                // perform query here
                querySearch(s);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void querySearch(String str) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);;
        query.whereContains("shoeNameSearch", str.toLowerCase());
        query.setLimit(20);
        // Order the posts by date
        query.addDescendingOrder("createdAt");
        // Get the posts
        query.findInBackground((newPosts, e) -> {
            if (e != null) {
                Log.e(TAG,"Issue with getting posts", e);
                return;
            }
            searches.addAll(newPosts);
            adapter.notifyDataSetChanged();
        });
    }
}
