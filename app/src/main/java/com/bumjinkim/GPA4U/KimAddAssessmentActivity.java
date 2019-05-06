package com.bumjinkim.GPA4U;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

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

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kim_add_assessment);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Realm realm = Realm.getDefaultInstance();

        final TextView nameView = findViewById(R.id.kim_add_assessment_name);
        final TextView gradeView = findViewById(R.id.kim_add_assessment_grade);
        final CheckBox expectedView = findViewById(R.id.kim_add_assessment_expected);
        final TextView weightView = findViewById(R.id.kim_add_assessment_weight);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean expected = preferences.getBoolean("show_expected_gpa", true);
        if (expected) {
            expectedView.setVisibility(View.VISIBLE);
        } else {
            expectedView.setVisibility(View.INVISIBLE);
        }

        KimAssessment assessment = null;

        final String method = getIntent().getExtras().getString("method", "add");
        final Long courseId = getIntent().getExtras().getLong("course");

        final RealmResults<KimWeight> weights = realm.where(KimWeight.class).equalTo("course.id", courseId).findAll();
        final RealmResults<KimCourse> courses = realm.where(KimCourse.class).equalTo("id", getIntent().getExtras().getLong("course")).findAll();

        final Spinner spinner = findViewById(R.id.kim_add_assessment_type);

        ArrayList<String> spinnerItems = new ArrayList();
        for (KimWeight weight : weights) {
            spinnerItems.add(weight.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerItems);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        if (method.equals("edit")) {
            setTitle("Edit Assessment");

            final RealmResults<KimAssessment> assessments = realm.where(KimAssessment.class).equalTo("id", getIntent().getExtras().getLong("assessment")).findAll();
            assessment = assessments.get(0);

            for (int i = 0; i < weights.size(); i++) {
                if (assessment.weight.id.equals(weights.get(i).id)) {
                    spinner.setSelection(i);
                }
            }

            nameView.setText(assessment.name);
            gradeView.setText(String.valueOf(assessment.grade));
            expectedView.setSelected(assessment.expected);
            weightView.setText(String.valueOf(assessment.assessmentWeight));

        } else {
            setTitle("Add Assessment");
        }

        if (weights.size() != 0) {
            Button saveButton = findViewById(R.id.kim_add_assessment_save_button);

            final KimAssessment savedAssessment = assessment;

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KimAssessment kimAssessment = null;

                    if (TextUtils.isEmpty(gradeView.getText()) || TextUtils.isEmpty(weightView.getText()) || TextUtils.isEmpty(nameView.getText())) {
                        return;
                    }

                    if (Double.valueOf(String.valueOf(gradeView.getText())) > 100 || Double.valueOf(String.valueOf(gradeView.getText())) < 0 || Double.valueOf(String.valueOf(weightView.getText())) > 100 || Double.valueOf(String.valueOf(weightView.getText())) < 0) {
                        return;
                    }

                    final Realm realm = Realm.getDefaultInstance();

                    if (method.equals("edit")) {
                        realm.beginTransaction();

                        savedAssessment.grade = Double.valueOf(String.valueOf(gradeView.getText()));
                        savedAssessment.name = String.valueOf(nameView.getText());
                        savedAssessment.assessmentWeight = Double.valueOf(String.valueOf(weightView.getText()));
                        savedAssessment.expected = expectedView.isSelected();
                        savedAssessment.weight = weights.get(spinner.getSelectedItemPosition());

                        realm.copyToRealmOrUpdate(savedAssessment);
                        realm.commitTransaction();
                    } else {
                        realm.beginTransaction();

                        kimAssessment = new KimAssessment();

                        Number currentIdNum = realm.where(KimAssessment.class).max("id");

                        long nextId;
                        if (currentIdNum == null) {
                            nextId = 1;
                        } else {
                            nextId = currentIdNum.intValue() + 1;
                        }
                        kimAssessment.id = nextId;
                        kimAssessment.expected = expectedView.isChecked();
                        kimAssessment.assessmentWeight = Double.valueOf(String.valueOf(weightView.getText()));
                        kimAssessment.grade = Double.valueOf(String.valueOf(gradeView.getText()));
                        kimAssessment.course = courses.get(0);
                        kimAssessment.name = String.valueOf(nameView.getText());
                        kimAssessment.weight = weights.get(spinner.getSelectedItemPosition());

                        realm.copyToRealmOrUpdate(kimAssessment);
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
