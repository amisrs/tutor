package com.amisrs.gavin.tutorhelp.view;

import android.app.DialogFragment;
import android.nfc.Tag;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.amisrs.gavin.tutorhelp.db.DBHelper;
import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.model.Tutorial;

public class AttendanceActivity extends AppCompatActivity implements TableFragment.OnFragmentInteractionListener, NewStudentDialogFragment.OnFragmentInteractionListener {
    private static final String TAG = "AttendanceActivity";
    Tutorial tutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_attendance);
        Intent fromIntent = getIntent();
        tutorial = fromIntent.getParcelableExtra("tutorial");
        Log.d(TAG, "Got tutorial from intent: " + tutorial.getName());

        Fragment tableFragment = TableFragment.newInstance(tutorial);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.rl_main, tableFragment);
        fragmentTransaction.commit();


        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
    }

    @Override
    public void onFragmentInteraction(String name) {

    }



    public Tutorial getTutorial() {
        return tutorial;
    }
}
