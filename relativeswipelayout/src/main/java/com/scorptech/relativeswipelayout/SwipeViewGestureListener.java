/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Talha Can Havadar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.scorptech.relativeswipelayout;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;


/**
 * Created by Talha Can Havadar on 29.7.2015.
 */
public class SwipeViewGestureListener implements GestureDetector.OnGestureListener {

    public enum AnimationDirections {
        ANIMATON_OUT_DIRECTON1, ANIMATION_IN_DIRECTON1,
        ANIMATION_OUT_DIRECTION2, ANIMATION_IN_DIRECTION2
    }

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private boolean swipeable;

    private Animation animInDirection1;
    private Animation animOutDirection1;
    private Animation animInDirection2;
    private Animation animOutDirection2;

    private ArrayList<View> mChildren;
    private int mCurrentViewIndex;
    private Handler mHandler;
    private Runnable mRunnable;
    private Runnable addedRunnableOnSwipe;
    private int interval = -1;
    private Context context;
    private boolean autoSwipeOn;

    public SwipeViewGestureListener(Context context, ArrayList<View> children) {
        this.context = context;
        animOutDirection2 = AnimationUtils.loadAnimation(context, R.anim.slide_out_left_for_swipe_layout);
        animInDirection1 = AnimationUtils.loadAnimation(context, R.anim.slide_in_right_for_swipe_layout);
        animInDirection2 = AnimationUtils.loadAnimation(context, R.anim.slide_in_left_for_swipe_layout);
        animOutDirection1 = AnimationUtils.loadAnimation(context, R.anim.slide_out_right_for_swipe_layout);
        mChildren = children;
        swipeable = true;
        mCurrentViewIndex = 0;
        mHandler = null;
        addedRunnableOnSwipe = null;
        for (int i = 0; i < mChildren.size(); i++) {
            mChildren.get(i).setVisibility(View.GONE);
        }
        mChildren.get(0).setVisibility(View.VISIBLE);
        autoSwipeOn = false;
        mRunnable = new Runnable() {
            @Override
            public void run() {
                bringNextView();
                mHandler.postDelayed(this, interval);
            }
        };
    }

    public SwipeViewGestureListener(Context context, ArrayList<View> children, final int interval) {
        this.context = context;
        animOutDirection2 = AnimationUtils.loadAnimation(context, R.anim.slide_out_left_for_swipe_layout);
        animInDirection1 = AnimationUtils.loadAnimation(context, R.anim.slide_in_right_for_swipe_layout);
        animInDirection2 = AnimationUtils.loadAnimation(context, R.anim.slide_in_left_for_swipe_layout);
        animOutDirection1 = AnimationUtils.loadAnimation(context, R.anim.slide_out_right_for_swipe_layout);
        swipeable = true;
        mChildren = children;
        addedRunnableOnSwipe = null;
        mCurrentViewIndex = 0;
        for (int i = 0; i < mChildren.size(); i++) {
            mChildren.get(i).setVisibility(View.GONE);
        }
        mChildren.get(0).setVisibility(View.VISIBLE);
        mHandler = new Handler();
        this.interval = interval;
        mRunnable = new Runnable() {
            @Override
            public void run() {
                bringNextView();
                mHandler.postDelayed(this, interval);
            }
        };
        autoSwipeOn = true;
        mHandler.postDelayed(mRunnable, this.interval);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    private void bringNextView() {
        int previousView = mCurrentViewIndex;
        mCurrentViewIndex = (mCurrentViewIndex + 1) % mChildren.size();
        mChildren.get(mCurrentViewIndex).setVisibility(View.VISIBLE);
        mChildren.get(previousView).setVisibility(View.GONE);
        mChildren.get(previousView).startAnimation(animOutDirection2);
        mChildren.get(mCurrentViewIndex).startAnimation(animInDirection1);
        if (addedRunnableOnSwipe != null)
            mHandler.post(addedRunnableOnSwipe);
    }

    private void bringPrevView() {
        int nextView = mCurrentViewIndex;
        mCurrentViewIndex = ((mCurrentViewIndex - 1) + mChildren.size()) % mChildren.size();
        mChildren.get(mCurrentViewIndex).setVisibility(View.VISIBLE);
        mChildren.get(nextView).setVisibility(View.GONE);
        mChildren.get(nextView).startAnimation(animOutDirection1);
        mChildren.get(mCurrentViewIndex).startAnimation(animInDirection2);
        if (addedRunnableOnSwipe != null)
            mHandler.post(addedRunnableOnSwipe);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("AndroidSwipeLayout", "onFling");
        if (swipeable && e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            bringNextView();
            if (mHandler != null) {
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, this.interval);
            }
            Log.d("AndroidSwipeLayout", "Right to Left");
            return false;
        } else if (swipeable && e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            bringPrevView();
            Log.d("AndroidSwipeLayout", "Left to Right");
            if (mHandler != null) {
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, this.interval);
            }
            return false; // Left to right
        }

        return false;
    }

    public boolean isSwipeable() {
        return swipeable;
    }

    public void setSwipeable(boolean swipeable) {
        this.swipeable = swipeable;
    }

    public boolean isAutoSwipeOn() {
        return autoSwipeOn;
    }

    public void setAutoSwipe(boolean auto) {
        if (auto) {
            if (mHandler == null)
                mHandler = new Handler();
            mHandler.postDelayed(mRunnable, this.interval);
            autoSwipeOn = true;
        } else {
            mHandler.removeCallbacks(mRunnable);
            autoSwipeOn = false;
        }
    }

    public void setRunnableOnSwipe(Runnable runnable) {
        addedRunnableOnSwipe = runnable;
    }

    public void removeRunnableOnSwipe() {
        if (addedRunnableOnSwipe != null) {
            if (mHandler != null)
                mHandler.removeCallbacks(addedRunnableOnSwipe);
            addedRunnableOnSwipe = null;
        }
    }

    public Runnable getRunnableOnSwipe() {
        return addedRunnableOnSwipe;
    }

    public void setAnimation(Animation anim, AnimationDirections animDirection) {
        if (!(animDirection == AnimationDirections.ANIMATION_IN_DIRECTON1 || animDirection == AnimationDirections.ANIMATION_IN_DIRECTION2
                || animDirection == AnimationDirections.ANIMATION_OUT_DIRECTION2 || animDirection == AnimationDirections.ANIMATON_OUT_DIRECTON1)) {
            throw new AssertionError("Invalid animDirection.");
        }
        if (anim == null) {
            throw new AssertionError("Animation object can not be null.");
        }
        switch (animDirection) {
            case ANIMATION_IN_DIRECTION2:
                animInDirection2 = anim;
                break;
            case ANIMATION_IN_DIRECTON1:
                animInDirection1 = anim;
                break;
            case ANIMATION_OUT_DIRECTION2:
                animOutDirection2 = anim;
                break;
            case ANIMATON_OUT_DIRECTON1:
                animOutDirection1 = anim;
                break;
            default:
                break;
        }

    }


}
