package com.example.reshoe_fbu.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.activities.fragments.MessageFragment;
import com.example.reshoe_fbu.activities.fragments.ProfileFragment;
import com.example.reshoe_fbu.activities.fragments.TimelineBuyerFragment;
import com.example.reshoe_fbu.activities.fragments.TimelineSellerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ParseUser currentUser = ParseUser.getCurrentUser();

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment fragmentMessage = new MessageFragment();
        final Fragment fragmentProfile = new ProfileFragment();
        final Fragment fragmentTimeline;

        if (currentUser.getBoolean("isSeller")) {
            fragmentTimeline = new TimelineSellerFragment();
        } else {
            fragmentTimeline = new TimelineBuyerFragment();
        }

        // Setup the bottom navigation view for the activity and outline the flow
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.message:
                    fragment = fragmentMessage;
                    Log.i(TAG,"MESSAGE");
                    break;
                case R.id.profile:
                    fragment = fragmentProfile;
                    Log.i(TAG,"PROFILE");
                    break;
                case R.id.timeline:
                default:
                    fragment = fragmentTimeline;
                    Log.i(TAG,"TIMELINE");
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.timeline);
    }
}