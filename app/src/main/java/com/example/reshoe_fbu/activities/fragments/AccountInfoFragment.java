package com.example.reshoe_fbu.activities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentAccountInfoBinding;
import com.example.reshoe_fbu.databinding.FragmentProfileBinding;
import com.example.reshoe_fbu.models.User;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

public class AccountInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentAccountInfoBinding binding = FragmentAccountInfoBinding.bind(view);

        ParseUser currentUser = ParseUser.getCurrentUser();

        binding.tvFirstNameInfo.setText(currentUser.getString("firstName"));
        binding.tvLastNameInfo.setText(currentUser.getString("lastName"));
        binding.tvEmailInfo.setText(currentUser.getEmail());
        binding.tvUsernameInfo.setText(currentUser.getUsername());

        binding.btnChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        binding.btnBackInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

    }
}