package com.project.reshoe_fbu.activities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentAccountInfoBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.project.reshoe_fbu.models.User;

import org.jetbrains.annotations.NotNull;

public class AccountInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.
            Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentAccountInfoBinding binding = FragmentAccountInfoBinding.bind(view);

        User currentUser = new User(ParseUser.getCurrentUser());

        binding.tvFirstNameInfo.setText(currentUser.getFirstName());
        binding.tvLastNameInfo.setText(currentUser.getLastName());
        binding.tvEmailInfo.setText(currentUser.getEmail());
        try {
            binding.tvUsernameInfo.setText(currentUser.getUsername());
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }

        binding.btnChangeUsername.setOnClickListener(v -> {
            // To be implemented in strech
        });

        binding.btnChangePassword.setOnClickListener(v -> {
            // To be implemented in stretch
        });

        binding.btnBackInfo.setOnClickListener(v -> getActivity().getSupportFragmentManager().
                popBackStack());
    }
}