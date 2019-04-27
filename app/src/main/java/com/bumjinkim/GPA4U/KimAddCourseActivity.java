package com.bumjinkim.GPA4U;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kim_add_course);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        weightLayout = findViewById(R.id.kim_add_class_weight);
        final TextView nameView = findViewById(R.id.kim_add_class_class_name);

        Button saveButton = findViewById(R.id.kim_add_course_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TinyDB tinyDB = new TinyDB(KimAddCourseActivity.this);
                ArrayList<Object> courses = tinyDB.getListObject("courses", Course.class);

                Course course = new Course(String.valueOf(nameView.getText()), "No Grade", 3);
                courses.add(course);

                tinyDB.putListObject("courses", courses);

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

                TextView weightNameView = new TextView(KimAddCourseActivity.this);
                TextView weightPercentView = new TextView(KimAddCourseActivity.this);

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
