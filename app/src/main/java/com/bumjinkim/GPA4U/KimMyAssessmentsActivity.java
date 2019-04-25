package com.bumjinkim.GPA4U;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class KimMyAssessmentsActivity extends Activity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private KimMyAssessmentAdapter kimMyAssessmentAdapter;

    private String[] names = new String[]{"UIMA", "Intro to Psychology", "Abnormal Psychology"};
    private String[] grades = new String[]{"A", "A", "A"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_kim_my_assessments);

        recyclerView = findViewById(R.id.kim_my_assessment_list_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        kimMyAssessmentAdapter = new KimMyAssessmentAdapter(names, grades);
        recyclerView.setAdapter(kimMyAssessmentAdapter);

    }
}
