package com.bumjinkim.GPA4U;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class KimMyAssessmentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private KimMyAssessmentAdapter kimMyAssessmentAdapter;

    private int kim_add_assessment_request_code = 2;

    private ArrayList<KimAssessment> assessments = new ArrayList<>();

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
                myIntent.putExtra("method", "add");
                myIntent.putExtra("course", getIntent().getExtras().getLong("course"));
                startActivityForResult(myIntent, kim_add_assessment_request_code);
            }
        });

        recyclerView = findViewById(R.id.kim_my_assessment_list_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<KimAssessment> assessments = realm.where(KimAssessment.class).equalTo("course.id", getIntent().getExtras().getLong("course")).findAll();

        this.assessments.addAll(assessments);

        kimMyAssessmentAdapter = new KimMyAssessmentAdapter(this, getIntent().getExtras().getLong("course"), this.assessments, recyclerView);
        recyclerView.setAdapter(kimMyAssessmentAdapter);

        BottomNavigationView navigation = findViewById(R.id.navigation);
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

                Realm realm = Realm.getDefaultInstance();
                RealmResults<KimAssessment> assessments = realm.where(KimAssessment.class).equalTo("course.id", getIntent().getExtras().getLong("course")).findAll();

                this.assessments.addAll(assessments);

                kimMyAssessmentAdapter.updateAdapter(this.assessments);
            }
        }
    }
}
