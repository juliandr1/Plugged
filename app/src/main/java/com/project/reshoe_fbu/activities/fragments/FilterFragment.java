package com.project.reshoe_fbu.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.FragmentFilterBinding;
import com.project.reshoe_fbu.models.Post;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FilterFragment extends Fragment {

    public static int NOT_CHANGED_CODE = 111;
    public static int CHANGED_IS_WOMEN_SIZING_M = 222;
    public static int CHANGED_IS_WOMEN_SIZING_W = 333;
    public static int CHANGED_IS_HIGH_TO_LOW = 444;
    public static int CHANGED_IS_LOW_TO_HIGH = 555;

    private int positionCondition = NOT_CHANGED_CODE;
    private double positionSize = NOT_CHANGED_CODE;

    private String str;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        FragmentFilterBinding binding = FragmentFilterBinding.bind(view);

        binding.btnBackFilter.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("filter", false);
            bundle.putBoolean("posts", true);
            ArrayList<Post> items = getArguments().getParcelableArrayList("items");
            bundle.putParcelableArrayList("items", items);

            Fragment prevFragment = new SearchPostFragment();
            prevFragment.setArguments(bundle);

            getActivity().
                    getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.flContainer, prevFragment).
                    commit();
        });

        Spinner staticSpinner = binding.staticSpinner;
        Spinner staticSpinner2 = binding.staticSpinner2;

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter2 = ArrayAdapter
                .createFromResource(getActivity(), R.array.condition_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter2
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner2.setAdapter(staticAdapter2);

        staticSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = staticSpinner2.getItemAtPosition(position).toString();
                if (str.isEmpty()) {
                    positionCondition = NOT_CHANGED_CODE;
                } else {
                    positionCondition = Integer.parseInt(str);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.size_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);

        staticSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = staticSpinner.getItemAtPosition(position).toString();
                if (str.isEmpty()) {
                    positionSize = NOT_CHANGED_CODE;
                } else {
                    positionSize = Double.parseDouble(str);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        binding.btnApply.setOnClickListener(v -> {
            Fragment prevFragment = new SearchPostFragment();
            Bundle bundle = new Bundle();

            str = getArguments().getString("str");

            boolean changed = false;

            // Ternary operators ? enter here
            if (((int) positionSize) != NOT_CHANGED_CODE) {
                bundle.putDouble("size", positionSize);
                changed = true;
            } else {
                bundle.putDouble("size", positionSize);
            }

            if (positionCondition != NOT_CHANGED_CODE) {
                bundle.putInt("condition", positionCondition);
                changed = true;
            } else {
                bundle.putInt("condition", positionCondition);
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
            bundle.putBoolean("shoe", false);
            bundle.putString("str", str);
            prevFragment.setArguments(bundle);

            getActivity().
                    getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.flContainer, prevFragment).
                    commit();
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
