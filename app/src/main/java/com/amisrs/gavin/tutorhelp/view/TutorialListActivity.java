package com.amisrs.gavin.tutorhelp.view;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.controller.OnDeleteListener;
import com.amisrs.gavin.tutorhelp.controller.OnTutorialUpdateListener;
import com.amisrs.gavin.tutorhelp.db.TutorialQueries;
import com.amisrs.gavin.tutorhelp.controller.TutorialListAdapter;
import com.amisrs.gavin.tutorhelp.model.Tutor;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.view.NavDrawer.DrawerActivity;


import java.util.ArrayList;


public class TutorialListActivity extends DrawerActivity implements NewTutorialDialogFragment.OnFragmentInteractionListener,
<<<<<<< HEAD
        NewTutorialDialogFragment.NewTutorialDialogFragmentListener, OnDeleteListener {
=======
        NewTutorialDialogFragment.NewTutorialDialogFragmentListener, OnDeleteListener, OnTutorialUpdateListener {
>>>>>>> a7ef165f92399f95e3f49289bb2e13249a081b90

    //TODO: update student number, better layout
    private static final String TAG = "TutorialListActivity";
    RecyclerView recycler;
    LinearLayoutManager layoutManager;
    Tutor theTutor;
    FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_list);
        layoutManager = new LinearLayoutManager(this);

        recycler = (RecyclerView) findViewById(R.id.rv_tutorials);
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        TextView helloName = (TextView) findViewById(R.id.tv_tutorName);

        recycler.setLayoutManager(layoutManager);

        theTutor = getIntent().getParcelableExtra("tutor");

        if (theTutor == null) {
            Log.e(TAG, "No tutor was received from the Intent.");
            //stop
            finish();
        }
        //loadNavHeader(theTutor);
        Log.d(TAG, "Showing tutorials for: " + theTutor);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTutorial();
            }
        });


        helloName.setText(" " + theTutor.getPerson().getFirstName() + "!");


    }

    public void reloadRecycler() {
        //get an arraylist of tutorials for this tutor and create adapter

        TutorialQueries tutorialQueries = new TutorialQueries(this);
        ArrayList<Tutorial> tutorialArrayList = tutorialQueries.getTutorialListForTutor(theTutor);

<<<<<<< HEAD
        TutorialListAdapter adapter = new TutorialListAdapter(this, theTutor);
=======
        TutorialListAdapter adapter = new TutorialListAdapter(this);
        adapter.setOnTutorialUpdateListener(this);
>>>>>>> a7ef165f92399f95e3f49289bb2e13249a081b90
        adapter.giveList(tutorialArrayList);
        adapter.setOnDeleteListener(this);
        recycler.setAdapter(adapter);

    }

    public void addTutorial() {
       /* Intent intent = new Intent(this,NewTutorialActivity.class);
        intent.putExtra("tutor", theTutor);
        startActivity(intent);*/

        NewTutorialDialogFragment newTutorialDialog = NewTutorialDialogFragment.newInstance(theTutor);
        FragmentManager fragmentManager = getSupportFragmentManager();
        newTutorialDialog.show(fragmentManager, "dialog");
    }


    @Override
    public void onFragmentInteraction(String name) {

    }



    @Override
    public void onDialogPositiveClick(DialogFragment fragment) {
        reloadRecycler();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment fragment) {
        Log.d(TAG, "Cancelled add tutorial.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadRecycler();
    }

    @Override
    public void onDelete() {
        reloadRecycler();
    }

    @Override
    public void onTutorialUpdate(Tutorial tutorial) {
        reloadRecycler();
    }
}
