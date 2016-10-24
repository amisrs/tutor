package com.amisrs.gavin.tutorhelp.view.Assessment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.controller.OnDeleteListener;
import com.amisrs.gavin.tutorhelp.controller.TutorialListAdapter;
import com.amisrs.gavin.tutorhelp.db.AssessmentQueries;
import com.amisrs.gavin.tutorhelp.db.PersonQueries;
import com.amisrs.gavin.tutorhelp.db.StudentQueries;
import com.amisrs.gavin.tutorhelp.db.TutorialQueries;
import com.amisrs.gavin.tutorhelp.model.Assessment;
import com.amisrs.gavin.tutorhelp.model.Enrolment;
import com.amisrs.gavin.tutorhelp.model.Mark;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    private static final String TAG = "AssessmentDetailsFrag";
    private static final String ARG_ASSESSMENT = "assessment";

    // TODO: Rename and change types of parameters
    private Assessment assessmentParam;
    private boolean isEdit;

    private OnFragmentInteractionListener mListener;
    private OnDeleteListener onDeleteListener;

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

        TextView meanText = (TextView) view.findViewById(R.id.tv_mean);

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
                        .setMessage(R.string.deleteAssessmentMsg)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AssessmentQueries assessmentQueries = new AssessmentQueries(getContext());
                                assessmentQueries.deleteAssessment(assessmentParam);
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
                saveButton.setVisibility(View.INVISIBLE);

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
                    saveButton.setVisibility(View.INVISIBLE);
                    isEdit = false;
                }
            }
        });

        //calculate
        AssessmentQueries assessmentQueries = new AssessmentQueries(getContext());
        ArrayList<Mark> marks = assessmentQueries.getAllMarksForAssessment(assessmentParam);
        Collections.sort(marks);
        Log.d(TAG, "Mark list length: " + marks.size());
        //sum
        double sum = 0;
        int[][] markCount = new int[2][marks.size()];
        int uniqueMarks = 0;

        for(Mark m : marks) {
            sum += (double)m.getMark();
            boolean exists = false;
            int i = 0;
            for(i=0; i<markCount[0].length && !exists; i++) {
                if(m.getMark() == markCount[0][i]) {
                    exists = true;
                }
            }

            if(exists) {
                //add to count
                Log.d(TAG, "Mark value of " + markCount[0][i-1] + " already exists; incrementing count");
                markCount[1][i-1]++;
                Log.d(TAG, "Count for this mark is now " + markCount[1][i-1]);
            } else {
                Log.d(TAG, "This mark does not exist yet " + m.getMark() + " so add it to list.");
                markCount[0][uniqueMarks] = m.getMark();
                markCount[1][uniqueMarks]++;
                uniqueMarks++;
            }
        }

        Log.d(TAG, "Sum of marks for assessment " + assessmentParam.getName() + " is " + sum);

        double average = sum / (double)marks.size();
        Log.d(TAG, "Average = " + average);
        meanText.setText(String.valueOf(average));

        TableLayout distTable = new TableLayout(getContext());

        TableRow markRow = new TableRow(getContext());
        TextView markRowLab = new TextView(getContext());
        markRowLab.setText(getString(R.string.mark));
        markRowLab.setPadding(0,0,6,0);
        markRow.addView(markRowLab);

        TableRow markCountRow = new TableRow(getContext());
        TextView markCountLab = new TextView(getContext());
        markCountLab.setText(getString(R.string.count));
        markCountLab.setPadding(0,0,6,0);
        markCountRow.addView(markCountLab);

        for(int i=0; i<markCount[0].length; i++) {
            if(markCount[1][i] != 0) {
                TextView mark = new TextView(getContext());
                mark.setText(String.valueOf(markCount[0][i]));
                mark.setPadding(0,0,6,0);
                markRow.addView(mark);
            }
        }

        for(int i=0; i<markCount[1].length; i++) {
            if(markCount[1][i] != 0) {
                TextView mark = new TextView(getContext());
                mark.setText(String.valueOf(markCount[1][i]));
                mark.setPadding(0,0,6,0);
                markCountRow.addView(mark);
            }
        }
        distTable.addView(markRow);
        distTable.addView(markCountRow);
        RelativeLayout distLayout = (RelativeLayout) view.findViewById(R.id.rl_distribution);
        //distLayout.addView(distTable);
        //TODO: add this table back in when it looks good
        final RelativeLayout chartContainer = (RelativeLayout) view.findViewById(R.id.rl_chart);

        //Distribution Chart
        final BarChart distributionChart = new BarChart(getContext());
        Description distrcription = new Description();
        distrcription.setText(getString(R.string.distributionChart));
        distributionChart.setDescription(distrcription);
        distributionChart.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        List<BarEntry> entries = new ArrayList<BarEntry>();
        for(int i=0; i<markCount[0].length; i++) {
            entries.add(new BarEntry(markCount[0][i], markCount[1][i]));
        }
        BarDataSet barDataSet = new BarDataSet(entries, getString(R.string.count));
        BarData barData = new BarData(barDataSet);
        distributionChart.setData(barData);

        //All students chart
        final BarChart allStudentsChart = new BarChart(getContext());
        Description description = new Description();
        description.setText(getString(R.string.allMarksChart));
        allStudentsChart.setDescription(description);
        allStudentsChart.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        chartContainer.addView(allStudentsChart);
        List<BarEntry> entriesForAll = new ArrayList<BarEntry>();
        StudentQueries studentQueries = new StudentQueries(getContext());
        for(Mark m : marks) {
            Student student = studentQueries.getStudentById(m.getStudentID());
            entriesForAll.add(new BarEntry(m.getStudentID(), m.getMark()));
            Log.d(TAG, "Add new BarEntry for student " + m.getStudentID() + " and mark " + m.getMark());
        }
        LimitLine avg =  new LimitLine((float)average, getString(R.string.mean));
        allStudentsChart.getAxisLeft().addLimitLine(avg);
        BarDataSet allStudentsBarDataSet = new BarDataSet(entriesForAll, getString(R.string.mark));
        BarData allStudentsBarData = new BarData(allStudentsBarDataSet);

        allStudentsChart.setData(allStudentsBarData);

        Spinner chartSpinner = (Spinner) view.findViewById(R.id.sp_chart);
        chartSpinner.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        ArrayList<String> chartNames = new ArrayList<>();
        chartNames.add(getString(R.string.distributionChart));
        chartNames.add(getString(R.string.allMarksChart));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, chartNames);
        chartSpinner.setAdapter(arrayAdapter);

        chartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getSelectedItem().equals(getString(R.string.distributionChart))) {
                    chartContainer.removeAllViewsInLayout();
                    chartContainer.addView(distributionChart);
                    distributionChart.invalidate();
                } else if (adapterView.getSelectedItem().equals(getString(R.string.allMarksChart))) {
                    chartContainer.removeAllViewsInLayout();
                    chartContainer.addView(allStudentsChart);
                    allStudentsChart.invalidate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
        if (context instanceof OnDeleteListener) {
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
        onDeleteListener = null;
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
