package com.amisrs.gavin.tutorhelp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.WeekQueries;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.StudentWeek;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.model.Week;
import com.amisrs.gavin.tutorhelp.other.ProfileCircle;
import com.bumptech.glide.Glide;

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
    private static final String TAG = "StudentWeekDetailsFrag";
    // TODO: Rename and change types of parameters
    private Week weekParam;
    private Student studentParam;
    private Tutorial tutorialParam;
    private StudentWeek studentWeek;
    private boolean isEdit;
    public boolean isEmpty;

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
        isEdit = false;
        isEmpty = true;
        View view = inflater.inflate(R.layout.fragment_student_week_details, container, false);
        TextView weekText = (TextView) view.findViewById(R.id.tv_week);
        final TextView studentText = (TextView) view.findViewById(R.id.tv_student);
        Switch aSwitch = (Switch) view.findViewById(R.id.cb_attended);
        final TextInputEditText publicComment = (TextInputEditText) view.findViewById(R.id.et_pub);
        final TextInputEditText privateComment = (TextInputEditText) view.findViewById(R.id.et_priv);
        final ImageButton editButton = (ImageButton) view.findViewById(R.id.iv_edit);
        final ImageButton saveButton = (ImageButton) view.findViewById(R.id.iv_save);
        final ImageButton emailButton = (ImageButton) view.findViewById(R.id.emailBtn);
        final ImageView profile = (ImageView) view.findViewById(R.id.iv_profile);
        final TextView idText = (TextView) view.findViewById(R.id.tv_zid);

        Glide.with(getContext())
                .load(studentParam.getPerson().getProfilePath())
                .asBitmap()
                .transform(new ProfileCircle(getContext()))
                .placeholder(R.drawable.ic_default)
                .into(profile);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                toggleAttended(b);
            }
        });

        weekText.setText(weekParam.toString());
        studentText.setText(studentParam.getPerson().getFirstName() + " " + studentParam.getPerson().getLastName());
        idText.setText("z" + String.valueOf(studentParam.getPerson().getzID()));
        final WeekQueries weekQueries = new WeekQueries(getContext());
        studentWeek = weekQueries.getStudentWeekForWeekAndStudentAndTutorial(weekParam, studentParam, tutorialParam);
        Log.d(TAG, "the current student week public comment; " + studentWeek.getPublicComment());
        publicComment.setText(studentWeek.getPublicComment());
        privateComment.setText(studentWeek.getPrivateComment());

        if (studentWeek.getAttended() == 0) {
            aSwitch.setChecked(false);
        } else {
            aSwitch.setChecked(true);
        }
        if(publicComment.getText().toString().matches("")){
            isEmpty = true;
            emailButton.setVisibility(View.GONE);

        } else {
            isEmpty = false;
            emailButton.setVisibility(View.VISIBLE);
        }

        /*
        * The following resource was referenced -
        * https://code.tutsplus.com/tutorials/quick-tip-enabling-users-to-send-email-from-your-android-applications-the-easy-way--mobile-1686
        */
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);

                String[] emailTo = new String[]{studentParam.getPerson().getEmail()};
                intent.putExtra(Intent.EXTRA_EMAIL, emailTo);
                intent.putExtra(Intent.EXTRA_SUBJECT, "INFS3634");
                intent.setType("*/*");
                String emailBody = studentWeek.getPublicComment();
                intent.putExtra(Intent.EXTRA_TEXT, emailBody);
                startActivity(intent.createChooser(intent, "Send email in: "));
            }
        });


        //TODO: add in ui elements to manipulate and save to this studentWeek
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update details
                studentWeek.setPublicComment(publicComment.getText().toString());
                studentWeek.setPrivateComment(privateComment.getText().toString());
                weekQueries.updateStudentWeek(studentWeek);
                //update grade

                publicComment.setInputType(InputType.TYPE_NULL);
                privateComment.setInputType(InputType.TYPE_NULL);

                editButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_mode_edit_black_36dp));
                saveButton.setVisibility(View.GONE);

                //checks the public field. If empty, email button is disabled.
                if(publicComment.getText().toString().matches("")){
                    isEmpty = true;
                    emailButton.setVisibility(View.GONE);

                } else {
                    isEmpty = false;
                    emailButton.setVisibility(View.VISIBLE);
                }
                isEdit = false;
                //onButtonPressed("save");
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEdit) {
                    publicComment.setInputType(InputType.TYPE_CLASS_TEXT);
                    privateComment.setInputType(InputType.TYPE_CLASS_TEXT);
                    editButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_clear_black_36dp));
                    saveButton.setVisibility(View.VISIBLE);

                    isEdit = true;
                } else {
                    publicComment.setText(studentWeek.getPublicComment());
                    privateComment.setText(studentWeek.getPrivateComment());
                    publicComment.setInputType(InputType.TYPE_NULL);
                    privateComment.setInputType(InputType.TYPE_NULL);

                    editButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_mode_edit_black_36dp));
                    saveButton.setVisibility(View.GONE);
                    isEdit = false;
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void toggleAttended(boolean b) {
        WeekQueries weekQueries = new WeekQueries(getContext());
        if (b) {
            //now attended
            weekQueries.setAttendanceForStudentWeek(studentParam, weekParam, 1);
        } else {
            weekQueries.setAttendanceForStudentWeek(studentParam, weekParam, 0);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
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
