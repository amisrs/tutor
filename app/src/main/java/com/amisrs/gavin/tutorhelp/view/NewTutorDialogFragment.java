package com.amisrs.gavin.tutorhelp.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.PersonQueries;
import com.amisrs.gavin.tutorhelp.db.StudentQueries;
import com.amisrs.gavin.tutorhelp.db.TutorQueries;
import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutor;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.other.DbBitmapUtility;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewTutorDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewTutorDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewTutorDialogFragment extends DialogFragment {
    private static final String TAG = "NewTutorDialogFragment";
    private ImageView profilePic;
    private ImageButton captureButton;
    // private Button btn;
    private String fileName;
    private String[] permissions = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_PERMISSION_CODE = 1;
    private static final int REQUEST_CODE = 100;
    private String imgPath = "default.png";
    Bitmap photo;
    Boolean imgTaken = false;

    TextInputEditText zid;
    TextInputEditText fname;
    TextInputEditText lname;
    TextInputEditText email;

    private NewTutorDialogFragment.OnFragmentInteractionListener mListener;
    private NewTutorDialogFragment.NewTutorDialogFragmentListener dialogListener;

    public interface NewTutorDialogFragmentListener {
        public void onDialogPositiveClick(DialogFragment fragment);

        public void onDialogNegativeClick(DialogFragment fragment);
    }

    public NewTutorDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewStudentDialogFragment.
     */
    public static NewTutorDialogFragment newInstance() {
        NewTutorDialogFragment fragment = new NewTutorDialogFragment();
        //Bundle args = new Bundle();
        //args.putParcelable(ARG_TUTORIAL, tutorial);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // tutorialParam = getArguments().getParcelable(ARG_TUTORIAL);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_tutor_dialog, container, false);

        return view;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_new_tutor, null);
        builder.setView(view);

        captureButton = (ImageButton) view.findViewById(R.id.btn_camera_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("capture btn clicked");

                if (checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(permissions, CAMERA_PERMISSION_CODE);
                } else {
                    takePicture();
                }
            }
        });
        profilePic = (ImageView) view.findViewById(R.id.iv_camera);
        profilePic.setImageResource(R.drawable.ic_default);

        builder
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("aa", "create");
                        positivePress();
                        dialogListener.onDialogPositiveClick(NewTutorDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("aa", "cancel");
                        dialogListener.onDialogNegativeClick(NewTutorDialogFragment.this);
                        dialogInterface.cancel();
                    }
                });

        return builder.create();
    }


    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        NewTutorDialogFragment.this.startActivityForResult(intent, REQUEST_CODE);
    }

    //http://stackoverflow.com/questions/28450049/how-get-result-from-onactivityresult-in-fragment
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        System.out.println("actResult is called");
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == REQUEST_CODE){
            switch (resultCode) {
                case Activity.RESULT_OK:
                    photo = (Bitmap) data.getExtras().get("data");
                    profilePic.setImageBitmap(photo);
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
        }
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



    //http://stackoverflow.com/questions/23131768/how-to-save-an-image-to-internal-storage-and-then-show-it-on-another-activity?noredirect=1&lq=1
    private String saveImagePath(Bitmap bitmap){
        FileOutputStream fileOutputStream = null;
        String imgFilePath = getContext().getFilesDir().toString();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        fileName = strDate +"_tutor.PNG";
        try{
            fileOutputStream = getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(DbBitmapUtility.getBytes(bitmap));
            fileOutputStream.flush();
            imgPath = imgFilePath+"/" + fileName;
            //TODO: delete the test line below
            System.out.println("imgPath = " + imgPath);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                fileOutputStream.close();
                Log.d(TAG, "FileOutputStream is closed successfully");
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        Log.d(TAG, "File path is saved to DB");
        return imgFilePath;

    }


    public void positivePress() {
        Dialog dialog = getDialog();
        zid = (TextInputEditText) dialog.findViewById(R.id.et_zid);
        fname = (TextInputEditText) dialog.findViewById(R.id.et_fname);
        lname = (TextInputEditText) dialog.findViewById(R.id.et_lname);
        email = (TextInputEditText) dialog.findViewById(R.id.et_email);
        if(imgTaken) {
            saveImagePath(photo);
        }


        //TODO: improve validation
        String zidString = zid.getText().toString();
        Pattern zidPattern = Pattern.compile("\\d+");
        Matcher zidMatcher = zidPattern.matcher(zidString);
        if (zidMatcher.find()) {


            String fnameString = fname.getText().toString();
            String lnameString = lname.getText().toString();
            int zidInt = Integer.parseInt(zid.getText().toString());
            String emailString = email.getText().toString();
            String profilePath = imgPath;
            System.out.println("imgPath = " + imgPath);

            //add to database
            PersonQueries personQueries = new PersonQueries(getContext());
            Person addedPerson = personQueries.addPerson(new Person(fnameString, lnameString, zidInt, profilePath , emailString));

            TutorQueries tutorQueries = new TutorQueries(getContext());
            tutorQueries.addTutor(new Tutor(addedPerson.getPersonID(), addedPerson));

            addedPerson.setProfilePath(imgPath);
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
        if (context instanceof NewTutorDialogFragment.OnFragmentInteractionListener) {
            mListener = (NewTutorDialogFragment.OnFragmentInteractionListener) context;
            dialogListener = (NewTutorDialogFragment.NewTutorDialogFragmentListener) context;
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
