package com.project.reshoe_fbu.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.bumptech.glide.Glide;
import com.example.reshoe_fbu.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.project.reshoe_fbu.helper.BitmapScaler;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {

    private static final String TAG = "PagerAdapter";
    // Context object
    private Context context;

    // Array of bitmaps
    private List<Bitmap> bitmaps;

    // Array of stringUrls
    private List<String> images;

    // Layout Inflater
    private LayoutInflater mLayoutInflater;

    // Used to distinguish in which scenario the adapter will be used.
    private boolean isPosting;

    // Viewpager Constructor
    public PagerAdapter(Context context, List<String> images, boolean isPosting) {
        this.context = context;
        this.images = images;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isPosting = isPosting;
    }

    public PagerAdapter(Context context, List<Bitmap> bitmaps) {
        this.context = context;
        this.bitmaps = bitmaps;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        isPosting = true;
    }

    @Override
    public int getCount() {
        // return the number of images
        if (isPosting) {
            return bitmaps.size();
        } else {
            return images.size();
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.item_shoe_img, container, false);

        // referencing the image view from the item.xml file
        ImageView imageView =  itemView.findViewById(R.id.imageViewMain);

        if (isPosting) {
            imageView.setImageBitmap(bitmaps.get(position));
        } else {
            Glide.with(context).load(images.get(position)).override(370, 370).into(imageView);
        }
        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
