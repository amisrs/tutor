/*
package com.amisrs.gavin.tutorhelp.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amisrs.gavin.tutorhelp.R;

*/
/**
 * Created by Gavin on 16/10/2016.
 *//*

public class StudentWeekSquare extends ImageView implements GestureDetector.OnGestureListener {
    //TODO: make it look better; handle more touch events; change colour on comment
    private static final String TAG = "StudentWeekSquare";
    //private StudentWeek studentWeek;
    private GestureDetectorCompat detector;
    boolean attended = false;
    public static boolean isDragging = false;
    //http://stackoverflow.com/questions/6410200/android-detect-if-user-touches-and-drags-out-of-button-region?noredirect=1&lq=1
    Rect bounds;



    public StudentWeekSquare(final Context context) {
        super(context);
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        setLayoutParams(layoutParams);
        if(attended) {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_done_black_24dp));
        } else {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.blank));
        }
        detector = new GestureDetectorCompat(context,this);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detector.onTouchEvent(motionEvent);
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    bounds = new Rect(getLeft(), getTop(), getRight(), getBottom());
                } else if(motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    if(!bounds.contains(getLeft(), (int) motionEvent.getX(), getTop(), (int)motionEvent.getY())) {
                        setBackgroundColor(Color.TRANSPARENT);
                        Log.d(TAG, "Moved out of bounds.");
                    }
                }
                return true;
            }
        });
    }

    public void setIsDragging(Boolean bool) {
        isDragging = bool;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        Log.d(TAG, "Long pressed.");
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        bounds = new Rect(getLeft(), getTop(), getRight(), getBottom());

        setBackgroundColor(Color.GRAY);
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        setBackgroundColor(Color.TRANSPARENT);
        if (!attended) {
            setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_done_black_24dp));
            attended = true;
        } else {
            setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.blank));
            attended = false;
        }

        return false;
    }


    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
*/
