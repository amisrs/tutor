package com.amisrs.gavin.tutorhelp.view;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutor;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.other.ProfileCircle;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


//https://github.com/j-mateo/MultiActivityToolbar
public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "DrawerActivity";
    DrawerLayout drawerLayout;
    FrameLayout activityContainer;
    Toolbar toolbar;
    NavigationView navigationView;
    View navHeader;
    private int navItemIndex;
    Tutor tutor;
    Tutorial tutorial;
    TextView tutorName;
    ImageView navHeaderBg, tutorProfile;

    //TODO: add <include> in xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        tutor = getIntent().getParcelableExtra("tutor");
        if (tutor == null) {
            Log.e(TAG, "No tutor was received from the Intent.");
            //stop
            finish();
        }
  /*      navHeader = navigationView.inflateHeaderView(R.layout.drawer_nav_header);
        tutorName = (TextView) navHeader.findViewById(R.id.tv_tutorName);
        navHeaderBg = (ImageView) navHeader.findViewById(R.id.iv_navDrawer_bg);
        tutorProfile = (ImageView) navHeader.findViewById(R.id.iv_tutor_profile);*/
//        tutorName = (TextView) findViewById(R.id.tv_tutorName);
//        tutorName.setText(tutor.getPerson().getFirstName());

    }



    @Override
    public void setContentView(int layoutResID) {

        //this is the parent layout
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer, null);
        //inflate the child layout
        activityContainer = (FrameLayout) drawerLayout.findViewById(R.id.fl_nav_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);

        //child's layoutResId is passed into inflated layout
        super.setContentView(drawerLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);




        setSupportActionBar(toolbar);
        setTitle("NAV DRAW TEST");

        prepareNavView();


    }
    // TextView tutorName

 /*   protected void loadNavHeader(Tutor tutor) {

       *//* tutor = getIntent().getParcelableExtra("tutor");
        if (tutor == null) {
            Log.e(TAG, "No tutor was received from the Intent.");
            //stop
            finish();
        }*//*
       //Setting the tutor name to be displayed in the header
        tutorName.setText(tutor.getPerson().getFirstName() + " " + tutor.getPerson().getLastName());


        //Loading the header background image
        Glide.with(this)
                .load("http://api.androidhive.info/images/nav-menu-header-bg.jpg")
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(navHeaderBg);

        Glide.with(this)
                .load("http://api.androidhive.info/images/nav-menu-header-bg.jpg")
                .crossFade()
                .transform(new ProfileCircle(this))
                .into(tutorProfile);


    }*/

    protected void prepareNavView() {
        navigationView.setNavigationItemSelectedListener(this);
        if (getSupportActionBar() != null) {
            // Use home/back button instead
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //TODO: change home image
            //getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_person_black_24dp_2x));
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        navItemIndex = menuItem.getItemId();

        return onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch (menuItem.getItemId()) {

            //TODO: Remove nd_home or set to something else as this should be handled by the Home/Up button
            //TODO: same problem as getting the nav header image
          /*  case R.id.nd_home:

                navItemIndex = 0;
                Intent homeIntent = new Intent(this, TutorialListActivity.class);
                homeIntent.putExtra("tutor", tutor);
                startActivity(homeIntent);
                return true;*/
        //code below breaks because user has not selected a tutorial
            /*case R.id.nd_attendance:
                navItemIndex = 1;
                Intent attendanceIntent = new Intent(this, AttendanceActivity.class);
                attendanceIntent.putExtra("tutorial", tutorial);
                startActivity(attendanceIntent);
                return true;*/

            case R.id.nd_student:
                navItemIndex = 2;
                Intent studentIntent = new Intent(this, StudentsActivity.class);

                startActivity(studentIntent);
                return true;

            case R.id.nd_tutor:
                navItemIndex = 3;
                Intent tutorIntent = new Intent(this, TutorialListActivity.class);
                startActivity(tutorIntent);
                return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    public TextView getTutorName() {
        return tutorName;
    }

    public void setTutorName(TextView tutorName) {
        this.tutorName = tutorName;
    }
}
