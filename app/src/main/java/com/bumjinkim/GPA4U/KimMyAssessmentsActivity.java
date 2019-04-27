package com.bumjinkim.GPA4U;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.List;

public class KimMyAssessmentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private KimMyAssessmentAdapter kimMyAssessmentAdapter;

    private int kim_add_assessment_request_code = 2;

    private ArrayList<Object> assessments = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_my_courses:
                    finish();
                    return true;
                case R.id.navigation_my_gpa:

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_kim_my_assessments);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = findViewById(R.id.kim_add_course_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(KimMyAssessmentsActivity.this, KimAddAssessmentActivity.class);
                myIntent.putExtra("course", getIntent().getExtras().getString("course"));
                startActivityForResult(myIntent, kim_add_assessment_request_code);
            }
        });

        recyclerView = findViewById(R.id.kim_my_assessment_list_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        TinyDB tinyDB = new TinyDB(this);
        ArrayList<Object> assessments = tinyDB.getListObject("assessments", Assessment.class);

        for (Object assessment : assessments) {
            Log.d("MY ASSESSMENT", getIntent().getExtras().getString("course"));

            if (((Assessment)assessment).course.equals(getIntent().getExtras().getString("course"))) {
                this.assessments.add(assessment);
            }
        }

        kimMyAssessmentAdapter = new KimMyAssessmentAdapter(this, this.assessments, recyclerView);
        recyclerView.setAdapter(kimMyAssessmentAdapter);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == kim_add_assessment_request_code) {
            if (resultCode == RESULT_OK) {
                this.assessments.clear();

                TinyDB tinyDB = new TinyDB(this);
                ArrayList<Object> assessments = tinyDB.getListObject("assessments", Assessment.class);

                for (Object assessment : assessments) {
                    if (((Assessment)assessment).course.equals(getIntent().getExtras().getString("course"))) {
                        this.assessments.add(assessment);
                    }
                }

                kimMyAssessmentAdapter.updateAdapter(this.assessments);
            }
        }
    }
}
