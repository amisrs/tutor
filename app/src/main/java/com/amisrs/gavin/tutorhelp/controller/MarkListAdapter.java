package com.amisrs.gavin.tutorhelp.controller;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.AssessmentQueries;
import com.amisrs.gavin.tutorhelp.db.PersonQueries;
import com.amisrs.gavin.tutorhelp.db.StudentQueries;
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
        private EditText markText;
        private TextView totalText;
        private ImageButton editButton;
        private ImageButton saveButton;
        boolean isEdit = false;
        boolean legit = false;

        private Assessment assessment;


        public MarkViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.tv_name);
            markText = (EditText)itemView.findViewById(R.id.et_mark);
            totalText = (TextView)itemView.findViewById(R.id.tv_total);
            editButton = (ImageButton)itemView.findViewById(R.id.iv_edit);
            saveButton = (ImageButton)itemView.findViewById(R.id.iv_save);

        }

        public void bindMark(final Mark mark) {
            final AssessmentQueries assessmentQueries = new AssessmentQueries(context);
            assessment = assessmentQueries.getAssessmentById(mark.getAssessmentID());

            textView.setText(assessment.getName());
            markText.setText(String.valueOf(mark.getMark()));
            totalText.setText("/" + assessment.getMaxMark());

            markText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(markText.getText().toString().equals("") || Integer.parseInt(markText.getText().toString()) > assessment.getMaxMark()) {
                        //cant have mark more than max
                        legit = false;
                        markText.setError(context.getString(R.string.invalidValue));
                    } else {
                        legit = true;
                    }
                    return false;
                }
            });

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(legit) {
                        //update details
                        mark.setMark(Integer.parseInt(markText.getText().toString()));
                        markText.setInputType(InputType.TYPE_NULL);
                        markText.setError(null);

                        StudentQueries studentQueries = new StudentQueries(context);
                        studentQueries.updateMark(mark);

                        editButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_mode_edit_black_36dp));
                        saveButton.setVisibility(View.GONE);
                        isEdit = false;
                        //update grade
                    }
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isEdit) {
                        markText.setInputType(InputType.TYPE_CLASS_TEXT);
                        editButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_clear_black_36dp));
                        saveButton.setVisibility(View.VISIBLE);

                        isEdit = true;
                    } else {
                        markText.setText(String.valueOf(mark.getMark()));
                        markText.setInputType(InputType.TYPE_NULL);
                        markText.setError(null);

                        editButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_mode_edit_black_36dp));
                        saveButton.setVisibility(View.GONE);
                        isEdit = false;
                    }
                }
            });

        }
    }
}
