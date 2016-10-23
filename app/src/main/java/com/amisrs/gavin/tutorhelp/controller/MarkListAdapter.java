package com.amisrs.gavin.tutorhelp.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.AssessmentQueries;
import com.amisrs.gavin.tutorhelp.model.Assessment;
import com.amisrs.gavin.tutorhelp.model.Mark;

import java.util.ArrayList;

/**
 * Created by Gavin on 23/10/2016.
 */

public class MarkListAdapter extends RecyclerView.Adapter<MarkListAdapter.MarkViewHolder> {
    private static final String TAG = "MarkListAdapter";
    ArrayList<Mark> marks;
    Context context;

    public MarkListAdapter(Context context) {
        super();
        this.context = context;
    }

    public void giveList(ArrayList<Mark> marks) {
        this.marks = marks;
    }

    @Override
    public int getItemCount() {
        return marks.size();
    }

    @Override
    public void onBindViewHolder(MarkViewHolder holder, int position) {
        Mark currentMark = marks.get(position);
        holder.bindMark(currentMark);
    }

    @Override
    public MarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mark_listitem, parent, false);

        return new MarkViewHolder(inflatedView);
    }

    class MarkViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView textView;
        private Assessment assessment;

        public MarkViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.tv_name);
        }

        public void bindMark(Mark mark) {
            AssessmentQueries assessmentQueries = new AssessmentQueries(context);
            assessment = assessmentQueries.getAssessmentById(mark.getAssessmentID());

            textView.setText(assessment.getName() + " " + String.valueOf(mark.getMark()));
        }
    }
}
