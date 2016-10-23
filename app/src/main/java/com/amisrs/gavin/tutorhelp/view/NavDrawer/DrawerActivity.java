package com.amisrs.gavin.tutorhelp.view.NavDrawer;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.model.Student;
import com.amisrs.gavin.tutorhelp.model.Tutor;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.other.ProfileCircle;
import com.amisrs.gavin.tutorhelp.view.BaseActivity;
import com.amisrs.gavin.tutorhelp.view.MenuActivity;
import com.amisrs.gavin.tutorhelp.view.StudentsActivity;
import com.amisrs.gavin.tutorhelp.view.TutorActivity;
import com.amisrs.gavin.tutorhelp.view.TutorialListActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


//http://stackoverflow.com/questions/36095691/android-navigationdrawer-multiple-activities-same-menu
public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "DrawerActivity";
    DrawerLayout drawerLayout;
    FrameLayout activityContainer;
    Toolbar toolbar;
    NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    View navHeader;
    private int navItemIndex;
    Tutor tutor;
    Tutorial tutorial;
    TextView tutorName;
    TextView tutorEmail;
    ImageView navHeaderBg;
    ImageView tutorProfile;

    //TODO: add <include> in xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);


      /*  navHeader = navigationView.inflateHeaderView(R.layout.drawer_nav_header);
        tutorName = (TextView) navHeader.findViewById(R.id.tv_tutorName);
        navHeaderBg = (ImageView) navHeader.findViewById(R.id.iv_navDrawer_bg);
        tutorProfile = (ImageView) navHeader.findViewById(R.id.iv_tutor_profile);
        tutorName = (TextView) findViewById(R.id.tv_tutorName);
        */

        //View header = navigationView.getHeaderView(0);

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

        navigationView = (NavigationView) findViewById(R.id.navigationView);




        /*
        * Get the current tutor
        * */
        tutor = getIntent().getParcelableExtra("tutor");
        if (tutor == null) {
            Log.e(TAG, "No tutor was received from the Intent.");
            //stop
            finish();
        } else {
            Log.d(TAG, tutor.getPerson().getFirstName());
        }

        /*
        * Retrieve, initialise and set the values for the navigation header layout
        * */
        View header = navigationView.getHeaderView(0);
        tutorName = (TextView) header.findViewById(R.id.tv_tutorName);
        tutorEmail = (TextView) header.findViewById(R.id.tv_tutorEmail);
        navHeaderBg = (ImageView) header.findViewById(R.id.iv_navDrawer_bg);
        tutorProfile = (ImageView) header.findViewById(R.id.iv_tutor_profile);

        tutorName.setText(tutor.getPerson().getFirstName() + " " + tutor.getPerson().getLastName());
        tutorEmail.setText(tutor.getPerson().getEmail());

        Glide.with(this)
                .load(R.drawable.wp_004)
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(navHeaderBg);

        Glide.with(this)
                .load(tutor.getPerson().getProfilePath())
                .asBitmap()
                .transform(new ProfileCircle(this))
                .placeholder(R.drawable.ic_default)
                .into(tutorProfile);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //TODO: change the APP NAME!!! otherwise we will get rekt
        setTitle("Morgan's Replacement");
        toolbar.setTitleTextColor(Color.WHITE);

        prepareNavView();

    }

    //TODO method below to be deleted if not used
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    protected void prepareNavView() {
        navigationView.setNavigationItemSelectedListener(this);
        // use the hamburger menu
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.openDrawer,
                R.string.closeDrawer);


    //setting the actionBarToggle to the layout
    drawerLayout.addDrawerListener(drawerToggle);

    //display hamburger
    drawerToggle.syncState();

    // Use home/back button instead
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_add_white_24dp);


}

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
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
            case R.id.nd_tutorial:

                navItemIndex = 0;
                Intent homeIntent = new Intent(this, TutorActivity.class);
                homeIntent.putExtra("tutor", tutor);
                startActivity(homeIntent);
                return true;
            //code below breaks because user has not selected a tutorial
            case R.id.nd_attendance:
                navItemIndex = 1;

                Intent attendanceIntent = new Intent(this, TutorialListActivity.class);
                attendanceIntent.putExtra("tutor", tutor);
                startActivity(attendanceIntent);
                return true;


            case R.id.nd_student:
                navItemIndex = 2;
               /* MenuActivity menuAct = new MenuActivity();
                menuAct.goToStudents();*/
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
