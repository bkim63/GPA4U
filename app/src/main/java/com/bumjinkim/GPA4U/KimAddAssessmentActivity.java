package com.bumjinkim.GPA4U;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;

public class KimAddAssessmentActivity extends AppCompatActivity {

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
    private ArrayList<KimWeight> courseWeights = new ArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kim_add_assessment);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        TinyDB tinyDB = new TinyDB(KimAddAssessmentActivity.this);
        ArrayList<Object> weights = tinyDB.getListObject("weights", KimWeight.class);

        if (weights.size() != 0) {
            ArrayList<String> spinnerItems = new ArrayList();
            for (Object weight : weights) {
                if (((KimWeight) weight).course.equals(getIntent().getExtras().getString("course"))) {
                    courseWeights.add((KimWeight) weight);
                    spinnerItems.add(((KimWeight) weight).name);
                }
            }
            final Spinner s = (Spinner) findViewById(R.id.kim_add_assessment_type);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, spinnerItems);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            s.setAdapter(adapter);
            s.setSelection(0);

            final TextView nameView = findViewById(R.id.kim_add_assessment_name);
            final TextView gradeView = findViewById(R.id.kim_add_assessment_grade);
            final CheckBox expectedView = findViewById(R.id.kim_add_assessment_expected);
            final TextView weightView = findViewById(R.id.kim_add_assessment_weight);

            Button saveButton = findViewById(R.id.kim_add_assessment_save_button);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TinyDB tinyDB = new TinyDB(KimAddAssessmentActivity.this);
                    ArrayList<Object> assessments = tinyDB.getListObject("assessments", KimAssessment.class);

                    KimAssessment assessment = new KimAssessment(getIntent().getExtras().getString("course"), String.valueOf(nameView.getText()), expectedView.isChecked(), courseWeights.get(s.getSelectedItemPosition()).id, Double.valueOf(String.valueOf(gradeView.getText())));
                    assessments.add(assessment);

                    tinyDB.putListObject("assessments", assessments);

                    setResult(RESULT_OK);
                    finish();
                }
            });
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
