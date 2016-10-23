package com.amisrs.gavin.tutorhelp.controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.TutorialQueries;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.view.MenuActivity;
import com.amisrs.gavin.tutorhelp.view.StudentDetailsFragment;

import java.util.ArrayList;

/**
 * Created by Gavin on 15/10/2016.
 */
public class TutorialListAdapter extends RecyclerView.Adapter<TutorialListAdapter.TutorialViewHolder> {
    private static final String TAG = "TutorialListAdapter";
    ArrayList<Tutorial> tutorials;
    Context context;
    OnDeleteListener onDeleteListener;


    public TutorialListAdapter(Context context) {
        this.context = context;
    }

    public void giveList(ArrayList<Tutorial> list) {
        tutorials = list;
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    @Override
    public void onBindViewHolder(TutorialViewHolder holder, int position) {
        Tutorial currentTutorial = tutorials.get(position);
        holder.bindTutorial(currentTutorial);
    }

    @Override
    public int getItemCount() {
        return tutorials.size();
    }

    @Override
    public TutorialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tutorial_listitem, parent, false);
        return new TutorialViewHolder(inflatedView);
    }

    class TutorialViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relativeLayout;
        private TextView name;
        private TextView time;
        private TextView place;
        private TextView population;
        private TextView term;
        private ImageButton deleteButton;

        public TutorialViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.tv_tutorialName);
            time = (TextView)itemView.findViewById(R.id.tv_tutorialTime);
            place = (TextView)itemView.findViewById(R.id.tv_tutorialLocation);
            population = (TextView)itemView.findViewById(R.id.tv_pop);
            term = (TextView)itemView.findViewById(R.id.tv_term);
            deleteButton = (ImageButton)itemView.findViewById(R.id.ib_delete);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.rl_item);
            Log.d(TAG, "Context is " + context.getClass().getName());
        }

        public void bindTutorial(final Tutorial tutorial) {

            name.setText(tutorial.getName());
            time.setText(tutorial.getTimeSlot());
            place.setText(tutorial.getLocation());
            term.setText(tutorial.getTerm());
            System.out.println("tutorial term: " + tutorial.getTerm());
            //get count of students in this tutorial; from enrolment table
            final TutorialQueries tutorialQueries = new TutorialQueries(context);
            int size = tutorialQueries.getStudentsForTutorial(tutorial).size();
            population.setText(String.valueOf(size));

            if(context.getClass().getName().equals(StudentDetailsFragment.class.getName())) {
                deleteButton.setVisibility(View.GONE);
            } else {
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog alertDialog = new AlertDialog.Builder(context)
                                .setMessage(R.string.deleteTutorialMsg)
                                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                tutorialQueries.deleteTutorial(tutorial);
                                                onDeleteListener.onDelete();
                                            }
                                        })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();

                    }
                });
            }

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Activity activity = (Activity)view.getContext();
                    Intent intent = new Intent(activity, MenuActivity.class);
                    Log.d(TAG, "Putting intent extra tutorial: " + tutorial.getName());
                    intent.putExtra("tutorial", tutorial);
                    activity.startActivity(intent);
                }
            });
        }

    }
}
