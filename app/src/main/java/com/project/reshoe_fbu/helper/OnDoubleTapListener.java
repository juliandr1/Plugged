package com.project.reshoe_fbu.helper;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONException;

public class OnDoubleTapListener implements View.OnTouchListener {

    private GestureDetector gestureDetector;

    public OnDoubleTapListener(Context c) {
        gestureDetector = new GestureDetector(c, new GestureListener());
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            try {
                OnDoubleTapListener.this.onDoubleTap(e);
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            return super.onDoubleTap(e);
        }
    }

    public void onDoubleTap(MotionEvent e) throws JSONException {
        // To be overridden when implementing listener
    }
}
