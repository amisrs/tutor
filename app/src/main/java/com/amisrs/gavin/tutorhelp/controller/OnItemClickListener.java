package com.amisrs.gavin.tutorhelp.controller;

import android.view.View;

import com.amisrs.gavin.tutorhelp.model.Student;

/**
 * Created by Gavin on 18/10/2016.
 */
public interface OnItemClickListener {
    void onStudentClick(View view, Student student);
}
