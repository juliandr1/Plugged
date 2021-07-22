package com.project.reshoe_fbu.activities.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.reshoe_fbu.R;
import com.parse.ParseException;
import com.project.reshoe_fbu.activities.LoginActivity;
import com.example.reshoe_fbu.databinding.FragmentProfileBinding;
import com.parse.ParseUser;
import com.project.reshoe_fbu.models.User;

import org.jetbrains.annotations.NotNull;


public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.
            Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentProfileBinding binding = FragmentProfileBinding.bind(view);

        User currentUser = new User(ParseUser.getCurrentUser());

        try {
            Glide.with(view).
                    load(currentUser.getProfilePicURL()).
                    circleCrop().
                    into(binding.ivProfile);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }

        // If the user clicks, then go to the account info fragment
        binding.tvAccountInfo.setOnClickListener(v -> {
            Fragment accountInfoFragment = new AccountInfoFragment();
            getActivity().
                    getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.flContainer, accountInfoFragment).
                    addToBackStack("back").
                    commit();
        });

        binding.tvUserPreferences.setOnClickListener(v -> {

            Fragment userPreferenceFragment;
            if (currentUser.getIsSeller()) {
                userPreferenceFragment = new UserPreferencesSellerFragment();
            } else {
                userPreferenceFragment = new UserPreferencesBuyerFragment();
            }
            getActivity().
                    getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.flContainer, userPreferenceFragment).
                    addToBackStack("back").
                    commit();
        });

        binding.tvLogOut.setOnClickListener(v -> {
            ParseUser.logOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });
    }
}