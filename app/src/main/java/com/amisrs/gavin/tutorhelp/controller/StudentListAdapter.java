package com.amisrs.gavin.tutorhelp.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.model.Student;

import java.util.ArrayList;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentViewHolder> {
    private static final String TAG = "StudentListAdapter";
    ArrayList<Student> students;
    OnItemClickListener studentListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        studentListener = listener;
    }

    public void giveList(ArrayList<Student> list) {
        students = list;
    }

    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position) {
        Student currentStudent = students.get(position);
        holder.bindStudent(currentStudent);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_student_listitem, parent, false);
        return new StudentViewHolder(inflatedView);
    }
    class StudentViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView fname;
        private TextView lname;
        private TextView zid;


        public StudentViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            fname = (TextView)itemView.findViewById(R.id.student_fname);
            lname = (TextView)itemView.findViewById(R.id.student_lname);
            zid = (TextView)itemView.findViewById(R.id.student_zid);

        }

        public void bindStudent(final Student student) {
            fname.setText(student.getPerson().getFirstName());
            lname.setText(student.getPerson().getLastName());
            zid.setText(String.valueOf(student.getPerson().getzID()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Student clicked");
                    studentListener.onStudentClick(view, student);
                }
            });
        }

    }
}
