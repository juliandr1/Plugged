package com.project.reshoe_fbu.activities.fragments;

import android.content.Intent;
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
import com.parse.ParseException;
import com.project.reshoe_fbu.activities.CheckoutActivity;
import com.project.reshoe_fbu.adapters.PagerAdapter;
import com.project.reshoe_fbu.models.Post;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.text.NumberFormat;
import java.util.Objects;

public class DetailShoeFragment extends Fragment {

    private static final String TAG = "DetailShoeFragment";
    private Post post;
    private PagerAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the particular post that will be in detailed view
        post = getArguments().getParcelable("post");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_shoe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentDetailShoeBinding binding = FragmentDetailShoeBinding.bind(view);

        // Load post data

        try {
            String username = post.getParseUser(Post.KEY_USER).fetchIfNeeded().getUsername();
            binding.tvDetailSellerUser.setText("@" + username);
        } catch (ParseException e) {
            Log.e(TAG, "Something has gone terribly wrong with Parse", e);
        }

        binding.tvDetailCondition.setText(post.getCondition() + "/10");
        binding.tvDetailedDescription.setText(post.getDescription());
        binding.tvDetailName.setText(post.getShoeName());

        double price = post.getPrice();
        String currencyString = NumberFormat.getCurrencyInstance().format(price);
        // Handle the weird exception of formatting whole dollar amounts with no decimal
        currencyString = currencyString.replaceAll("\\.00", "");
        binding.tvPriceDetailed.setText(currencyString);

        Glide.with(view).load(Objects.requireNonNull(post.getUser().getParseFile("profilePic")).getUrl()).circleCrop().into(binding.ibSellerProfile);

        // Return back to the previous fragment (timeline)
        binding.btnBackDetail.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

        // Go to checkout (activity or fragment?)
        binding.btnCheckout.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), CheckoutActivity.class);
            startActivity(i);
            getActivity().overridePendingTransition(0, 0);
        });

        // Go to the detailed seller profile
        binding.ibSellerProfile.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("seller", post.getUser());
            Fragment detailedSellerFragment = new DetailedSellerFragment();
            detailedSellerFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, detailedSellerFragment).commit();
        });

        try {
            pagerAdapter = new PagerAdapter(getActivity(), post.getImageUrls(), false);
        } catch (JSONException e) {
                e.printStackTrace();
        }

        binding.viewPager.setAdapter(pagerAdapter);
    }
}
