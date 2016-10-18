package com.amisrs.gavin.tutorhelp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.model.Tutorial;

public class MenuActivity extends AppCompatActivity {
    //TODO: decide what happens with this activity (get rid of it?)
    private static final String TAG = "MenuActivity";
    TextView nameTextView;
    Button attendanceButton;
    Tutorial tutorial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent fromIntent = getIntent();
        tutorial = fromIntent.getParcelableExtra("tutorial");


        attendanceButton = (Button)findViewById(R.id.btn_attendance);
        nameTextView = (TextView)findViewById(R.id.tv_name);

        nameTextView.setText(tutorial.getName());
        attendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAttendance();
            }
        });
    }

    public void goToAttendance() {
        Intent intent = new Intent(this, BaseActivity.class);
        intent.putExtra("tutorial", tutorial);
        Log.d(TAG, "Put extra tutorial: " + tutorial.getName());
        startActivity(intent);
    }
}
