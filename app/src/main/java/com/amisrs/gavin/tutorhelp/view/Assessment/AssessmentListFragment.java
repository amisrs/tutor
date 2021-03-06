package com.amisrs.gavin.tutorhelp.view.Assessment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.controller.AssessmentListAdapter;
import com.amisrs.gavin.tutorhelp.controller.DividerItemDecoration;
import com.amisrs.gavin.tutorhelp.controller.StudentListAdapter;
import com.amisrs.gavin.tutorhelp.db.AssessmentQueries;
import com.amisrs.gavin.tutorhelp.db.TutorialQueries;
import com.amisrs.gavin.tutorhelp.model.Assessment;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.view.BaseActivity;
import com.amisrs.gavin.tutorhelp.view.StudentsActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AssessmentListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssessmentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssessmentListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TERM = "termParam";

    // TODO: Rename and change types of parameters
    private String termParam;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TextView noneTextView;

    private OnFragmentInteractionListener mListener;

    public AssessmentListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param term Parameter 1.
     * @return A new instance of fragment StudentListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssessmentListFragment newInstance(String term) {
        AssessmentListFragment fragment = new AssessmentListFragment();
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
        View view = inflater.inflate(R.layout.fragment_student_list, container, false);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView)view.findViewById(R.id.rv_students);
        noneTextView = (TextView)view.findViewById(R.id.tv_none);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
//        TutorialQueries tutorialQueries = new TutorialQueries(getContext());
        AssessmentQueries assessmentQueries = new AssessmentQueries(getContext());
        ArrayList<Assessment> assessmentArrayList = new ArrayList<>();
        assessmentArrayList = assessmentQueries.getAssessmentsForTerm(termParam);

        if(assessmentArrayList.size() < 1) {
            noneTextView.setText(getString(R.string.noassessments));
        } else {
            noneTextView.setText("");
        }

        AssessmentListAdapter adapter = new AssessmentListAdapter();
        AssessmentsActivity assessmentsActivity = (AssessmentsActivity)getActivity();
        adapter.setOnAssessmentClickListener(assessmentsActivity);
        adapter.giveList(assessmentArrayList);

        recyclerView.setAdapter(adapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }
}
