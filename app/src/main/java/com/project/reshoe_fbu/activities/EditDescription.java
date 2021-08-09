package com.project.reshoe_fbu.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reshoe_fbu.databinding.ActivityEditDescriptionBinding;
import com.parse.ParseUser;
import com.project.reshoe_fbu.models.User;

public class EditDescription extends AppCompatActivity {

    private ActivityEditDescriptionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = this;

        binding = ActivityEditDescriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnCancelDesc.setOnClickListener(v -> finish());

        binding.btnEditDesc.setOnClickListener(v -> {
            String text = binding.etDescriptionSeller.getText().toString();
            if (text.isEmpty()) {
                Toast.makeText(context, "Descripton cannot be empty",
                        Toast.LENGTH_SHORT).show();
            } else {
                User currentUser = new User(ParseUser.getCurrentUser());
                currentUser.setDescription(text);
                finish();
            }
        });
    }
}