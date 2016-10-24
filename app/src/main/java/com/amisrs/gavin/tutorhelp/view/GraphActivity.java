package com.amisrs.gavin.tutorhelp.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.amisrs.gavin.tutorhelp.R;
import com.amisrs.gavin.tutorhelp.db.TutorialQueries;
import com.amisrs.gavin.tutorhelp.model.Enrolment;
import com.amisrs.gavin.tutorhelp.model.StudentWeek;
import com.amisrs.gavin.tutorhelp.model.Tutorial;
import com.amisrs.gavin.tutorhelp.view.NavDrawer.DrawerActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.PieHighlighter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends DrawerActivity {

    Tutorial tutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_chart);
        tutorial = getIntent().getParcelableExtra("tutorial");

        TutorialQueries tutorialQueries = new TutorialQueries(this);
        ArrayList<Enrolment> enrolments = tutorialQueries.getEnrolmentsForTutorial(tutorial);
        int PCount = 0;
        int CCount = 0;
        int DCount = 0;
        int HDCount = 0;
        int FCount = 0;
        for(Enrolment e : enrolments) {
            if(e.getGrade() > 49 && e.getGrade() < 65) {
                PCount++;
            } else if (e.getGrade() > 64 && e.getGrade() < 75) {
                CCount++;
            } else if (e.getGrade() > 74 && e.getGrade() < 85) {
                DCount++;
            } else if (e.getGrade() > 84) {
                HDCount++;
            } else {
                FCount++;
            }
        }


        PieChart attendancePie = new PieChart(this);
        attendancePie.highlightValues(null);
        attendancePie.setEntryLabelColor(Color.BLACK);
        attendancePie.setUsePercentValues(true);
        attendancePie.setDescription(null);
        attendancePie.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //get StudentWeeks for student
        relativeLayout.addView(attendancePie);


        List<PieEntry> pieEntries = new ArrayList<PieEntry>();
        pieEntries.add(new PieEntry(PCount, "P"));
        pieEntries.add(new PieEntry(CCount, "C"));
        pieEntries.add(new PieEntry(DCount, "D"));
        pieEntries.add(new PieEntry(HDCount, "HD"));
        pieEntries.add(new PieEntry(FCount, "F"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, getString(R.string.grade));
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(pieDataSet);


        attendancePie.setData(pieData);
        attendancePie.invalidate();

//        PieChart pieChart = new PieChart(this);
//        pieChart.setDescription(null);
//        pieChart.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        list.add(new PieEntry(PCount, "P"));
//        list.add(new PieEntry(CCount, "C"));
//        list.add(new PieEntry(DCount, "D"));
//        list.add(new PieEntry(HDCount, "HD"));
//        list.add(new PieEntry(FCount, "F"));
//
//        PieDataSet pieDataSet = new PieDataSet(list, "Grades");
//        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//        PieData pieData = new PieData(pieDataSet);
//        pieChart.setData(pieData);
//        pieDataSet.setHighlightEnabled(true);
//
//        pieChart.invalidate();
//
//        relativeLayout.addView(pieChart);
//        pieChart.invalidate();

    }


}
