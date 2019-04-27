package com.bumjinkim.GPA4U;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.widget.LinearLayout.HORIZONTAL;

public class KimAddCourseActivity extends AppCompatActivity {

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

    private LinearLayout weightLayout;
    private ArrayList<EditText> weightNameViews = new ArrayList<>();
    private ArrayList<EditText> weightPercentViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kim_add_course);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final Realm realm = Realm.getDefaultInstance();

        weightLayout = findViewById(R.id.kim_add_class_weight);
        final TextView nameView = findViewById(R.id.kim_add_class_class_name);
        final Spinner creditView = findViewById(R.id.kim_add_course_credits_dropdown);
        creditView.setSelection(0);

        final Spinner gradeSystemView = findViewById(R.id.kim_add_course_grading_system_dropdown);
        gradeSystemView.setSelection(0);

        KimCourse course = null;
        int index = 0;
        final String method = getIntent().getExtras().getString("method");
        final long courseId = getIntent().getExtras().getLong("course");

        if (method.equals("edit")) {
            setTitle("Edit Course");

            RealmResults<KimCourse> courses = realm.where(KimCourse.class).equalTo("id", courseId).findAll();
            RealmResults<KimWeight> weights = realm.where(KimWeight.class).equalTo("course.id", courseId).findAll();

            course = courses.get(0);

            nameView.setText(course.name);
            creditView.setSelection(course.credit - 1);
            gradeSystemView.setSelection(course.su ? 1 : 0);

            for (KimWeight o : weights) {
                LinearLayout textViewLayout = new LinearLayout(KimAddCourseActivity.this);
                textViewLayout.setOrientation(HORIZONTAL);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                textViewLayout.setLayoutParams(layoutParams);

                EditText weightNameView = new EditText(KimAddCourseActivity.this);
                LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                        300, 80
                );
                textViewParams.setMargins(50, 30, 0, 50);
                weightNameView.setText(((KimWeight) o).name);
                weightNameView.setLayoutParams(textViewParams);
                weightNameView.setHint("Weight Name");
                weightNameViews.add(weightNameView);

                EditText weightPercentView = new EditText(KimAddCourseActivity.this);
                LinearLayout.LayoutParams textViewParams2 = new LinearLayout.LayoutParams(
                        300, 80
                );
                textViewParams2.setMargins(0, 30, 50, 50);
                weightPercentView.setLayoutParams(textViewParams2);
                weightNameView.setHint("90");

                weightPercentView.setText(String.valueOf(((KimWeight) o).percent));
                weightPercentViews.add(weightPercentView);

                textViewLayout.addView(weightNameView);
                textViewLayout.addView(weightPercentView);

                weightLayout.addView(textViewLayout);
            }

        } else {
            setTitle("Add Course");
        }

        Button saveButton = findViewById(R.id.kim_add_course_save_button);
        final KimCourse finalCourse = course;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KimCourse c = null;
                if (method.equals("edit")) {
                    realm.beginTransaction();

                    finalCourse.credit = Integer.valueOf(String.valueOf(creditView.getSelectedItem()));
                    finalCourse.name = String.valueOf(nameView.getText());
                    finalCourse.su = gradeSystemView.getSelectedItemPosition() != 0;
                    c = finalCourse;

                    realm.copyToRealmOrUpdate(finalCourse);
                    realm.commitTransaction();
                } else {
                    realm.beginTransaction();

                    c = new KimCourse();
                    Number currentIdNum = realm.where(KimCourse.class).max("id");
                    long nextId;
                    if (currentIdNum == null) {
                        nextId = 1;
                    } else {
                        nextId = currentIdNum.intValue() + 1;
                    }
                    c.id = nextId;
                    c.name = String.valueOf(nameView.getText());
                    c.grade = "No Grade";
                    c.credit = Integer.valueOf(String.valueOf(creditView.getSelectedItem()));
                    c.su = gradeSystemView.getSelectedItemPosition() != 0;
                    realm.copyToRealmOrUpdate(c);
                    realm.commitTransaction();
                }

                realm.beginTransaction();
                final RealmResults<KimWeight> results = realm.where(KimWeight.class).equalTo("course.id", courseId).findAll();
                results.deleteAllFromRealm();
                realm.commitTransaction();

                for (int i = 0; i < weightNameViews.size(); i++) {
                    realm.beginTransaction();
                    EditText weightPercentView = weightPercentViews.get(i);
                    KimWeight weight = new KimWeight();
                    Number currentIdNum = realm.where(KimWeight.class).max("id");
                    long nextId;
                    if (currentIdNum == null) {
                        nextId = 1;
                    } else {
                        nextId = currentIdNum.intValue() + 1;
                    }
                    weight.id = nextId;
                    weight.name = String.valueOf(weightNameViews.get(i).getText());
                    weight.percent = Double.valueOf(String.valueOf(weightPercentView.getText()));
                    weight.course = c;
                    realm.copyToRealmOrUpdate(weight);
                    realm.commitTransaction();
                }

                setResult(RESULT_OK);
                finish();
            }
        });

        Button weightButton = findViewById(R.id.kim_add_course_add_weight_button);
        weightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout textViewLayout = new LinearLayout(KimAddCourseActivity.this);
                textViewLayout.setOrientation(HORIZONTAL);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                textViewLayout.setLayoutParams(layoutParams);

                EditText weightNameView = new EditText(KimAddCourseActivity.this);
                LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                        300, 80
                );
                textViewParams.setMargins(50, 30, 0, 50);
                weightNameView.setLayoutParams(textViewParams);
                weightNameView.setHint("Weight Name");

                weightNameViews.add(weightNameView);

                EditText weightPercentView = new EditText(KimAddCourseActivity.this);
                LinearLayout.LayoutParams textViewParams2 = new LinearLayout.LayoutParams(
                        300, 80
                );
                textViewParams2.setMargins(0, 30, 50, 50);
                weightPercentView.setLayoutParams(textViewParams2);
                weightPercentView.setHint("90");
                weightPercentViews.add(weightPercentView);

                textViewLayout.addView(weightNameView);
                textViewLayout.addView(weightPercentView);

                weightLayout.addView(textViewLayout);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
