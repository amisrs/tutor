package com.amisrs.gavin.tutorhelp;

import android.content.Context;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by Gavin on 11/10/2016.
 */
public class TableHelper {
    public TableLayout tableLayout;
    public Context context;

    public TableHelper(TableLayout tableLayout) {
        this.tableLayout = tableLayout;
        context = tableLayout.getContext();
    }

    public void makeTableCool() {
        TableRow firstRow = new TableRow(context);
        TextView studentLabel = new TextView(context);
        studentLabel.setText(context.getString(R.string.studentLabel));
        firstRow.addView(studentLabel);
        tableLayout.addView(firstRow);
    }

    public void addColumnOfButtons () {
        //add new column by adding child to each row
        for(int i=0; i<tableLayout.getChildCount(); i++) {
            TableRow row = (TableRow)tableLayout.getChildAt(i);
            //row.addView();
        }

    }
}
