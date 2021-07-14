package com.example.reshoe_fbu.activities.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.activities.CheckoutActivity;
import com.example.reshoe_fbu.databinding.FragmentAccountInfoBinding;
import com.example.reshoe_fbu.databinding.FragmentDetailShoeBinding;
import com.example.reshoe_fbu.models.Post;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

public class DetailShoeFragment extends Fragment {

    private Post post;

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
        binding.tvDetailCondition.setText(post.getCondition() + "/10");
        binding.tvDetailedPrice.setText("$" + post.getPrice());
        binding.tvDetailedDescription.setText(post.getDescription());
        binding.tvDetailName.setText(post.getShoeName());
        binding.tvDetailSellerUser.setText(post.getUser().getUsername());

        Glide.with(view).load(post.getUser().getParseFile("profilePic").getUrl()).circleCrop().into(binding.ibSellerProfile);
        Glide.with(view).load(post.getImage().getUrl()).circleCrop().into(binding.ivDetailPic);

        // Return back to the previous fragment (timeline)
        binding.btnBackDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Go to checkout (activity or fragment?)
        binding.btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CheckoutActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });


    }

}
