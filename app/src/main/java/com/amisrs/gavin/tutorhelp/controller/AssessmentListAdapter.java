package com.amisrs.gavin.tutorhelp.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.model.Assessment;
import com.amisrs.gavin.tutorhelp.model.Student;

import java.util.ArrayList;

public class AssessmentListAdapter extends RecyclerView.Adapter<AssessmentListAdapter.AssessmentViewHolder> {
    private static final String TAG = "StudentListAdapter";
    ArrayList<Assessment> assessments;
    OnAssessmentClickListener assessmentClickListener;
    Context context;


    public void setOnAssessmentClickListener(OnAssessmentClickListener listener) {
        assessmentClickListener = listener;
    }

    public void giveList(ArrayList<Assessment> list) {
        assessments = list;
    }

    @Override
    public void onBindViewHolder(AssessmentViewHolder holder, int position) {
        Assessment currentAssessment = assessments.get(position);
        holder.bindStudent(currentAssessment);
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    @Override
    public AssessmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_assessment_listitem, parent, false);
        return new AssessmentViewHolder(inflatedView);
    }
    class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView name;
        private TextView weighting;

        public AssessmentViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = (TextView)itemView.findViewById(R.id.tv_name);
            weighting = (TextView)itemView.findViewById(R.id.tv_weighting);


        }

        public void bindStudent(final Assessment assessment) {
            //String name = student.getPerson().getFirstName() + "  " +  student.getPerson().getLastName();
            //studentName.setText(name);
            // lname.setText(student.getPerson().getLastName());
            //zid.setText(String.valueOf(student.getPerson().getzID()));
            name.setText(assessment.getName());
            weighting.setText((int)assessment.getWeighting() + "%");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Student clicked");
                    assessmentClickListener.onAssessmentClicked(view, assessment);
                }
            });
        }

    }
}
