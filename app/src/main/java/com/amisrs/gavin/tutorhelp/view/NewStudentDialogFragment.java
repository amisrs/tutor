package com.amisrs.gavin.tutorhelp.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.PersonQueries;
import com.amisrs.gavin.tutorhelp.db.StudentQueries;
import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutorial;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewStudentDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewStudentDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewStudentDialogFragment extends DialogFragment {
    private static final String TAG = "NewStudentDialogFragmen";
    private static final String ARG_TUTORIAL = "tutorial";

    private Tutorial tutorialParam;

    private OnFragmentInteractionListener mListener;
    private NewStudentDialogFragmentListener dialogListener;

    public interface NewStudentDialogFragmentListener {
        public void onDialogPositiveClick(DialogFragment fragment);
        public void onDialogNegativeClick(DialogFragment fragment);
    }

    public NewStudentDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tutorial Parameter 1.
     * @return A new instance of fragment NewStudentDialogFragment.
     */
    public static NewStudentDialogFragment newInstance(Tutorial tutorial) {
        NewStudentDialogFragment fragment = new NewStudentDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TUTORIAL, tutorial);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tutorialParam = getArguments().getParcelable(ARG_TUTORIAL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_student_dialog, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_new_student, null));
        builder
        .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("aa", "create");
                positivePress();
                dialogListener.onDialogPositiveClick(NewStudentDialogFragment.this);
            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("aa", "cancel");
                dialogListener.onDialogNegativeClick(NewStudentDialogFragment.this);
                dialogInterface.cancel();
            }
        });

        return builder.create();
    }

    public void positivePress() {
        Dialog dialog  = getDialog();
        EditText zid = (EditText)dialog.findViewById(R.id.student_zid);
        EditText fname = (EditText)dialog.findViewById(R.id.student_fname);
        EditText lname = (EditText)dialog.findViewById(R.id.student_lname);

        //TODO: improve validation
        String zidString = zid.getText().toString();
        Pattern zidPattern = Pattern.compile("\\d+");
        Matcher zidMatcher = zidPattern.matcher(zidString);
        if(zidMatcher.find()) {


            String fnameString = fname.getText().toString();
            String lnameString = lname.getText().toString();
            int zidInt = Integer.parseInt(zid.getText().toString());

            PersonQueries personQueries = new PersonQueries(getContext());
            Person addedPerson = personQueries.addPerson(new Person(fnameString, lnameString, zidInt));

            StudentQueries studentQueries = new StudentQueries(getContext());
            studentQueries.addStudent(new Student(addedPerson.getPersonID(), addedPerson), tutorialParam);

            Log.d(TAG, "Added person to database: " + addedPerson.getzID() + " " + addedPerson.getFirstName() + " " + addedPerson.getLastName());
            dialogListener.onDialogPositiveClick(this);

        } else {
            //the zid has non-digits in it
            zid.setError("zID must only contain numbers.");
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
            dialogListener = (NewStudentDialogFragmentListener) context;
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
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String name);
    }
}
