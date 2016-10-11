package com.amisrs.gavin.tutorhelp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by Gavin on 11/10/2016.
 */
public class TableHelper {
    private static final String TAG = "TableHelper";

    public TableLayout tableLayout;
    public TableRow firstRow;
    public Context context;

    public TableHelper(TableLayout tableLayout) {
        this.tableLayout = tableLayout;
        this.firstRow = (TableRow)tableLayout.getChildAt(0);
        context = tableLayout.getContext();
        makeTableCool();
    }

    private void makeTableCool() {
        TableRow firstRow = new TableRow(context);
        TextView studentLabel = new TextView(context);
        studentLabel.setText(context.getString(R.string.studentLabel));
        firstRow.addView(studentLabel);
        tableLayout.addView(firstRow);

        addAddWeekButton();
        addAddStudentButton();
    }

    public void addColumn() {
        //use this when you add a week
        //remove the last one (it should be an add button)
        TableRow topRow = (TableRow)tableLayout.getChildAt(0);

        topRow.removeView(topRow.getChildAt(topRow.getChildCount()-1));


        //add new column by adding child to each row
        Log.d(TAG, "Add new week, number of rows: " + tableLayout.getChildCount());
        for(int i=0; i<tableLayout.getChildCount()-1; i++) {
            Log.d(TAG, "Adding box for new week column; " + i);
            TableRow row = (TableRow)tableLayout.getChildAt(i);
            TextView placeholderForStudentWeek = new TextView(context);
            placeholderForStudentWeek.setText(String.valueOf(i));
            row.addView(placeholderForStudentWeek);
        }
        addAddWeekButton();
    }

    public void addRow() {
        //use this when you add a student
        //remove the last one (it should be an add button)
        Log.d(TAG, "Row count = " + tableLayout.getChildCount());

        int rowCount = tableLayout.getChildCount();
        View lastView = tableLayout.getChildAt(rowCount - 1);
        tableLayout.removeView(lastView);
        //add row and add box child for each week
        TableRow firstRow = new TableRow(context);
        Log.d(TAG, "First row should exist; " + firstRow.getId());
        firstRow = (TableRow)tableLayout.getChildAt(0);
        Log.d(TAG, "First row after; " + firstRow.getId());

        int rowItems = firstRow.getChildCount();

        TableRow newRow = new TableRow(context);
        for(int i=0; i<rowItems-1; i++) {
            TextView placeHolderForStudentWeek = new TextView(context);
            placeHolderForStudentWeek.setText(String.valueOf(i));
            newRow.addView(placeHolderForStudentWeek);
        }
        tableLayout.addView(newRow);
        addAddStudentButton();
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

    public void addAddStudentButton() {
        //add new row that just has a button
        TableRow bottomRow = new TableRow(context);
        Button addStudentButton = new Button(context);
        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRow();
            }
        });
        bottomRow.addView(addStudentButton);
        tableLayout.addView(bottomRow);


    }

}
