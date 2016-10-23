package com.amisrs.gavin.tutorhelp.controller;

import android.view.View;

import com.amisrs.gavin.tutorhelp.model.Assessment;

/**
 * Created by Gavin on 23/10/2016.
 */

public interface OnAssessmentClickListener {
    void onAssessmentClicked(View view, Assessment assessment);
}
