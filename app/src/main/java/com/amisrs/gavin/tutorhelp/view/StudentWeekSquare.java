package com.amisrs.gavin.tutorhelp.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amisrs.gavin.tutorhelp.R;

/**
 * Created by Gavin on 16/10/2016.
 */
public class StudentWeekSquare extends ImageView {
    private static final String TAG = "StudentWeekSquare";
    //private StudentWeek studentWeek;
    boolean attended = false;

    public StudentWeekSquare(final Context context) {
        super(context);
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        setLayoutParams(layoutParams);
        if(attended) {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_done_black_24dp));
        } else {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.blank));
        }

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d(TAG, "Touched square.");
                //toggle attended
                if(!attended) {
                    setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_done_black_24dp));
                    attended = true;
                } else {
                    setImageDrawable(ContextCompat.getDrawable(context, R.drawable.blank));
                    attended = false;
                }
                return false;
            }
        });
    }
}
