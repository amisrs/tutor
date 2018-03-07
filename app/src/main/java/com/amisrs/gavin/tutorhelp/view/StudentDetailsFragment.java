package com.amisrs.gavin.tutorhelp.view;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;

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
import android.widget.ImageButton;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.amisrs.gavin.tutorhelp.model.Tutor;
import com.amisrs.gavin.tutorhelp.model.StudentWeek;

import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.other.DbBitmapUtility;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

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
    private static final String ARG_TUTOR = "tutor";
    // TODO: Rename and change types of parameters
    private Student studentParam;
    private Tutorial tutorialParam;
    private Tutor tutorParam;

    private boolean isEdit;

    private OnFragmentInteractionListener mListener;
    private OnDeleteListener onDeleteListener;

    private String zid;
    private String fileName;
    private String[] permissions = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int REQUEST_CODE = 100;
    private String imgPath = "default.png";
    Bitmap photo;
    Boolean imgTaken = false;
    ImageView profile;
    ImageButton captureButton;
    ImageButton bluetoothButton;
    BluetoothManager bluetoothManager;

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
     * @param tutor    Parameter 3.
     * @return A new instance of fragment StudentDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentDetailsFragment newInstance(Student student, Tutorial tutorial, Tutor tutor) {
        StudentDetailsFragment fragment = new StudentDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_STUDENT, student);
        args.putParcelable(ARG_TUTORIAL, tutorial);
        args.putParcelable(ARG_TUTOR, tutor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            studentParam = getArguments().getParcelable(ARG_STUDENT);
            tutorialParam = getArguments().getParcelable(ARG_TUTORIAL);
            tutorParam = getArguments().getParcelable(ARG_TUTOR);
        }
        bluetoothManager = (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);

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
        final TextInputEditText fnameTextView = (TextInputEditText) view.findViewById(R.id.tv_fname);
        final TextInputEditText lnameTextView = (TextInputEditText) view.findViewById(R.id.tv_lname);
        final TextInputEditText zidTextView = (TextInputEditText) view.findViewById(R.id.tv_zid);
        final TextInputEditText emailTextView = (TextInputEditText) view.findViewById(R.id.tv_email);
        profile = (ImageView) view.findViewById(R.id.iv_pic);
        bluetoothButton = (ImageButton) view.findViewById(R.id.btn_bluetooth);
        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "bt button clicked");
                BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
                Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

                Log.d(TAG, bondedDevices.toString());

            }
        });

        captureButton = (ImageButton) view.findViewById(R.id.btn_camera_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("capture btn clicked");
                if (checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, CAMERA_PERMISSION_CODE);
                } else {
                    takePicture();
                }
            }
        });
        if(studentParam.getPerson().getProfilePath().equals("default.png")){
            profile.setImageResource(R.drawable.ic_default);
        } else {
            Glide.with(getContext())
                    .load(studentParam.getPerson().getProfilePath())
                    .asBitmap()
                    .placeholder(R.drawable.ic_default)
                    .into(profile);

        }
        //TODO gradeText should not be editable?
        final TextView gradeText = (TextView) view.findViewById(R.id.et_grade);
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
        TutorialListAdapter adapter = new TutorialListAdapter(getContext(), tutorParam);
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
        emailTextView.setText(studentParam.getPerson().getEmail());
        gradeText.setText(String.valueOf(enrolment.getGrade()));


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), R.string.editSave, Toast.LENGTH_SHORT).show();

                //update details
                PersonQueries personQueries = new PersonQueries(getContext());
                personQueries.updatePerson(studentParam.getPersonID(),
                        fnameTextView.getText().toString(),
                        lnameTextView.getText().toString(),
                        Integer.parseInt(zidTextView.getText().toString()),
                        emailTextView.getText().toString());
                //personQueries.addImageFilePathForPerson(studentParam.getPersonID(), imgPath);
                studentParam.getPerson().setFirstName(fnameTextView.getText().toString());
                studentParam.getPerson().setLastName(lnameTextView.getText().toString());
                studentParam.getPerson().setEmail(emailTextView.getText().toString());
                studentParam.getPerson().setzID(Integer.parseInt(zidTextView.getText().toString()));
                studentParam.getPerson().setProfilePath(imgPath);
                //TODO delete the line below
                System.out.println("IMGPATH: " + imgPath);


                //update grade


                fnameTextView.setInputType(InputType.TYPE_NULL);
                lnameTextView.setInputType(InputType.TYPE_NULL);
                zidTextView.setInputType(InputType.TYPE_NULL);
                emailTextView.setInputType(InputType.TYPE_NULL);
                gradeText.setInputType(InputType.TYPE_NULL);

                editButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_mode_edit_black_36dp));
                saveButton.setVisibility(View.INVISIBLE);
                captureButton.setVisibility(View.INVISIBLE);
                isEdit = false;
                onButtonPressed("student details saved");
                onButtonPressed("save");


            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEdit) {
                    Toast.makeText(getContext(), R.string.editStart, Toast.LENGTH_SHORT).show();

                    fnameTextView.setInputType(InputType.TYPE_CLASS_TEXT);
                    lnameTextView.setInputType(InputType.TYPE_CLASS_TEXT);
                    zidTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_clear_black_36dp));
                    saveButton.setVisibility(View.VISIBLE);
                    captureButton.setVisibility(View.VISIBLE);
                    isEdit = true;
                } else {
                    Toast.makeText(getContext(), R.string.editCancel, Toast.LENGTH_SHORT).show();

                    fnameTextView.setText(studentParam.getPerson().getFirstName());
                    lnameTextView.setText(studentParam.getPerson().getLastName());
                    zidTextView.setText(String.valueOf(studentParam.getPerson().getzID()));
                    emailTextView.setText(studentParam.getPerson().getEmail());
                    gradeText.setText(String.valueOf(enrolment.getGrade()));
                    fnameTextView.setInputType(InputType.TYPE_NULL);
                    lnameTextView.setInputType(InputType.TYPE_NULL);
                    zidTextView.setInputType(InputType.TYPE_NULL);
                    emailTextView.setInputType(InputType.TYPE_NULL);
                    gradeText.setInputType(InputType.TYPE_NULL);


                    editButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_mode_edit_black_36dp));
                    captureButton.setVisibility(View.INVISIBLE);
                    saveButton.setVisibility(View.INVISIBLE);
                    isEdit = false;
                }
            }
        });

        RelativeLayout chartContainer = (RelativeLayout) view.findViewById(R.id.rl_chart);
        PieChart attendancePie = new PieChart(getContext());
        attendancePie.setDescription(null);
        attendancePie.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //get StudentWeeks for student
        ArrayList<StudentWeek> studentWeeks = studentQueries.getStudentWeekForStudent(studentParam);
        int attendedCount = 0;

        for(StudentWeek sw : studentWeeks) {
            if(sw.getAttended() == 1) {

                attendedCount++;
            }
        }

        List<PieEntry> pieEntries = new ArrayList<PieEntry>();
        pieEntries.add(new PieEntry(attendedCount));
        pieEntries.add(new PieEntry(studentWeeks.size() - attendedCount));
        PieDataSet pieDataSet = new PieDataSet(pieEntries, getString(R.string.attendance));
        PieData pieData = new PieData(pieDataSet);
        attendancePie.setData(pieData);
        chartContainer.addView(attendancePie);
        attendancePie.invalidate();


        return view;
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        StudentDetailsFragment.this.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("actResult is called");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    photo = (Bitmap) data.getExtras().get("data");
                    profile.setImageBitmap(photo);
                    imgTaken = true;



                    Log.d(TAG, "User successfully took image");
                    break;
                case Activity.RESULT_CANCELED:
                    //if user decides not to take a picture
                    imgPath = "default.png";
                    imgTaken = false;
                    Log.d(TAG, "User cancelled request to take image");
                    break;
                default:
                    imgPath = "default.png";
                    imgTaken = false;
                    break;
            }
            if (imgTaken) {
                getImagePath(photo);

            }

        }
    }



    private String getImagePath(Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        String imgFilePath = getContext().getFilesDir().toString();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date();
        String strDate = sdfDate.format(now);

        fileName = strDate + "_student.PNG";

        try {
            fileOutputStream = getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(DbBitmapUtility.getBytes(bitmap));
            fileOutputStream.flush();
            imgPath = imgFilePath + "/" + fileName;

            PersonQueries personQueries = new PersonQueries(getContext());
            personQueries.addImageFilePathForPerson(studentParam.getPersonID(), imgPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
                Log.d(TAG, "FileOutputStream is closed successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "File path is saved to DB");
        return imgFilePath;

    }


    // TODO: Rename method, update argument and hook method into UI eventx
    public void onButtonPressed(String string) {
        if (mListener != null) {
            mListener.onFragmentInteraction(string);
        }
    }

    public void refreshGrade(Mark mark) {
        Log.d(TAG, "Refreshing grade");
        final TextView gradeText = (TextView) getView().findViewById(R.id.et_grade);
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
