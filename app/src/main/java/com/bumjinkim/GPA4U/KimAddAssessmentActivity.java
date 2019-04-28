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
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

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

        final Realm realm = Realm.getDefaultInstance();

        final TextView nameView = findViewById(R.id.kim_add_assessment_name);
        final TextView gradeView = findViewById(R.id.kim_add_assessment_grade);
        final CheckBox expectedView = findViewById(R.id.kim_add_assessment_expected);
        final TextView weightView = findViewById(R.id.kim_add_assessment_weight);

        KimAssessment assessment = null;

        final String method = getIntent().getExtras().getString("method");
        final Long courseId = getIntent().getExtras().getLong("course");

        final RealmResults<KimWeight> weights = realm.where(KimWeight.class).equalTo("course.id", courseId).findAll();
        final RealmResults<KimCourse> courses = realm.where(KimCourse.class).equalTo("id", getIntent().getExtras().getLong("course")).findAll();

        if (method.equals("edit")) {
            setTitle("Edit Assessment");

            final RealmResults<KimAssessment> asts = realm.where(KimAssessment.class).equalTo("id", getIntent().getExtras().getLong("assessment")).findAll();

            assessment = asts.get(0);

            nameView.setText(assessment.name);
            gradeView.setText(String.valueOf(assessment.grade));
            expectedView.setSelected(assessment.expected);
            weightView.setText(String.valueOf(assessment.assessmentWeight));

        } else {
            setTitle("Add Assessment");
        }

        if (weights.size() != 0) {
            ArrayList<String> spinnerItems = new ArrayList();
            for (KimWeight weight : weights) {
                spinnerItems.add(weight.name);
            }
            final Spinner s = (Spinner) findViewById(R.id.kim_add_assessment_type);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, spinnerItems);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            s.setAdapter(adapter);
            s.setSelection(0);

            Button saveButton = findViewById(R.id.kim_add_assessment_save_button);

            final KimAssessment finalAssessment = assessment;

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KimAssessment ast = null;

                    if (method.equals("edit")) {
                        realm.beginTransaction();

                        finalAssessment.grade = Double.valueOf(String.valueOf(gradeView.getText()));
                        finalAssessment.name = String.valueOf(nameView.getText());
                        finalAssessment.assessmentWeight = Double.valueOf(String.valueOf(weightView.getText()));
                        finalAssessment.expected = expectedView.isSelected();
                        finalAssessment.weight = weights.get(s.getSelectedItemPosition());

                        realm.copyToRealmOrUpdate(finalAssessment);
                        realm.commitTransaction();
                    } else {
                        realm.beginTransaction();

                        ast = new KimAssessment();

                        Number currentIdNum = realm.where(KimAssessment.class).max("id");

                        long nextId;
                        if (currentIdNum == null) {
                            nextId = 1;
                        } else {
                            nextId = currentIdNum.intValue() + 1;
                        }
                        ast.id = nextId;
                        ast.expected = expectedView.isChecked();
                        ast.assessmentWeight = Double.valueOf(String.valueOf(weightView.getText()));
                        ast.grade = Double.valueOf(String.valueOf(gradeView.getText()));
                        ast.course = courses.get(0);
                        ast.name = String.valueOf(nameView.getText());
                        ast.weight = weights.get(s.getSelectedItemPosition());

                        realm.copyToRealmOrUpdate(ast);
                        realm.commitTransaction();
                    }

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
