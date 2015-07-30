/**
 The MIT License (MIT)

 Copyright (c) 2015 Talha Can Havadar

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */
package com.scorptech.relativeswipelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by Talha Can Havadar on 29.7.2015.
 */
public class RelativeSwipeLayout extends RelativeLayout {

    GestureDetector gestureDetector;
    SwipeViewGestureListener mListener;
    private Context mContext;
    ArrayList<View> mChildren;
    boolean enableManualSwipe;
    boolean autoSwipe;

    public RelativeSwipeLayout(Context context) {
        super(context);
        mContext = context;

    }
    public RelativeSwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }
    public RelativeSwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void init(){
        mChildren = new ArrayList<>();
        for (int i = 0; i < this.getChildCount(); i++) {
            mChildren.add(this.getChildAt(i));
        }
        mListener = new SwipeViewGestureListener(mContext, mChildren);
        gestureDetector = new GestureDetector(mContext, mListener);
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    public void init(int interval) {
        mChildren = new ArrayList<>();
        for (int i = 0; i < this.getChildCount(); i++) {
            mChildren.add(this.getChildAt(i));
        }
        mListener = new SwipeViewGestureListener(mContext, mChildren, interval);
        gestureDetector = new GestureDetector(mContext, mListener);
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ViewParent parentView = getParent();
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            if (parentView != null) {
                parentView.requestDisallowInterceptTouchEvent(true);
                Log.i("AndroidSwipeLayout", "OnTouchIntercept");
            }
        }
        return false;
    }

    public void postRunnableOnSwipe(Runnable runnable){
        mListener.setRunnableOnSwipe(runnable);
    }

    public void removeRunnableOnSwipe() {
        mListener.removeRunnableOnSwipe();
    }

    public Runnable getRunnableOnSwipe() {
        return mListener.getRunnableOnSwipe();
    }

    public boolean isSwipeable(){
        return mListener.isSwipeable();
    }

    public boolean isAutoSwipeOn(){
        return mListener.isAutoSwipeOn();
    }

    public boolean isEnableManualSwipe() {
        return enableManualSwipe;
    }

    public void setEnableManualSwipe(boolean enableManualSwipe) {
        this.enableManualSwipe = enableManualSwipe;
        mListener.setSwipeable(enableManualSwipe);
    }

    public boolean isAutoSwipe() {
        return autoSwipe;
    }

    public void setAnimation(Animation anim, SwipeViewGestureListener.AnimationDirections animDirection){
        mListener.setAnimation(anim, animDirection);
    }

    public void setAutoSwipe(boolean autoSwipe) {
        this.autoSwipe = autoSwipe;
        mListener.setAutoSwipe(autoSwipe);
    }
}
