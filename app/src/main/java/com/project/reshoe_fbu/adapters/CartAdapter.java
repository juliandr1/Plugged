package com.project.reshoe_fbu.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.reshoe_fbu.R;
import com.example.reshoe_fbu.databinding.ActivityCartBinding;
import com.parse.ParseUser;
import com.project.reshoe_fbu.activities.CartActivity;
import com.project.reshoe_fbu.activities.fragments.DetailShoeFragment;
import com.project.reshoe_fbu.activities.fragments.DetailShoeSellerFragment;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    public static String TAG = "CartAdapter";

    private final Context mContext;

    private final List<Post> cartItems;

    private final ActivityCartBinding binding;

    private final FragmentManager fragmentManager;

    public CartAdapter(Context context, List<Post> cartItems, ActivityCartBinding binding,
                       FragmentManager fragmentManager) {
        this.mContext = context;
        this.cartItems = cartItems;
        this.binding = binding;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cart, parent,
                false);

        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartAdapter.ViewHolder holder,
                                 int position) {
        Post post = cartItems.get(position);

        try {
            holder.bind(post);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivPhotoPreviewCart;
        private final TextView tvShoeTitleCart, tvSizeCart, tvPriceCart;
        private final Button btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPhotoPreviewCart = itemView.findViewById(R.id.ivPhotoPreviewCart);
            tvShoeTitleCart = itemView.findViewById(R.id.tvShoeTitleCart);
            tvSizeCart = itemView.findViewById(R.id.tvSizeCart);
            tvPriceCart = itemView.findViewById(R.id.tvPriceCart);
            btnRemove = itemView.findViewById(R.id.btnRemove);

        }

        public void bind(Post post) throws JSONException {

            tvShoeTitleCart.setText(post.getShoeName());
            tvPriceCart.setText(mContext.getString(R.string.money) + post.getPrice());
            Glide.with(mContext).load(post.getImageUrls().get(0)).into(ivPhotoPreviewCart);

            double size = post.getSize();

            // Check to see if its a whole size or .5 & ternary
            if (size == Math.floor(size)) {
                String str = String.valueOf((int)(size));
                str = checkSizing(str, post);
                tvSizeCart.setText(str);
            } else {
                String str = String.valueOf(size);
                str = checkSizing(str, post);
                tvSizeCart.setText(str);
            }

            btnRemove.setOnClickListener(v -> {
                User user = new User(ParseUser.getCurrentUser());
                user.removeFromCart(post.getObjectId());
                cartItems.remove(post);
                int totalPrice = getCartTotal();
                binding.tvTotalCart.setText(mContext.getString(R.string.money) + totalPrice);
                notifyDataSetChanged();
            });
        }
    }

    private String checkSizing(String str, Post post) {
        if (post.getIsWomenSizing()) {
            return str.concat(mContext.getString(R.string.women));
        } else {
            return str.concat(mContext.getString(R.string.men));
        }
    }

    private int getCartTotal() {
        int total = 0;

        for (int i = 0; i < cartItems.size(); i++) {
            total += cartItems.get(i).getPrice();
        }

        return total;
    }
}
