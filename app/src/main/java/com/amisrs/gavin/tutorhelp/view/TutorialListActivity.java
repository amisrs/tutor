package com.amisrs.gavin.tutorhelp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;

public class TutorialListActivity extends AppCompatActivity {
    private static final String TAG = "TutorialListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_list);
        int tutorZID = getIntent().getIntExtra("zid", 0);

        if(tutorZID == 0) {
            Log.e(TAG, "No zID was received from the Intent.");
            //stop
            finish();
        }


        //get an arraylist of tutorials for this tutor

        Log.d(TAG, "Showing tutorials for: " + tutorZID);

        TextView helloName = (TextView)findViewById(R.id.tv_helloName);

        helloName.setText(" " + tutorZID + "!");

    }
}
