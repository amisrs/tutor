package com.amisrs.gavin.tutorhelp.view.Assessment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.controller.TutorialListAdapter;
import com.amisrs.gavin.tutorhelp.db.AssessmentQueries;
import com.amisrs.gavin.tutorhelp.db.PersonQueries;
import com.amisrs.gavin.tutorhelp.db.StudentQueries;
import com.amisrs.gavin.tutorhelp.db.TutorialQueries;
import com.amisrs.gavin.tutorhelp.model.Assessment;
import com.amisrs.gavin.tutorhelp.model.Enrolment;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutorial;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AssessmentDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssessmentDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssessmentDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ASSESSMENT = "assessment";

    // TODO: Rename and change types of parameters
    private Assessment assessmentParam;
    private boolean isEdit;

    private OnFragmentInteractionListener mListener;

    public AssessmentDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param assessment Parameter 1.
     * @return A new instance of fragment StudentDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssessmentDetailsFragment newInstance(Assessment assessment) {
        AssessmentDetailsFragment fragment = new AssessmentDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ASSESSMENT, assessment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            assessmentParam = getArguments().getParcelable(ARG_ASSESSMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        isEdit = false;
        final View view = inflater.inflate(R.layout.fragment_assessment_details, container, false);
        final EditText nameText = (EditText) view.findViewById(R.id.tv_name);
        final TextInputEditText descText = (TextInputEditText) view.findViewById(R.id.tv_desc);
        final TextInputEditText weightText = (TextInputEditText) view.findViewById(R.id.et_weighting);
        final TextInputEditText markText = (TextInputEditText) view.findViewById(R.id.et_maxmark);


        final ImageButton editButton = (ImageButton) view.findViewById(R.id.iv_edit);
        final ImageButton saveButton = (ImageButton) view.findViewById(R.id.iv_save);
        ImageButton deleteButton = (ImageButton) view.findViewById(R.id.ib_delete);

        nameText.setText(assessmentParam.getName());
        descText.setText(assessmentParam.getDescription());
        weightText.setText(String.valueOf((int)assessmentParam.getWeighting()));
        markText.setText(String.valueOf((assessmentParam.getMaxMark())));

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setMessage(R.string.deleteTutorialMsg)
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



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update details
                AssessmentQueries assessmentQueries = new AssessmentQueries(getContext());
                assessmentQueries.updateAssessment(new Assessment(assessmentParam.getAssessmentId(),
                        nameText.getText().toString(),
                        descText.getText().toString(),
                        assessmentParam.getTerm(),
                        Double.parseDouble(weightText.getText().toString()),
                        Integer.parseInt(markText.getText().toString())));

                editButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_mode_edit_black_36dp));
                saveButton.setVisibility(View.GONE);

                nameText.setInputType(InputType.TYPE_NULL);
                descText.setInputType(InputType.TYPE_NULL);
                weightText.setInputType(InputType.TYPE_NULL);
                markText.setInputType(InputType.TYPE_NULL);

                isEdit = false;
                onButtonPressed("save");
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEdit) {
                    editButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_clear_black_36dp));
                    saveButton.setVisibility(View.VISIBLE);

                    nameText.setInputType(InputType.TYPE_CLASS_TEXT);
                    descText.setInputType(InputType.TYPE_CLASS_TEXT);
                    weightText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    markText.setInputType(InputType.TYPE_CLASS_NUMBER);

                    isEdit = true;
                } else {

                    nameText.setText(assessmentParam.getName());
                    descText.setText(assessmentParam.getDescription());
                    weightText.setText(String.valueOf((int)assessmentParam.getWeighting()));
                    markText.setText(String.valueOf(assessmentParam.getMaxMark()));

                    nameText.setInputType(InputType.TYPE_NULL);
                    descText.setInputType(InputType.TYPE_NULL);
                    weightText.setInputType(InputType.TYPE_NULL);
                    markText.setInputType(InputType.TYPE_NULL);

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



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
}
