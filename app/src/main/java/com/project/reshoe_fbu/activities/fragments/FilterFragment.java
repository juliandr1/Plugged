package com.project.reshoe_fbu.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentFilterBinding;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.project.reshoe_fbu.models.Post;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment {

    public static int NOT_CHANGED_CODE = 111;
    public static int CHANGED_IS_WOMEN_SIZING_M = 222;
    public static int CHANGED_IS_WOMEN_SIZING_W = 333;
    public static int CHANGED_IS_HIGH_TO_LOW = 444;
    public static int CHANGED_IS_LOW_TO_HIGH = 555;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        FragmentFilterBinding binding = FragmentFilterBinding.bind(view);

        ParseQuery<Post> query = new ParseQuery<Post>(Post.class);


        binding.btnBackFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("filter", false);

                Fragment prevFragment = new SearchPostFragment();
                prevFragment.setArguments(bundle);

                getActivity().
                        getSupportFragmentManager().
                        beginTransaction().
                        replace(R.id.flContainer, prevFragment).
                        commit();
            }
        });

        binding.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment prevFragment = new SearchPostFragment();
                Bundle bundle = new Bundle();

                boolean changed = false;
                String sizeStr = binding.etFilterSize.getEditableText().toString();
                String conditionStr = binding.etFilterCondition.getEditableText().toString();

                // Ternary operators ? enter here
                if (!sizeStr.isEmpty()) {
                    bundle.putInt("size", Integer.parseInt(sizeStr));
                    changed = true;
                } else {
                    bundle.putInt("size", NOT_CHANGED_CODE);
                }

                if (!conditionStr.isEmpty()) {
                    bundle.putInt("condition", Integer.parseInt(conditionStr));
                    changed = true;
                } else {
                    bundle.putInt("condition", NOT_CHANGED_CODE);
                }

                if (binding.rgGroupFilterSize.getCheckedRadioButtonId() ==
                        binding.rbMenFilter.getId()) {
                    bundle.putInt("isWomenSizing", CHANGED_IS_WOMEN_SIZING_M);
                    changed = true;
                } else if (binding.rgGroupFilterSize.getCheckedRadioButtonId() ==
                        binding.rbWomenFilter.getId()) {
                    bundle.putInt("isWomenSizing", CHANGED_IS_WOMEN_SIZING_W);
                    changed = true;
                } else {
                    bundle.putInt("isWomenSizing", NOT_CHANGED_CODE);
                }

                if (binding.rgGroupFilterPrice.getCheckedRadioButtonId() ==
                        binding.rbHighToLow.getId()) {
                    bundle.putInt("isHighToLow", CHANGED_IS_HIGH_TO_LOW);
                    changed = true;
                } else if (binding.rgGroupFilterPrice.getCheckedRadioButtonId() ==
                        binding.rbLowToHigh.getId()) {
                    bundle.putInt("isHighToLow", CHANGED_IS_LOW_TO_HIGH);
                    changed = true;
                } else {
                    bundle.putInt("isHighToLow", NOT_CHANGED_CODE);
                }

                bundle.putBoolean("filter", changed);
                prevFragment.setArguments(bundle);

                getActivity().
                        getSupportFragmentManager().
                        beginTransaction().
                        replace(R.id.flContainer, prevFragment).
                        commit();
            }
        });
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container,
                false);
    }
}
