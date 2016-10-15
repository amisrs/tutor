package com.amisrs.gavin.tutorhelp.view;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amisrs.gavin.tutorhelp.db.DBHelper;
import com.amisrs.gavin.tutorhelp.R;

public class AttendanceActivity extends AppCompatActivity implements TableFragment.OnFragmentInteractionListener {
    private static final String TAG = "AttendanceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);


        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
