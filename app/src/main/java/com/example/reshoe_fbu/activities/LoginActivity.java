package com.example.reshoe_fbu.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.reshoe_fbu.databinding.ActivityLoginBinding;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static String TAG = "LoginActivity";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            goMainActivity();
        }

        context = this;
        // Attempt to login, using the login button
        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString();
            String password = binding.etPassword.getText().toString();
            loginUser(username, password);
        });

        // Go to the register activity in order to create an account
        binding.btnsignup.setOnClickListener(v -> {
            Intent intent = new Intent(context, SignupActivity.class);
            startActivity(intent);
        });
    }

    // Login by checking if the user exists in the database. If yes go to their timeline, if not
    // show a toast saying they could not login.
    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login: " + username);
        ParseUser.logInInBackground(username, password, (user, e) -> {
            if (e != null) {
                Log.e(TAG, "Issue with login", e);
                Toast.makeText(context, "Username and password combination is incorrect or does not exist", Toast.LENGTH_SHORT).show();
                return;
            }
            goMainActivity();

        });
    }
    // Go to the timeline.
    private void goMainActivity() {
        Intent intent = new Intent(this, TimelineActivity.class);
        startActivity(intent);
    }


}
