package com.amisrs.gavin.tutorhelp.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.model.Tutorial;

import java.util.ArrayList;

/**
 * Created by Gavin on 15/10/2016.
 */
public class TutorialListAdapter extends RecyclerView.Adapter<TutorialListAdapter.TutorialViewHolder> {
    ArrayList<Tutorial> tutorials;

    public void giveList(ArrayList<Tutorial> list) {
        tutorials = list;
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

        public TutorialViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.tv_tutorialName);
            time = (TextView)itemView.findViewById(R.id.tv_tutorialTime);
            place = (TextView)itemView.findViewById(R.id.tv_tutorialLocation);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.rl_item);

        }

        public void bindTutorial(Tutorial tutorial) {
            name.setText(tutorial.getName());
            time.setText(tutorial.getTimeSlot());
            place.setText(tutorial.getLocation());
        }
    }
}
