package com.example.reshoe_fbu.activities.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.activities.LoginActivity;
import com.example.reshoe_fbu.databinding.FragmentProfileBinding;
import com.parse.Parse;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;


public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentProfileBinding binding = FragmentProfileBinding.bind(view);

        ParseUser currentUser = ParseUser.getCurrentUser();

        Glide.with(view).load(currentUser.getParseFile("profilePic").getUrl()).transform(new RoundedCorners(300)).into(binding.ivProfile);

        binding.tvAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment accountInfoFragment = new AccountInfoFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, accountInfoFragment).addToBackStack("back").commit();
            }
        });

        binding.tvUserPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment userPreferenceFragment = new UserPreferencesFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, userPreferenceFragment).commit();
            }
        });

        binding.tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}