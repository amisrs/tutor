package com.amisrs.gavin.tutorhelp.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.TutorialQueries;
import com.amisrs.gavin.tutorhelp.db.WeekQueries;
import com.amisrs.gavin.tutorhelp.model.Tutor;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.model.Week;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewTutorialDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewTutorialDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewTutorialDialogFragment extends DialogFragment {
    private static final String TAG = "NewTutorialDialogFrag";
    private static final String ARG_TUTOR = "tutor";
    private Tutor tutorParam;

    Button createButton;
    TextInputEditText name;
    TextInputEditText timeSlot;
    TextInputEditText location;
    Spinner semSp;
    Spinner yearSp;


    private OnFragmentInteractionListener mListener;
    private NewTutorialDialogFragmentListener dialogListener;

    public interface NewTutorialDialogFragmentListener {
        public void onDialogPositiveClick(DialogFragment fragment);

        public void onDialogNegativeClick(DialogFragment fragment);
    }
    public NewTutorialDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tutor Parameter 1.
     * @return A new instance of fragment NewTutorialDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewTutorialDialogFragment newInstance(Tutor tutor) {
        NewTutorialDialogFragment fragment = new NewTutorialDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TUTOR, tutor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tutorParam = getArguments().getParcelable(ARG_TUTOR);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_tutorial_dialog, container, false);


        return inflater.inflate(R.layout.fragment_new_tutorial_dialog, container, false);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_new_tutorial, null);
        builder.setView(view);
        semSp = (Spinner) view.findViewById(R.id.sp_sem);
        yearSp = (Spinner) view.findViewById(R.id.sp_year);

        ArrayList<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=currentYear; i<currentYear+5; i++) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, years);
        yearSp.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        yearSp.setAdapter(yearAdapter);

        ArrayList<String> sems = new ArrayList<>();
        sems.add(getString(R.string.S1));
        sems.add(getString(R.string.S2));
        sems.add(getString(R.string.ST));
        ArrayAdapter<String> semAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sems);
        semSp.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        semSp.setAdapter(semAdapter);


/*


        semSp = (Spinner) view.findViewById(R.id.sp_sem);
        yearSp = (Spinner) view.findViewById(R.id.sp_year);

        ArrayList<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i=currentYear; i<currentYear+5; i++) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, years);
        yearSp.setAdapter(yearAdapter);

        ArrayList<String> sems = new ArrayList<>();
        sems.add(getString(R.string.S1));
        sems.add(getString(R.string.S2));
        sems.add(getString(R.string.ST));
        ArrayAdapter<String> semAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sems);
        semSp.setAdapter(semAdapter);
*/

     /*   createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTutorial();
            }
        });*/

        builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("aa", "create");
                positivePress();
                dialogListener.onDialogPositiveClick(NewTutorialDialogFragment.this);
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("aa", "cancel");
                        dialogListener.onDialogNegativeClick(NewTutorialDialogFragment.this);
                        dialogInterface.cancel();
                    }
                });

        return builder.create();
    }

    public void positivePress() {
        Dialog dialog = getDialog();

        name = (TextInputEditText) dialog.findViewById(R.id.et_name);
        timeSlot = (TextInputEditText) dialog.findViewById(R.id.et_timeSlot);
        location = (TextInputEditText) dialog.findViewById(R.id.et_location);


        TutorialQueries tutorialQueries = new TutorialQueries(getContext());
        String termString = semSp.getSelectedItem().toString() + " " + yearSp.getSelectedItem().toString();

        Tutorial newTutorial = new Tutorial(tutorParam.getTutorID(),
                name.getText().toString(),
                timeSlot.getText().toString(),
                location.getText().toString(),
                termString);
        int tutorialID = (int)tutorialQueries.addTutorial(newTutorial);
        WeekQueries weekQueries = new WeekQueries(getContext());
        for(int i=1; i<Tutorial.MAX_WEEKS+1; i++) {
            Week newWeek = new Week(tutorialID, i, "");
            weekQueries.addWeek(newWeek);
            Log.d(TAG, "Added week " + i + " of 13 for tutorial " + newTutorial.getName());

            dialogListener.onDialogPositiveClick(this);
        }


    }




    public void onButtonPressed(String name) {
        if (mListener != null) {
            mListener.onFragmentInteraction(name);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            dialogListener = (NewTutorialDialogFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        dialogListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String name);
    }
}
