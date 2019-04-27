package com.bumjinkim.GPA4U;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

        final TextView nameView = findViewById(R.id.kim_add_assessment_name);
        final TextView gradeView = findViewById(R.id.kim_add_assessment_grade);
        final CheckBox expectedView = findViewById(R.id.kim_add_assessment_expected);
        final TextView weightView = findViewById(R.id.kim_add_assessment_weight);

        KimAssessment assessment = null;
        int index = 0;
        final String method = getIntent().getExtras().getString("method");
        final String assessmentId = getIntent().getExtras().getString("assessment");

        TinyDB tinyDB = new TinyDB(KimAddAssessmentActivity.this);
        final ArrayList<Object> assessments = tinyDB.getListObject("assessments", KimAssessment.class);
        ArrayList<Object> weights = tinyDB.getListObject("weights", KimWeight.class);

        if (method.equals("edit")) {
            setTitle("Edit Assessment");

            for (int i = 0; i < assessments.size(); i++) {
                Log.d("ASSESSMENTS", String.valueOf(((KimAssessment) assessments.get(i))));
                if (((KimAssessment) assessments.get(i)).id.equals(getIntent().getExtras().get("assessment"))) {
                    assessment = ((KimAssessment) (assessments.get(i)));
                    index = i;

                    nameView.setText(assessment.name);
                    gradeView.setText(String.valueOf(assessment.grade));
                    expectedView.setSelected(assessment.expected);
                    weightView.setText(String.valueOf(assessment.assessmentWeight));
                }
            }
        } else {
            setTitle("Add Assessment");
        }

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

            Button saveButton = findViewById(R.id.kim_add_assessment_save_button);
            final KimAssessment finalAssessment = assessment;
            final int finalIndex = index;
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KimAssessment ast = null;
                    if (method.equals("edit")) {
                        finalAssessment.grade = Double.valueOf(String.valueOf(gradeView.getText()));
                        finalAssessment.name = String.valueOf(nameView.getText());
                        finalAssessment.assessmentWeight = Double.valueOf(String.valueOf(weightView.getText()));
                        finalAssessment.expected = expectedView.isSelected();
                        assessments.set(finalIndex, finalAssessment);
                        ast = finalAssessment;
                    } else {
                        ast = new KimAssessment(getIntent().getExtras().getString("course"), String.valueOf(nameView.getText()), expectedView.isChecked(), courseWeights.get(s.getSelectedItemPosition()).id, Double.valueOf(String.valueOf(gradeView.getText())), Double.valueOf(String.valueOf(weightView.getText())));
                        assessments.add(ast);
                    }

                    TinyDB tinyDB = new TinyDB(KimAddAssessmentActivity.this);
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
