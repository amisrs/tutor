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
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.model.Tutor;

//https://github.com/j-mateo/MultiActivityToolbar
public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    FrameLayout activityContainer;
    Toolbar toolbar;
    NavigationView navigationView;
    private int selectedNavItemId;
//TODO: add <include> in xml
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_drawer);

        //this is the parent layout

        //inflate the child layout
    }

    @Override
    public void setContentView(int layoutResID)
    {
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

    protected void prepareNavView(){
        navigationView.setNavigationItemSelectedListener(this);
        if(getSupportActionBar() != null ){
            // Use home/back button instead
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //TODO: change home image
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_person_black_24dp_2x));
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        selectedNavItemId = menuItem.getItemId();

        return onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            //TODO: Remove nd_home as this should be handled by the Home/Up button
            case R.id.nd_home:
                startActivity(new Intent(this, TutorialListActivity.class));
                return true;

            case R.id.nd_attendance:
                startActivity(new Intent(this, AttendanceActivity.class));
                return true;

            case R.id.nd_student :
                startActivity(new Intent(this, StudentsActivity.class));
                return true;

            case R.id.nd_tutor :
                startActivity(new Intent(this, TutorialListActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
