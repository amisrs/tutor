package com.amisrs.gavin.tutorhelp.view;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.controller.BluetoothListAdapter;
import com.amisrs.gavin.tutorhelp.controller.DividerItemDecoration;
import com.amisrs.gavin.tutorhelp.controller.OnDeleteListener;
import com.amisrs.gavin.tutorhelp.controller.OnTutorialUpdateListener;
import com.amisrs.gavin.tutorhelp.db.TutorialQueries;
import com.amisrs.gavin.tutorhelp.controller.TutorialListAdapter;
import com.amisrs.gavin.tutorhelp.model.Tutor;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.view.NavDrawer.DrawerActivity;


import java.util.ArrayList;
import java.util.Set;


public class BluetoothActivity extends Activity {

    private static final String TAG = "BluetoothActivity";
    RecyclerView recycler;
    LinearLayoutManager layoutManager;
    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        layoutManager = new LinearLayoutManager(this);

        recycler = (RecyclerView) findViewById(R.id.rv_bluetooth);

        recycler.setLayoutManager(layoutManager);
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        reloadRecycler();

    }

    public void reloadRecycler() {

        //get an arraylist of blutooth

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        Log.d(TAG, bondedDevices.toString());
        for (BluetoothDevice bd : bondedDevices
                ) {
            Log.d(TAG,bd.getName() + ": " + bd.toString());

        }
        BluetoothListAdapter adapter = new BluetoothListAdapter(this );
        //TutorialQueries tutorialQueries = new TutorialQueries(this);

        adapter.giveList(bondedDevices);
        recycler.addItemDecoration(new DividerItemDecoration(this));
        recycler.setAdapter(adapter);

    }





    @Override
    protected void onResume() {
        super.onResume();
        reloadRecycler();
    }

}
