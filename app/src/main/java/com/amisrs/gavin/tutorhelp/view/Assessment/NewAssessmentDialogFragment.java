package com.amisrs.gavin.tutorhelp.view.Assessment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.AssessmentQueries;
import com.amisrs.gavin.tutorhelp.db.PersonQueries;
import com.amisrs.gavin.tutorhelp.db.StudentQueries;
import com.amisrs.gavin.tutorhelp.model.Assessment;
import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.other.DbBitmapUtility;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewAssessmentDialogFragment.OnNewAssessmentDialogFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewAssessmentDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewAssessmentDialogFragment extends DialogFragment {
    private static final String TAG = "NewAssDialogFragment";
    private static final String ARG_TERM = "term";

    private String termParam;
    EditText weightPicker;
    TextInputEditText markText;

    private OnNewAssessmentDialogFragmentInteractionListener mListener;
    private NewAssessmentDialogFragmentListener dialogListener;

    public interface NewAssessmentDialogFragmentListener {
        public void onDialogPositiveClick(DialogFragment fragment);
        public void onDialogNegativeClick(DialogFragment fragment);
    }

    public NewAssessmentDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param term Parameter 1.
     * @return A new instance of fragment NewStudentDialogFragment.
     */
    public static NewAssessmentDialogFragment newInstance(String term) {
        NewAssessmentDialogFragment fragment = new NewAssessmentDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TERM, term);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            termParam = getArguments().getString(ARG_TERM);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_student_dialog, container, false);
        //loadCamera(getDialog());

        return view;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_new_assessment_dialog, null);
        builder.setView(view);

        builder
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("aa", "create");
                        Log.d(TAG, "Positive press");
                        positivePress();
                        dialogListener.onDialogPositiveClick(NewAssessmentDialogFragment.this);

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("aa", "cancel");
                        dialogListener.onDialogNegativeClick(NewAssessmentDialogFragment.this);
                        dialogInterface.cancel();
                    }
                });


        final AlertDialog dialog = builder.create();
        markText = (TextInputEditText) view.findViewById(R.id.et_maxmark);
        weightPicker = (EditText)view.findViewById(R.id.et_weighting);
        weightPicker.setOnKeyListener(new TextView.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(weightPicker.getText().toString().equals("") || Integer.parseInt(weightPicker.getText().toString()) > 100) {
                    weightPicker.setError(getString(R.string.weightingError));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
                return false;
            }
        });

        markText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(markText.getText().toString().equals("") || Integer.parseInt(markText.getText().toString()) < 0) {
                    markText.setError(getString(R.string.greaterThanZero));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
                return false;
            }
        });


        return dialog;
    }




    public void positivePress() {
        Dialog dialog = getDialog();
        TextInputEditText name = (TextInputEditText) dialog.findViewById(R.id.et_name);
        TextInputEditText desc = (TextInputEditText) dialog.findViewById(R.id.et_desc);
        TextInputEditText weight = (TextInputEditText) dialog.findViewById(R.id.et_weighting);
        TextInputEditText mark = (TextInputEditText) dialog.findViewById(R.id.et_maxmark);

        Log.d(TAG, "creating assessment for term: " + termParam);
        AssessmentQueries assessmentQueries = new AssessmentQueries(getContext());
        Assessment newAssessment = new Assessment(name.getText().toString(),
                desc.getText().toString(),
                termParam,
                Double.parseDouble(weight.getText().toString()),
                Integer.parseInt(mark.getText().toString()));
        assessmentQueries.addAssessment(newAssessment);
        //loadCamera(dialog);

        //TODO: improve validation
    }

    public void onButtonPressed(String name) {
        if (mListener != null) {
            mListener.onNewAssessmentDialogFragmentInteraction(name);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewAssessmentDialogFragmentInteractionListener) {
            mListener = (OnNewAssessmentDialogFragmentInteractionListener) context;
            dialogListener = (NewAssessmentDialogFragmentListener) context;
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnNewAssessmentDialogFragmentInteractionListener {
        void onNewAssessmentDialogFragmentInteraction(String name);
    }
}