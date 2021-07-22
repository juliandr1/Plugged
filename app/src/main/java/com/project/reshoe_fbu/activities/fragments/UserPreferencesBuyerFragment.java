package com.project.reshoe_fbu.activities.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentUserPreferencesBinding;

import org.jetbrains.annotations.NotNull;

public class UserPreferencesBuyerFragment extends Fragment {

    public static String TAG = "UserPreferencesBuyerFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_preferences, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.
            Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentUserPreferencesBinding binding = FragmentUserPreferencesBinding.bind(view);

        binding.btnBackUserPrefBuyer.setOnClickListener(v -> {
            Log.i(TAG, "clicked");
            getActivity().getSupportFragmentManager().popBackStack();
        });
    }
}
