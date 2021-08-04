package com.project.reshoe_fbu.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.reshoe_fbu.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.parse.ParseUser;
import com.project.reshoe_fbu.helper.OnDoubleTapListener;
import com.project.reshoe_fbu.models.Post;
import com.project.reshoe_fbu.models.User;

import org.json.JSONException;

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

    private Post post;

    // Viewpager Constructor
    public PagerAdapter(Context context, List<String> images, boolean isPosting, Post post) {
        this.mContext = context;
        this.images = images;
        this.mLayoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isPosting = isPosting;
        this.post = post;
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

        PhotoView photoView =  itemView.findViewById(R.id.imageViewMain);

        if (isPosting) {
            photoView.setImageBitmap(bitmaps.get(position));
        } else {
            Glide.with(mContext).
                    load(images.get(position)).centerInside().into(photoView);
            photoView.getAttacher().setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    return false;
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    User currentUser = new User(ParseUser.getCurrentUser());
                    Log.i(TAG, "clicked");
                    try {
                        if (!post.didLike(currentUser)) {
                            currentUser.addLike(post.getObjectId());
                            post.like();
                            Toast.makeText(mContext, "Liked post!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Add string resource
                            Toast.makeText(mContext, "Already liked!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                    return true;
                }

                @Override
                public boolean onDoubleTapEvent(MotionEvent e) {
                    return false;
                }
            });
        }
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
