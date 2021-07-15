package com.project.reshoe_fbu.activities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentUserPreferencesSellerBinding;

import org.jetbrains.annotations.NotNull;

public class UserPreferencesSellerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_preferences_seller, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentUserPreferencesSellerBinding binding = FragmentUserPreferencesSellerBinding.bind(view);

        binding.btnBackUserPrefSeller.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());
    }
}