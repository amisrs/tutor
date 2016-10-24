package com.amisrs.gavin.tutorhelp.view;

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
import com.amisrs.gavin.tutorhelp.controller.DividerItemDecoration;
import com.amisrs.gavin.tutorhelp.controller.StudentListAdapter;
import com.amisrs.gavin.tutorhelp.db.TutorialQueries;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutorial;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudentListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TUTORIAL = "tutorialParam";

    // TODO: Rename and change types of parameters
    private Tutorial tutorialParam;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TextView noneTextView;

    private OnFragmentInteractionListener mListener;

    public StudentListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tutorial Parameter 1.
     * @return A new instance of fragment StudentListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentListFragment newInstance(Tutorial tutorial) {
        StudentListFragment fragment = new StudentListFragment();
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
        View view = inflater.inflate(R.layout.fragment_student_list, container, false);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView)view.findViewById(R.id.rv_students);
        noneTextView = (TextView)view.findViewById(R.id.tv_none);
        recyclerView.setLayoutManager(linearLayoutManager);

        //http://stackoverflow.com/questions/31242812/how-can-a-divider-line-be-added-in-an-android-recyclerview
       recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        TutorialQueries tutorialQueries = new TutorialQueries(getContext());
        ArrayList<Student> studentArrayList = new ArrayList<>();

        studentArrayList = tutorialQueries.getStudentsForTutorial(tutorialParam);
        if(studentArrayList.size() < 1) {
            noneTextView.setText(getString(R.string.nostudents));
        } else {
            noneTextView.setText("");
        }
        StudentListAdapter adapter = new StudentListAdapter();
        if(getActivity() instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity)getActivity();
            adapter.setOnItemClickListener(baseActivity);
        } else if (getActivity() instanceof StudentsActivity) {
            StudentsActivity studentsActivity = (StudentsActivity)getActivity();
            adapter.setOnItemClickListener(studentsActivity);
        }
        adapter.giveList(studentArrayList);

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
