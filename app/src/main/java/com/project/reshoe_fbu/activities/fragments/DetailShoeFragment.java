package com.project.reshoe_fbu.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentDetailShoeBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.project.reshoe_fbu.adapters.PagerAdapter;
import com.project.reshoe_fbu.adapters.PostsAdapter;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.text.NumberFormat;
import java.util.Objects;

public class DetailShoeFragment extends Fragment {

    private static final String TAG = "DetailShoeFragment";
    private Post post;
    private PagerAdapter pagerAdapter;
    private int prevFragmentCode;

    private FragmentDetailShoeBinding binding;

    private ViewGroup container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the particular post that will be in detailed view
        post = getArguments().getParcelable("post");
        prevFragmentCode = getArguments().getInt("code");
        this.container = container;

        return inflater.inflate(R.layout.fragment_detail_shoe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.
            Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User user = new User(ParseUser.getCurrentUser());

        binding = FragmentDetailShoeBinding.bind(view);

        try {
            String username = post.getUser().getUsername();
            binding.tvDetailSellerUser.setText("@" + username);
        } catch (ParseException e) {
            Log.e(TAG, "Something has gone terribly wrong with Parse", e);
        }

        binding.tvDetailCondition.setText(post.getCondition() + "/10");
        binding.tvDetailedDescription.setText(post.getDescription());
        binding.tvDetailName.setText(post.getShoeName());

        checkLikes(post.getNumLikes());

        double size = post.getSize();

        // Check to see if its a whole size or .5 & ternary
        if (size == Math.floor(size)) {
            String str = String.valueOf((int) (size));
            str = checkSizing(str);
            binding.tvSize.setText(str);
        } else {
            String str = String.valueOf(size);
            str = checkSizing(str);
            binding.tvSize.setText(str);
        }

        double price = post.getPrice();
        String currencyString = NumberFormat.getCurrencyInstance().format(price);
        // Handle the weird exception of formatting whole dollar amounts with no decimal
        currencyString = currencyString.replaceAll("\\.00", "");
        binding.tvDetailedPrice.setText(currencyString);

        try {
            Glide.with(view).
                    load(Objects.requireNonNull(post.getUser().getProfilePicURL())).
                    circleCrop().
                    into(binding.ivSellerProfile);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }

        binding.btnBackDetailShoe.setOnClickListener(v -> {

            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

            Fragment prevFragment;

            if (prevFragmentCode == PostsAdapter.BUYER_CODE) {
                prevFragment = new TimelineBuyerFragment();
            } else if (prevFragmentCode == PostsAdapter.SELLER_CODE) {
                prevFragment = new TimelineSellerFragment();
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean("filter", false);
                prevFragment = new SearchPostFragment();
                prevFragment.setArguments(bundle);
            }

            ft.replace(R.id.flContainer, prevFragment).commit();

        });

        if (!post.getIsSold()) {
            binding.fabAddCart.setOnClickListener(v -> {
                User currentUser = new User(ParseUser.getCurrentUser());
                try {
                    currentUser.addToCart(post, getActivity());
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            });
        } else {
            binding.fabAddCart.setVisibility(View.INVISIBLE);
            binding.tvSold.setVisibility(View.VISIBLE);
        }

        binding.ivSellerProfile.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("seller", post.getUser().getUser());
            Fragment detailedSellerFragment = new DetailedSellerFragment();
            detailedSellerFragment.setArguments(bundle);
            getActivity().
                    getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.flContainer, detailedSellerFragment).
                    addToBackStack("back").
                    commit();
        });

        try {
            if (post.didLike(user)) {
                binding.btnLike.setBackgroundResource(R.drawable.heart_filled);
            } else {
                binding.btnLike.setBackgroundResource(R.drawable.heart_outline);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        binding.btnLike.setOnClickListener(v -> {
            try {
                // If the user has liked the post before then unlike it.
                if (post.didLike(user)) {
                    Log.i(TAG, "Unlike");
                    binding.btnLike.setBackgroundResource(R.drawable.heart_outline);
                    post.unlike();
                    user.removeLike(post.getObjectId());
                    int numLikes = post.getNumLikes();
                    checkLikes(numLikes);
                } else {
                    Log.i(TAG, "Like");
                    binding.btnLike.setBackgroundResource(R.drawable.heart_filled);
                    post.like();
                    user.addLike(post.getObjectId());
                    int numLikes = post.getNumLikes();
                    Log.i("TAG", "" + numLikes);
                    checkLikes(numLikes);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        try {
            pagerAdapter = new PagerAdapter(getActivity().getBaseContext(),
                    post.getImageUrls(), false, post, container);
        } catch (
                JSONException e) {
            e.printStackTrace();
        }

        binding.viewPager.requestDisallowInterceptTouchEvent(true);
        binding.viewPager.setAdapter(pagerAdapter);

    }

    private String checkSizing(String str) {
        if (post.getIsWomenSizing()) {
            return str.concat(getString(R.string.women));
        } else {
            return str.concat(getString(R.string.men));
        }
    }

    public void checkLikes(int numLikes) {
        if (numLikes == 0) {
            binding.tvLikes.setText("");
        } else {
            binding.tvLikes.setText("" + numLikes);
        }
    }
}
