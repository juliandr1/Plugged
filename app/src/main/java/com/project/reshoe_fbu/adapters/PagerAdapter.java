package com.project.reshoe_fbu.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.reshoe_fbu.R;

import java.util.List;
import java.util.Objects;

public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {

    private static final String TAG = "PagerAdapter";

    private final Context mContext;
    private List<Bitmap> bitmaps;
    private List<String> images;

    private final LayoutInflater mLayoutInflater;

    // Used to distinguish in which scenario the adapter will be used.
    private final boolean isPosting;

    // Viewpager Constructor
    public PagerAdapter(Context context, List<String> images, boolean isPosting) {
        this.mContext = context;
        this.images = images;
        this.mLayoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isPosting = isPosting;
    }

    public PagerAdapter(Context context, List<Bitmap> bitmaps) {
        this.mContext = context;
        this.bitmaps = bitmaps;
        this.mLayoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        isPosting = true;
    }

    @Override
    public int getCount() {
        // Depending on if the user is viewing the view pager in the posting screen (bitmaps) or the
        // detailed shoe screen (images) return the count of the appropriate list
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
        View itemView = mLayoutInflater.inflate(R.layout.item_shoe_img, container,
                false);

        ImageView imageView =  itemView.findViewById(R.id.imageViewMain);

        if (isPosting) {
            imageView.setImageBitmap(bitmaps.get(position));
        } else {
            Glide.with(mContext).
                    load(images.get(position)).
                    override(370, 370).
                    into(imageView);
        }
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
