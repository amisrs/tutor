package com.amisrs.gavin.tutorhelp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.TutorialQueries;
import com.amisrs.gavin.tutorhelp.model.Tutor;
import com.amisrs.gavin.tutorhelp.model.Tutorial;

public class NewTutorialActivity extends AppCompatActivity {
    private static final String TAG = "NewTutorialActivity";

    Tutor tutor;
    Button createButton;
    EditText name;
    EditText timeSlot;
    EditText location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tutorial);
        tutor = getIntent().getParcelableExtra("tutor");

        createButton = (Button)findViewById(R.id.btn_create);
        name = (EditText)findViewById(R.id.et_name);
        timeSlot = (EditText)findViewById(R.id.et_timeSlot);
        location = (EditText)findViewById(R.id.et_location);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTutorial();
            }
        });
    }

    public void createTutorial() {
        TutorialQueries tutorialQueries = new TutorialQueries(this);
        Tutorial newTutorial = new Tutorial(tutor.getTutorID(),
                                            name.getText().toString(),
                                            timeSlot.getText().toString(),
                                            location.getText().toString());
        tutorialQueries.addTutorial(newTutorial);
        finish();
    }
}
