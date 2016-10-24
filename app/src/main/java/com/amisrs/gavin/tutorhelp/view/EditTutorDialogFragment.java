package com.amisrs.gavin.tutorhelp.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.PersonQueries;
import com.amisrs.gavin.tutorhelp.db.TutorQueries;
import com.amisrs.gavin.tutorhelp.model.Person;
import com.amisrs.gavin.tutorhelp.model.Tutor;
import com.amisrs.gavin.tutorhelp.other.DbBitmapUtility;
import com.amisrs.gavin.tutorhelp.other.ProfileCircle;
import com.bumptech.glide.Glide;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditTutorDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditTutorDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTutorDialogFragment extends DialogFragment {
    private static final String TAG = "NewTutorDialogFragment";

    private static final String ARG_TUTOR = "tutor";
    private Tutor tutorParam;



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

    private EditTutorDialogFragment.OnFragmentInteractionListener mListener;
    private EditTutorDialogFragment.NewTutorDialogFragmentListener dialogListener;

    public interface NewTutorDialogFragmentListener {
        public void onDialogPositiveClick(DialogFragment fragment);

        public void onDialogNegativeClick(DialogFragment fragment);
    }

    public EditTutorDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewStudentDialogFragment.
     */
    public static EditTutorDialogFragment newInstance(Tutor tutor) {
        EditTutorDialogFragment fragment = new EditTutorDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TUTOR, tutor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           tutorParam = getArguments().getParcelable(ARG_TUTOR);
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
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText(R.string.editTutor);
        fname = (TextInputEditText) view.findViewById(R.id.et_fname);
        lname = (TextInputEditText) view.findViewById(R.id.et_lname);
        zid = (TextInputEditText) view.findViewById(R.id.et_zid);
        email = (TextInputEditText) view.findViewById(R.id.et_email);

        builder.setView(view);

        fname.setText(tutorParam.getPerson().getFirstName());
        lname.setText(tutorParam.getPerson().getLastName());
        zid.setText(String.valueOf(tutorParam.getPerson().getzID()));
        email.setText(tutorParam.getPerson().getEmail());

        captureButton = (ImageButton) view.findViewById(R.id.btn_camera_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("capture btn clicked");

                if (checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(permissions, CAMERA_PERMISSION_CODE);
                } else {
                    takePicture();
                }
            }
        });
        profilePic = (ImageView) view.findViewById(R.id.iv_camera);
        Glide.with(getContext())
                .load(tutorParam.getPerson().getProfilePath())
                .asBitmap()
                .placeholder(R.drawable.ic_default)
                .transform(new ProfileCircle(getContext()))
                .into(profilePic);


        builder
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("aa", "create");
                        positivePress();
                        dialogListener.onDialogPositiveClick(EditTutorDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("aa", "cancel");
                        dialogListener.onDialogNegativeClick(EditTutorDialogFragment.this);
                        dialogInterface.cancel();
                    }
                });

        return builder.create();
    }


    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        EditTutorDialogFragment.this.startActivityForResult(intent, REQUEST_CODE);
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

        fileName = zid.getText().toString()+"_tutor.PNG";
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
            personQueries.updatePerson(tutorParam.getPersonID(), fnameString, lnameString, zidInt, emailString);
            personQueries.addImageFilePathForPerson(tutorParam.getPersonID(), profilePath);
            //Log.d(TAG, "Added person to database: " + addedPerson.getzID() + " " + addedPerson.getFirstName() + " " + addedPerson.getLastName());
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
        if (context instanceof EditTutorDialogFragment.OnFragmentInteractionListener) {
            mListener = (EditTutorDialogFragment.OnFragmentInteractionListener) context;
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
