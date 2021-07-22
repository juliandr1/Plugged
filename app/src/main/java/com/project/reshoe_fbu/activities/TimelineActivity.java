package com.project.reshoe_fbu.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.example.reshoe_fbu.R;
import com.project.reshoe_fbu.activities.fragments.ThreadFragment;
import com.project.reshoe_fbu.activities.fragments.ProfileFragment;
import com.project.reshoe_fbu.activities.fragments.TimelineBuyerFragment;
import com.project.reshoe_fbu.activities.fragments.TimelineSellerFragment;
import com.project.reshoe_fbu.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        User currentUser = new User(ParseUser.getCurrentUser());

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment fragmentThread = new ThreadFragment();
        final Fragment fragmentProfile = new ProfileFragment();
        final Fragment fragmentTimeline;

        if (currentUser.getIsSeller()) {
            fragmentTimeline = new TimelineSellerFragment();
        } else {
            fragmentTimeline = new TimelineBuyerFragment();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            // Based on which item is clicked go to the appropriate fragment
            switch (item.getItemId()) {
                case R.id.message:
                    fragment = fragmentThread;
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