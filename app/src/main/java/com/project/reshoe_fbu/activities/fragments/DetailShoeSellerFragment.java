package com.project.reshoe_fbu.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentDetailShoeBinding;
import com.example.reshoe_fbu.databinding.FragmentDetailShoeSellerBinding;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.project.reshoe_fbu.adapters.PagerAdapter;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.text.NumberFormat;
import java.util.Objects;

public class DetailShoeSellerFragment extends Fragment{

    private static final String TAG = "DetailShoeSellerFragment";
    private Post post;
    private PagerAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the particular post that will be in detailed view
        post = getArguments().getParcelable("post");
        return inflater.inflate(R.layout.fragment_detail_shoe_seller, container,
                false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.
            Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentDetailShoeSellerBinding binding = FragmentDetailShoeSellerBinding.bind(view);

        try {
            String username = post.getUser().getUsername();
            binding.tvDetailSellerUser.setText("@" + username);
        } catch (ParseException e) {
            Log.e(TAG, "Something has gone terribly wrong with Parse", e);
        }

        binding.tvDetailCondition.setText(post.getCondition() + "/10");
        binding.tvDetailedDescription.setText(post.getDescription());
        binding.tvDetailName.setText(post.getShoeName());

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
        binding.tvPriceDetailed.setText(currencyString);

        try {
            Glide.with(view).
                    load(Objects.requireNonNull(post.getUser().getProfilePicURL())).
                    circleCrop().
                    into(binding.ibSellerProfile);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }

        binding.btnBackDetailShoe.setOnClickListener(v -> getActivity().
                getSupportFragmentManager().
                popBackStack());


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

        binding.ibSellerProfile.setOnClickListener(v -> {
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
            pagerAdapter = new PagerAdapter(getActivity().getBaseContext(),
                    post.getImageUrls(), false, post);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        binding.viewPager.setAdapter(pagerAdapter);
    }

    private String checkSizing(String str) {
        if (post.getIsWomenSizing()) {
            return str.concat(getString(R.string.women));
        } else {
            return str.concat(getString(R.string.men));
        }
    }
}
