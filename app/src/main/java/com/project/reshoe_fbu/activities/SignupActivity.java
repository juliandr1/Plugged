package com.project.reshoe_fbu.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reshoe_fbu.databinding.ActivitySignupBinding;
import com.project.reshoe_fbu.models.User;
import com.parse.ParseUser;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "SignupActivity";
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
            public void onClick(View v) {

                // set various data points according to user input
                user.setUsername(binding.etUsernameCreate.getText().toString());
                user.setPassword(binding.etPasswordCreate.getText().toString());
                user.setEmail(binding.etEmail.getText().toString());
                user.setFirstName(binding.etFirstName.getText().toString());
                user.setLastName(binding.etLastName.getText().toString());

                // Else if needed or else if a person does not click anything
                if (binding.rgGroup.getCheckedRadioButtonId() == binding.rbBuyer.getId()) {
                    user.setIsSeller(false);
                } else if (binding.rgGroup.getCheckedRadioButtonId() == binding.rbSeller.getId()){
                    user.setIsSeller(true);
                } else {
                    Toast.makeText(context, "Must select buyer or seller", Toast.LENGTH_SHORT).show();
                }

                user.signUp(context);

                goMainActivity();
            }
        });

    }

    // Go to the main timeline
    private void goMainActivity() {
        Intent intent = new Intent(this, TimelineActivity.class);
        startActivity(intent);
    }
}
