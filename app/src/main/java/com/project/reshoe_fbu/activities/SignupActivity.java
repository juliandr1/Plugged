package com.project.reshoe_fbu.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reshoe_fbu.databinding.ActivitySignupBinding;
import com.parse.ParseException;
import com.parse.SignUpCallback;
import com.project.reshoe_fbu.models.User;
import com.parse.ParseUser;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "CertifiedLover";

    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding
        ActivitySignupBinding binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        User user = new User(new ParseUser());

        context = this;

        // Fill in all of the data and attempt to create a new user
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Set the various data points
                user.setUsername(binding.etUsernameCreate.getText().toString());
                user.setPassword(binding.etPasswordCreate.getText().toString());
                user.setEmail(binding.etEmail.getText().toString());
                user.setFirstName(binding.etFirstName.getText().toString());
                user.setLastName(binding.etLastName.getText().toString());

                if (binding.rgGroup.getCheckedRadioButtonId() == binding.rbBuyer.getId()) {
                    user.setIsSeller(false);
                } else if (binding.rgGroup.getCheckedRadioButtonId() == binding.rbSeller.getId()){
                    user.setIsSeller(true);
                }

                user.getUser().signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            goMainActivity();
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    // Go to the main timeline
    private void goMainActivity() {
        Intent intent = new Intent(this, TimelineActivity.class);
        startActivity(intent);
    }
}
