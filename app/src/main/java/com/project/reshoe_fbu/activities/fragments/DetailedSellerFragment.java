package com.project.reshoe_fbu.activities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentDetailedSellerBinding;
import com.project.reshoe_fbu.models.User;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

public class DetailedSellerFragment extends Fragment {

    public static final String TAG = "DetailedSellerFragment";

    private ParseUser seller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the particular seller that was selected
        seller = getArguments().getParcelable("seller");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detailed_seller, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentDetailedSellerBinding binding = FragmentDetailedSellerBinding.bind(view);

        User currentUser = new User(ParseUser.getCurrentUser());

        // Load user data
        binding.tvUserName.setText(seller.getUsername());
        binding.tvSellerDescription.setText(seller.getString("description"));

        Glide.with(view).load(seller.getParseFile("profilePic").getUrl()).circleCrop().into(binding.ivSellerProfilePic);

        // If the user click the like button then check if we must like or unlike
        // the user.
        binding.btnLikeSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // If the user has liked the post before then unlike it.
                    if (currentUser.didLike(seller)) {
                        Log.i(TAG, "Unlike");
                        binding.btnLikeSeller.setBackgroundResource(R.drawable.heart_outline);
                        currentUser.unlike(seller);
                    } else {
                        Log.i(TAG, "Like");
                        binding.btnLikeSeller.setBackgroundResource(R.drawable.heart_filled);
                        currentUser.like(seller);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // If the user wants to message the user then go to the messaging fragment
        binding.btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", User.class);
                Fragment messageFragment = new MessagesFragment();
                messageFragment.setArguments(bundle);
            }
        });

        // Return back to the previous fragment (detailed shoe view)
        binding.btnBackDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}