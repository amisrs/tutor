package com.amisrs.gavin.tutorhelp.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.WeekQueries;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.StudentWeek;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.model.Week;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudentWeekDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentWeekDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentWeekDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_WEEK = "weekParam";
    private static final String ARG_STUDENT = "studentParam";
    private static final String ARG_TUTORIAL = "tutorialParam";

    // TODO: Rename and change types of parameters
    private Week weekParam;
    private Student studentParam;
    private Tutorial tutorialParam;
    private StudentWeek studentWeek;

    private OnFragmentInteractionListener mListener;

    public StudentWeekDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StudentWeekDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentWeekDetailsFragment newInstance(Week week, Student student, Tutorial tutorial) {
        StudentWeekDetailsFragment fragment = new StudentWeekDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_WEEK, week);
        args.putParcelable(ARG_STUDENT, student);
        args.putParcelable(ARG_TUTORIAL, tutorial);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weekParam = getArguments().getParcelable(ARG_WEEK);
            studentParam = getArguments().getParcelable(ARG_STUDENT);
            tutorialParam = getArguments().getParcelable(ARG_TUTORIAL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_week_details, container, false);
        TextView weekText = (TextView)view.findViewById(R.id.tv_week);
        TextView studentText = (TextView)view.findViewById(R.id.tv_student);

        weekText.setText(weekParam.toString());
        studentText.setText(studentParam.toString());
        WeekQueries weekQueries = new WeekQueries(getContext());
        studentWeek = weekQueries.getStudentWeekForWeekAndStudentAndTutorial(weekParam, studentParam, tutorialParam);

        //TODO: add in ui elements to manipulate and save to this studentWeek

        // Inflate the layout for this fragment
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
