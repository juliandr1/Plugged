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
import com.example.reshoe_fbu.databinding.FragmentDetailShoeSellerBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.project.reshoe_fbu.adapters.PagerAdapter;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.text.NumberFormat;
import java.util.Objects;

public class DetailShoeSellerFragment extends Fragment {

    private static final String TAG = "DetailShoeSellerFragment";

    private Post post;

    private PagerAdapter pagerAdapter;

    private ViewGroup container;

    private FragmentDetailShoeSellerBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the particular post that will be in detailed view
        post = getArguments().getParcelable("post");
        this.container = container;
        return inflater.inflate(R.layout.fragment_detail_shoe_seller, container,
                false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.
            Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User user = new User(ParseUser.getCurrentUser());

        binding = FragmentDetailShoeSellerBinding.bind(view);

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

            Fragment prevFragment = new TimelineSellerFragment();

            ft.replace(R.id.flContainer, prevFragment).commit();
        });

        if (post.getIsSold()) {
            binding.tvSold.setVisibility(View.VISIBLE);
        }

        binding.btnDelete.setOnClickListener(v -> {
            binding.btnDelete.setClickable(false);
            post.deleteInBackground(e -> {
                binding.btnDelete.setClickable(true);
                Log.i(TAG, "deleted post");
                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack();
            });
        });

        try {
            pagerAdapter = new PagerAdapter(getActivity().getBaseContext(),
                    post.getImageUrls(), false, post, container);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        binding.viewPager.setAdapter(pagerAdapter);

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
            binding.tvLikes.setText(String.valueOf(numLikes));
        }
    }
}
