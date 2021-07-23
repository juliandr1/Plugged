package com.project.reshoe_fbu.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.ActivityCartBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.project.reshoe_fbu.adapters.CartAdapter;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.User;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    public static String TAG = "CartActivity";

    private List<Post> cartItems;
    private CartAdapter adapter;
    private ActivityCartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Context context = this;

        cartItems = new ArrayList<>();
        RecyclerView rvItems = binding.rvItems;
        adapter = new CartAdapter(this, cartItems, binding);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rvItems.setLayoutManager(layoutManager);
        rvItems.setAdapter(adapter);

        binding.btnBackCart.setOnClickListener(v -> { finish(); });

        try {
            queryCartItems();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
    }

    private void queryCartItems() throws JSONException, ParseException {
        User currentUser = new User(ParseUser.getCurrentUser());
        cartItems.addAll(currentUser.getCart());
        adapter.notifyDataSetChanged();
        int totalPrice = getCartTotal();
        binding.tvTotalCart.setText(getString(R.string.money) + totalPrice);
    }

    private int getCartTotal() {
        int total = 0;

        for (int i = 0; i < cartItems.size(); i++) {
            total += cartItems.get(i).getPrice();
        }

        return total;
    }
}
