package com.amisrs.gavin.tutorhelp.controller;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.PersonQueries;
import com.amisrs.gavin.tutorhelp.db.TutorialQueries;
import com.amisrs.gavin.tutorhelp.model.Tutor;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.view.BaseActivity;

import com.amisrs.gavin.tutorhelp.view.StudentDetailsFragment;
import com.amisrs.gavin.tutorhelp.view.StudentsActivity;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Gavin on 15/10/2016.
 */
public class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothListAdapter.BluetoothViewHolder> {
    private static final String TAG = "BluetoothListAdapter";
    ArrayList<BluetoothDevice> devices;
    Context context;


    public BluetoothListAdapter(Context context) {
        this.context = context;
    }

    public void giveList(ArrayList<BluetoothDevice> list) {
        devices = list;
    }
    public void giveList(Set<BluetoothDevice> list) {
        ArrayList<BluetoothDevice> newList = new ArrayList<>(list);
        devices  = newList;
        Log.d(TAG, newList.toString());
    }

    @Override
    public BluetoothListAdapter.BluetoothViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bluetooth_listitem, parent, false);
        return new BluetoothViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(BluetoothListAdapter.BluetoothViewHolder holder, int position) {
        BluetoothDevice currentDevice = devices.get(position);
        holder.bindBluetooth(currentDevice);

    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    class BluetoothViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relativeLayout;
        private TextView name;

        public BluetoothViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rl_item);
            //Log.d(TAG, "Context is " + context.getClass().getName());
        }

        public void bindBluetooth(final BluetoothDevice device) {

            name.setText(device.getName());
            Log.d(TAG, "BindBluetooth is binding: " + device.getName());
            //get count of students in this tutorial; from enrolment table
            //final TutorialQueries tutorialQueries = new TutorialQueries(context);
            //int size = tutorialQueries.getStudentsForTutorial(tutorial).size();
//            }


            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //returnIntent.putExtra("selectedDevice", device);

//                    Log.d(TAG, "Putting intent extra tutorial: " + tutorial.getName());
//                    intent.putExtra("tutor", tutor);
//                    intent.putExtra("tutorial", tutorial);
                }
            });
        }

    }
}

