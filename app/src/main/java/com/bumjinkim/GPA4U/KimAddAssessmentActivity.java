package com.bumjinkim.GPA4U;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_my_courses:
//                    finish();
//                    return true;
//                case R.id.navigation_my_gpa:
//
//                    return true;
//            }
//            return false;
//        }
//    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kim_add_assessment);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

//        BottomNavigationView navigation = findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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

        final String method;
        long courseId;

        if (getIntent().getExtras() != null) {
            method = getIntent().getExtras().getString("method", "add");
        } else {
            method = "add";
        }
        courseId = getIntent().getExtras().getLong("course");

        final RealmResults<KimWeight> weights = realm.where(KimWeight.class).equalTo("course.id", courseId).findAll();
        final RealmResults<KimCourse> courses = realm.where(KimCourse.class).equalTo("id", getIntent().getExtras().getLong("course")).findAll();

        final Spinner spinner = findViewById(R.id.kim_add_assessment_type);

        ArrayList<String> spinnerItems = new ArrayList();
        for (KimWeight weight : weights) {
            spinnerItems.add(weight.name);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinnerItems);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        if (method.equals("edit")) {
            setTitle("Edit Assessment");

            final RealmResults<KimAssessment> assessments = realm.where(KimAssessment.class).equalTo("id", getIntent().getExtras().getLong("assessment")).findAll();
            assessment = assessments.get(0);

            if (assessment != null) {
                for (int i = 0; i < weights.size(); i++) {
                    if (weights.get(i) != null) {
                        if (assessment.weight.id.equals(weights.get(i).id)) {
                            spinner.setSelection(i);
                        }
                    }
                }

                Log.d("Expected Checkbox: ", String.valueOf(assessment.expected));
                nameView.setText(assessment.name);
                gradeView.setText(String.valueOf(assessment.grade));
                expectedView.setChecked(assessment.expected);
                weightView.setText(String.valueOf(assessment.assessmentWeight));
            }
        } else {
            setTitle("Add Assessment");
        }

        if (weights.size() != 0) {
            Button saveButton = findViewById(R.id.kim_add_assessment_save_button);

            final KimAssessment savedAssessment = assessment;
            final AppCompatActivity finalActivity = this;

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KimAssessment kimAssessment;

                    if (TextUtils.getTrimmedLength(gradeView.getText()) == 0) {
                        Toast.makeText(finalActivity, "Please fill in percent mark.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (TextUtils.getTrimmedLength(weightView.getText()) == 0) {
                        Toast.makeText(finalActivity, "Please fill in assessment weight.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (TextUtils.getTrimmedLength(nameView.getText()) == 0) {
                        Toast.makeText(finalActivity, "Please fill in assessment name.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (Double.valueOf(String.valueOf(gradeView.getText())) > 100 || Double.valueOf(String.valueOf(gradeView.getText())) < 0 || Double.valueOf(String.valueOf(weightView.getText())) > 100 || Double.valueOf(String.valueOf(weightView.getText())) < 0) {
                        Toast.makeText(finalActivity, "Grade/Weight is less than 0 or equal to 100.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    final Realm realm = Realm.getDefaultInstance();

                    if (method.equals("edit")) {
                        if (savedAssessment != null) {
                            realm.beginTransaction();

                            savedAssessment.grade = Double.valueOf(String.valueOf(gradeView.getText()));
                            savedAssessment.name = String.valueOf(nameView.getText());
                            savedAssessment.assessmentWeight = Double.valueOf(String.valueOf(weightView.getText()));
                            savedAssessment.expected = expectedView.isChecked();
                            savedAssessment.weight = weights.get(spinner.getSelectedItemPosition());

                            realm.copyToRealmOrUpdate(savedAssessment);
                            realm.commitTransaction();
                        }
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

                    KimPushNotification.sendPush(KimAddAssessmentActivity.this);

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
