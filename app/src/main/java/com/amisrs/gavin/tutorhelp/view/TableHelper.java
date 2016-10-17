package com.amisrs.gavin.tutorhelp.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.TutorialQueries;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutorial;

import java.util.ArrayList;

/**
 * Created by Gavin on 11/10/2016.
 */
public class TableHelper {
    private static final String TAG = "TableHelper";

    public Tutorial tutorial;
    public TableLayout tableLayout;
    public TableRow firstRow;
    public Context context;

    public TableHelper(TableLayout tableLayout, Tutorial tutorial) {
        this.tableLayout = tableLayout;
        this.firstRow = (TableRow)tableLayout.getChildAt(0);
        this.tutorial = tutorial;
        Log.d(TAG, "TableHelper created for tutorial: " + tutorial.getName());
        context = tableLayout.getContext();
        makeTableCool();
    }

    private void makeTableCool() {
        TableRow firstRow = new TableRow(context);
        TextView studentLabel = new TextView(context);
        //this is the top left corner of the table
        studentLabel.setText(tutorial.getName());
        firstRow.addView(studentLabel);
        tableLayout.addView(firstRow);

        TutorialQueries tutorialQueries = new TutorialQueries(context);
        ArrayList<Student> students = tutorialQueries.getStudentsForTutorial(tutorial);
        for(Student s : students) {
            addRow(s);
        }

        addAddWeekButton();
        //addAddStudentButton();
    }

    public void addColumn() {
        //use this when you add a week
        //remove the last one (it should be an add button)
        TableRow topRow = (TableRow)tableLayout.getChildAt(0);

        topRow.removeView(topRow.getChildAt(topRow.getChildCount()-1));
        //top row should have week square
        TextView weekSquare = new TextView(context);
        weekSquare.setText("new week");
        topRow.addView(weekSquare);
        //add new column by adding child to each row (except first)

        Log.d(TAG, "Add new week, number of rows: " + tableLayout.getChildCount());
        for(int i=1; i<tableLayout.getChildCount(); i++) {
            Log.d(TAG, "Adding box for new week column; row " + i);
            TableRow row = (TableRow)tableLayout.getChildAt(i);

            StudentWeekSquare placeholderForStudentWeek = new StudentWeekSquare(context);
            row.addView(placeholderForStudentWeek);
        }
        addAddWeekButton();
    }

    public void addRow(Student student) {
        //use this when you add a student
        //remove the last one (it should be an add button)
        Log.d(TAG, "Row count = " + tableLayout.getChildCount());

//        int rowCount = tableLayout.getChildCount();
//        View lastView = tableLayout.getChildAt(rowCount-1);
//        tableLayout.removeView(lastView);
        //add row and add box child for each week
        TableRow firstRow = new TableRow(context);
        Log.d(TAG, "First row should exist; " + firstRow.getId());
        firstRow = (TableRow)tableLayout.getChildAt(0);
        Log.d(TAG, "First row after; " + firstRow.getId());

        int rowItems = firstRow.getChildCount();

        //first column should have student square

        TableRow newRow = new TableRow(context);
        //newrow add student square first
        TextView studentSquare = new TextView(context);
        studentSquare.setText(student.toString());
        newRow.addView(studentSquare);
        for(int i=0; i<rowItems-1; i++) {
            Log.d(TAG, "Adding new square for number of rowItems, currently at: " + i);
            StudentWeekSquare placeHolderForStudentWeek = new StudentWeekSquare(context);
            newRow.addView(placeHolderForStudentWeek);
        }
        tableLayout.addView(newRow);
        //addAddStudentButton();
    }

    public void addAddWeekButton() {
        //add button to the top row
        TableRow topRow = (TableRow)tableLayout.getChildAt(0);
        Button addWeekButton = new Button(context);
        addWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addColumn();
            }
        });
        topRow.addView(addWeekButton);
    }

//    public void addAddStudentButton() {
//        //add new row that just has a button
//        TableRow bottomRow = new TableRow(context);
//        Button addStudentButton = new Button(context);
//        addStudentButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addRow();
//            }
//        });
//        bottomRow.addView(addStudentButton);
//        tableLayout.addView(bottomRow);
//
//
//    }

}
