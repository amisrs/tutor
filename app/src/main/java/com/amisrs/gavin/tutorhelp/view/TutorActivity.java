package com.amisrs.gavin.tutorhelp.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.controller.LoadMoodleAsyncTask;
import com.amisrs.gavin.tutorhelp.controller.OnLoadListener;
import com.amisrs.gavin.tutorhelp.db.TutorQueries;
import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Tutor;
import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class TutorActivity extends AppCompatActivity implements NewTutorDialogFragment.OnFragmentInteractionListener,
        NewTutorDialogFragment.NewTutorDialogFragmentListener,
        OnLoadListener {
    //TODO: handle login as null
    private static final String TAG = "TutorActivity";
    Spinner tutorSpinner;
    Button loginButton;
    Button newButton;
    Button deleteButton;
    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor);


        tutorSpinner = (Spinner) findViewById(R.id.sp_tutor);
        loginButton = (Button) findViewById(R.id.btn_login);
        newButton = (Button) findViewById(R.id.btn_newTutor);
        deleteButton = (Button) findViewById(R.id.btn_deleteTutor);
        background = (ImageView) findViewById(R.id.iv_background);

        tutorSpinner.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        LoadMoodleAsyncTask asyncTask = new LoadMoodleAsyncTask(this);
        asyncTask.execute();

        if(tutorSpinner.getSelectedItem() == null) {
            deleteButton.setEnabled(false);
            loginButton.setEnabled(false);
        }

        tutorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getSelectedItem() == null) {
                    Log.d(TAG, "A null thing is selected.");
                    deleteButton.setEnabled(false);
                    loginButton.setEnabled(false);
                } else {
                    deleteButton.setEnabled(true);
                    loginButton.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewTutor();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTutor((Tutor)tutorSpinner.getSelectedItem());
            }
        });

        //might use EditText + password

        //get arraylist of tutors from db

        //test tutor
//        Person testperson = new Person("Gavin", "Chiem", 5062206);
//        Tutor test = new Tutor(5,testperson);
//        tutorList.add(test);
        refreshTutorSpinner();
    }

    public void login() {
        Tutor tutor = (Tutor) tutorSpinner.getSelectedItem();

        Log.d(TAG, "Login button pressed. Logging in with tutor: " + tutor.getTutorID());

        //go to TutorialListActivity with the tutorID
        Intent intent = new Intent(this, TutorialListActivity.class);
        intent.putExtra("tutor", tutor);
        startActivity(intent);
    }

    public void createNewTutor() {
        NewTutorDialogFragment ntdf = NewTutorDialogFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        ntdf.show(fragmentManager, "dialog");
    }

    public void deleteTutor(final Tutor tutor) {
        final TutorQueries tutorQueries = new TutorQueries(this);
        final Context context = this;

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(R.string.deleteTutorMsg)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tutorQueries.deleteTutor(tutor);
                        recreate();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }


    public void refreshTutorSpinner() {
        TutorQueries tutorQueries = new TutorQueries(this);
        ArrayList<Tutor> tutorList = tutorQueries.getTutorList();
        ArrayAdapter<Tutor> tutorSpinnerAdapter = new ArrayAdapter<Tutor>(this, R.layout.coolspinner, tutorList);
        tutorSpinner.setAdapter(tutorSpinnerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTutorSpinner();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onFragmentInteraction(String name) {

    }

    @Override
    public void onDialogPositiveClick(DialogFragment fragment) {
        refreshTutorSpinner();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment fragment) {
        Log.d(TAG, "Cancelled add tutor.");
    }

    @Override
    public void onLoad(String string) {
        Log.d(TAG, "Loading with glide " + string);
        Glide.with(this).load(string).asBitmap().into(background);
    }
}
