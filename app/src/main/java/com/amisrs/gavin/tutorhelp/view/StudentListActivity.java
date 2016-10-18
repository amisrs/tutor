package com.amisrs.gavin.tutorhelp.view;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.controller.StudentListAdapter;
import com.amisrs.gavin.tutorhelp.db.StudentQueries;
import com.amisrs.gavin.tutorhelp.model.Student;

import java.util.ArrayList;

public class StudentListActivity extends AppCompatActivity {
    private static final String TAG = "StudentListActivity";
    RecyclerView recycler;
    LinearLayoutManager layoutManager;
    FloatingActionButton fabAddStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        layoutManager = new LinearLayoutManager(this);

        recycler = (RecyclerView) findViewById(R.id.rv_students);
        fabAddStudent = (FloatingActionButton) findViewById(R.id.fab_add_student);
        fabAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStudent();
            }
        });

        recycler.setLayoutManager(layoutManager);


    }

    public void reloadRecycler() {
        //get an arraylist of tutorials for this tutor and create adapter

        StudentQueries studentQueries = new StudentQueries(this);
        ArrayList<Student> studentArrayList = studentQueries.getStudentList();

        StudentListAdapter adapter = new StudentListAdapter();
        adapter.giveList(studentArrayList);
        recycler.setAdapter(adapter);

    }

    public void addStudent() {
        Intent intent = new Intent(this, NewStudentActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadRecycler();
    }
}
