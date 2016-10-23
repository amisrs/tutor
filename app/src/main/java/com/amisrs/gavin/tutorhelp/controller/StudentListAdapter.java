package com.amisrs.gavin.tutorhelp.controller;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.other.ProfileCircle;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StudentViewHolder> {
    private static final String TAG = "StudentListAdapter";
    ArrayList<Student> students;
    OnItemClickListener studentListener;
    Context context;


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
        private TextView studentName;
        private TextView lname;
        private TextView zid;
        private ImageView profile;

        public StudentViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            studentName = (TextView)itemView.findViewById(R.id.student_name);

            zid = (TextView)itemView.findViewById(R.id.student_zid);


        }

        public void bindStudent(final Student student) {
            String name = student.getPerson().getFirstName() + "  " +  student.getPerson().getLastName();
            studentName.setText(name);
           // lname.setText(student.getPerson().getLastName());
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
