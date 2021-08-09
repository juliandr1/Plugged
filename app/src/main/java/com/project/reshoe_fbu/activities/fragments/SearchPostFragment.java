package com.project.reshoe_fbu.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentSearchPostBinding;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.project.reshoe_fbu.adapters.PostSearchAdapter;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.PostSort;
import com.project.reshoe_fbu.models.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchPostFragment extends Fragment {

    public static final String TAG = "SearchPostActivity";

    private PostSearchAdapter adapter;

    private List<PostSort> searches;

    private List<Post> queryItems;

    private ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

    private String searchStr;

    private FragmentSearchPostBinding binding;

    private int isHighToLowCode;

    private List<String> likedPosts, likedUsers, usersBought;

    private boolean sortPrice;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        User currentUser = new User(ParseUser.getCurrentUser());

        try {
            likedPosts = currentUser.getLikedPostsIds();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            likedUsers = currentUser.getLikedSellers();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            usersBought = currentUser.getUsersBought();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        boolean filterApplied = getArguments().getBoolean("filter");
        boolean savePosts = getArguments().getBoolean("posts");

        if (filterApplied) {
            searchStr = getArguments().getString("str");
            double size = getArguments().getDouble("size");
            if (((int) size) != FilterFragment.NOT_CHANGED_CODE) {
                query.whereEqualTo(Post.KEY_SIZE, size);
            }

            int condition = getArguments().getInt("condition");
            if (condition != FilterFragment.NOT_CHANGED_CODE) {
                query.whereEqualTo(Post.KEY_CONDITION, condition);
            }

            int isWomenSizingCode = getArguments().getInt("isWomenSizing");

            if (isWomenSizingCode == FilterFragment.CHANGED_IS_WOMEN_SIZING_M) {
                query.whereEqualTo(Post.KEY_IS_WOMEN_SIZING, false);
            } else if (isWomenSizingCode == FilterFragment.CHANGED_IS_WOMEN_SIZING_W) {
                query.whereEqualTo(Post.KEY_IS_WOMEN_SIZING, true);
            }

            isHighToLowCode = getArguments().getInt("isHighToLow");
            if (isHighToLowCode == FilterFragment.CHANGED_IS_HIGH_TO_LOW) {
                query.orderByDescending(Post.KEY_PRICE);
                sortPrice = true;
            } else if (isHighToLowCode == FilterFragment.CHANGED_IS_LOW_TO_HIGH) {
                query.orderByAscending(Post.KEY_PRICE);
                sortPrice = true;
            } else {
                sortPrice = false;
            }

            if (searchStr != null) {
                Log.i(TAG, searchStr);
                querySearch(searchStr);
            } else {
                querySearch(" ");
            }

        } else if (savePosts) {
            ArrayList<Post> items = getArguments().getParcelableArrayList("items");
            queryItems = new ArrayList<>();
            searches = new ArrayList<>();
            queryItems.addAll(items);
            adapter = new PostSearchAdapter(getActivity(), searches,
                    getActivity().getSupportFragmentManager(), this);

            try {
                convertPosts();
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.
            Nullable Bundle savedInstanceState) {
        binding = FragmentSearchPostBinding.bind(view);

        Context context = getActivity();

        if (searches == null) {
            searches = new ArrayList<>();
        }
        if (queryItems == null) {
            queryItems = new ArrayList<>();
        }

        if (adapter == null) {
            adapter = new PostSearchAdapter(context, searches,
                    getActivity().getSupportFragmentManager(), this);
        }
        RecyclerView rvSearches = binding.rvSearches;
        rvSearches.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvSearches.setLayoutManager(layoutManager);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull
            MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setQueryHint("Search for shoes...");
            // When somebody clicks on the searchview, query posts with those shoe names
            // as the user changes the input text.
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    querySearch(newText);
                    try {
                        sortItems();
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
        } else if (item.getItemId() == R.id.filter) {
            Fragment filterFragment = new FilterFragment();
            Bundle bundle = new Bundle();
            bundle.putString("str", searchStr);
            ArrayList<Post> items = new ArrayList<>();
            items.addAll(getSortedItems());
            bundle.putParcelableArrayList("items", items);
            filterFragment.setArguments(bundle);
            getActivity().
                    getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.flContainer, filterFragment).
                    commit();
        }
        return true;
    }

    /*
        Query for posts that contain the desired shoe.
     */
    private void querySearch(String str) {
        if (str.length() != 0) {
            searchStr = str;
            query.whereContains("shoeNameSearch", str.toLowerCase());
            query.setLimit(20);
            // Get the posts
            query.findInBackground((newPosts, e) -> {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                queryItems.clear();
                queryItems.addAll(newPosts);

                try {
                    sortItems();
                } catch (JSONException | ParseException jsonException) {
                    jsonException.printStackTrace();
                }

                if (queryItems.size() == 0) {
                    adapter.clear();
                    binding.tvNoSearchResults.setVisibility(View.VISIBLE);
                } else {
                    binding.tvNoSearchResults.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    private void sortItems() throws JSONException, ParseException {
        if (queryItems.size() != 0) {
            searches.clear();
            if (sortPrice) {
                Collections.sort(queryItems);
                if (isHighToLowCode == FilterFragment.CHANGED_IS_LOW_TO_HIGH) {
                    Collections.reverse(queryItems);
                }
                int price = 0, start = 0;
                boolean started = false;

                for (int i = 0; i < queryItems.size(); i++) {
                    Post post = queryItems.get(i);
                    searches.add(new PostSort(post, likedPosts, likedUsers, usersBought));
                    adapter.notifyDataSetChanged();

                    if (i == 0) {
                        price = post.getPrice();
                    } else {
                        if (price == post.getPrice() && i != queryItems.size() - 1) {
                            start = i - 1;
                            started = true;
                        } else if (started) {
                            if (queryItems.size() - 1 == i) {
                                Collections.sort(searches.subList(start, i + 1));
                            } else {
                                Collections.sort(searches.subList(start, i));
                            }
                            started = false;
                        } else {
                            price = post.getPrice();
                        }
                    }
                }
            } else {
                convertPosts();

                Collections.sort(searches);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_post, container, false);
    }

    public String getSearchStr() {
        return searchStr;
    }

    public List<Post> getSortedItems() {
        List<Post> items = new ArrayList<>();
        for (int i = 0; i < searches.size(); i++) {
            items.add(searches.get(i).getPost());
        }
        return items;
    }

    private void convertPosts() throws JSONException, ParseException {
        for (int i = 0; i < queryItems.size(); i++) {
            searches.add(new PostSort(queryItems.get(i), likedPosts, likedUsers,
                    usersBought));
            adapter.notifyDataSetChanged();
        }
    }
}