package com.amisrs.gavin.tutorhelp.view;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.controller.MarkListAdapter;
import com.amisrs.gavin.tutorhelp.controller.OnDeleteListener;
import com.amisrs.gavin.tutorhelp.controller.OnMarkUpdateListener;
import com.amisrs.gavin.tutorhelp.controller.TutorialListAdapter;
import com.amisrs.gavin.tutorhelp.db.PersonQueries;
import com.amisrs.gavin.tutorhelp.db.StudentQueries;
import com.amisrs.gavin.tutorhelp.db.TutorialQueries;
import com.amisrs.gavin.tutorhelp.model.Enrolment;
import com.amisrs.gavin.tutorhelp.model.Mark;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.other.ProfileCircle;
import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudentDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentDetailsFragment extends Fragment implements OnMarkUpdateListener, TextToSpeech.OnInitListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "StudentDetailsFragment";
    private static final String ARG_STUDENT = "student";
    private static final String ARG_TUTORIAL = "tutorial";

    // TODO: Rename and change types of parameters
    private Student studentParam;
    private Tutorial tutorialParam;
    private boolean isEdit;

    private OnFragmentInteractionListener mListener;
    private OnDeleteListener onDeleteListener;

    //http://www.androidhive.info/2012/01/android-text-to-speech-tutorial/
    private TextToSpeech textToSpeech;

    public StudentDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param student  Parameter 1.
     * @param tutorial Parameter 2.
     * @return A new instance of fragment StudentDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentDetailsFragment newInstance(Student student, Tutorial tutorial) {
        StudentDetailsFragment fragment = new StudentDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_STUDENT, student);
        args.putParcelable(ARG_TUTORIAL, tutorial);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            studentParam = getArguments().getParcelable(ARG_STUDENT);
            tutorialParam = getArguments().getParcelable(ARG_TUTORIAL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*View view = inflater.inflate(R.layout.fragment_student_details, container, false);
        TextView nameTextView = (TextView) view.findViewById(R.id.tv_name);
        TextView zidTextView = (TextView) view.findViewById(R.id.tv_zid);*/

        textToSpeech = new TextToSpeech(getContext(), this);


        isEdit = false;
        final View view = inflater.inflate(R.layout.fragment_student_details, container, false);
        final EditText fnameTextView = (EditText) view.findViewById(R.id.tv_fname);
        final EditText lnameTextView = (EditText) view.findViewById(R.id.tv_lname);
        final EditText zidTextView = (EditText) view.findViewById(R.id.tv_zid);
        ImageView profile = (ImageView) view.findViewById(R.id.iv_pic);
        Glide.with(getContext())
                .load(studentParam.getPerson().getProfilePath())
                .asBitmap()
                .placeholder(R.drawable.ic_default_profile_pic)
                .transform(new ProfileCircle(getContext()))
                .into(profile);

        final EditText gradeText = (EditText) view.findViewById(R.id.et_grade);
        final ImageButton editButton = (ImageButton) view.findViewById(R.id.iv_edit);
        final ImageButton saveButton = (ImageButton) view.findViewById(R.id.iv_save);
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.ib_delete);

        fnameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakName(fnameTextView.getText().toString());
            }
        });

        lnameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakName(lnameTextView.getText().toString());
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setMessage(R.string.deleteStudentMsg)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StudentQueries studentQueries = new StudentQueries(getContext());
                                studentQueries.deleteStudent(studentParam);
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

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_tutorials);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        RecyclerView markView = (RecyclerView) view.findViewById(R.id.rv_assessments);
        LinearLayoutManager markLayoutManager = new LinearLayoutManager(getContext());

        final StudentQueries studentQueries = new StudentQueries(getContext());
        final Enrolment enrolment = studentQueries.getEnrolmentForStudentAndTutorial(studentParam, tutorialParam);
        Log.d(TAG, "The enrolment grade is: " + enrolment.getGrade());

        TutorialQueries tutorialQueries = new TutorialQueries(getContext());
        ArrayList<Tutorial> tutorialArrayList = tutorialQueries.getTutorialsForStudent(studentParam);
        //get tutorials for student
        TutorialListAdapter adapter = new TutorialListAdapter(getContext());
        adapter.giveList(tutorialArrayList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        ArrayList<Mark> marks = studentQueries.getMarksForStudent(studentParam);
        MarkListAdapter markListAdapter = new MarkListAdapter(getContext());
        markListAdapter.setOnMarkUpdateListener(this);
        markListAdapter.giveList(marks);
        Log.d(TAG, "Mark list has: " + marks.size());
        markView.setLayoutManager(markLayoutManager);
        markView.setAdapter(markListAdapter);

        fnameTextView.setText(studentParam.getPerson().getFirstName());
        lnameTextView.setText(studentParam.getPerson().getLastName());
        zidTextView.setText(String.valueOf(studentParam.getPerson().getzID()));
        gradeText.setText(String.valueOf(enrolment.getGrade()));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update details
                PersonQueries personQueries = new PersonQueries(getContext());
                personQueries.updatePerson(studentParam.getPersonID(),
                        fnameTextView.getText().toString(),
                        lnameTextView.getText().toString(),
                        Integer.parseInt(zidTextView.getText().toString()));

                //update grade

                fnameTextView.setInputType(InputType.TYPE_NULL);
                lnameTextView.setInputType(InputType.TYPE_NULL);
                zidTextView.setInputType(InputType.TYPE_NULL);

                editButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_mode_edit_black_36dp));
                saveButton.setVisibility(View.GONE);
                isEdit = false;
                onButtonPressed("save");
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEdit) {
                    fnameTextView.setInputType(InputType.TYPE_CLASS_TEXT);
                    lnameTextView.setInputType(InputType.TYPE_CLASS_TEXT);
                    zidTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_clear_black_36dp));
                    saveButton.setVisibility(View.VISIBLE);

                    isEdit = true;
                } else {
                    fnameTextView.setText(studentParam.getPerson().getFirstName());
                    lnameTextView.setText(studentParam.getPerson().getLastName());
                    zidTextView.setText(String.valueOf(studentParam.getPerson().getzID()));
                    gradeText.setText(String.valueOf(enrolment.getGrade()));
                    fnameTextView.setInputType(InputType.TYPE_NULL);
                    lnameTextView.setInputType(InputType.TYPE_NULL);
                    zidTextView.setInputType(InputType.TYPE_NULL);

                    editButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_mode_edit_black_36dp));
                    saveButton.setVisibility(View.GONE);
                    isEdit = false;
                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI eventx
    public void onButtonPressed(String string) {
        if (mListener != null) {
            mListener.onFragmentInteraction(string);
        }
    }

    public void refreshGrade(Mark mark) {
        Log.d(TAG, "Refreshing grade");
        final EditText gradeText = (EditText) getView().findViewById(R.id.et_grade);
        final StudentQueries studentQueries = new StudentQueries(getContext());
        final Enrolment enrolment = studentQueries.getEnrolmentForStudentAndTutorial(studentParam, tutorialParam);

        Student student = studentQueries.getStudentById(mark.getStudentID());

        enrolment.setGrade(studentQueries.recalculateGradeForStudentAndTerm(student, tutorialParam.getTerm()));
        gradeText.setText(String.valueOf(enrolment.getGrade()));
    }

    @Override
    public void onMarkUpdate(Mark mark) {
        refreshGrade(mark);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        if (context instanceof  OnDeleteListener) {
            onDeleteListener = (OnDeleteListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDeleteListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        // TODO: Update argument type and name
        void onFragmentInteraction(String string);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }

    public void speakName(String speak) {
        Log.d(TAG, "Speaking name: " + speak);
        textToSpeech.speak(speak, TextToSpeech.QUEUE_FLUSH, null);
    }
}
