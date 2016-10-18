package com.amisrs.gavin.tutorhelp.view;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.model.Week;

public class BaseActivity extends AppCompatActivity implements StudentListFragment.OnFragmentInteractionListener, StudentWeekDetailsFragment.OnFragmentInteractionListener {
    Tutorial tutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        tutorial = getIntent().getParcelableExtra("tutorial");
        Week fakeWeek = new Week(1,1,1,"asdf");
        Person fakePerson = new Person(1, "test", "testlname", 2222);
        Student fakeStudent = new Student(1,1,fakePerson);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.rl_left, StudentListFragment.newInstance(tutorial));
        fragmentTransaction.add(R.id.rl_right, StudentWeekDetailsFragment.newInstance(fakeWeek,fakeStudent,tutorial));
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
