package com.bumjinkim.GPA4U;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class KimMyCoursesFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private KimMyCourseAdapter myCourseAdapter;
    private int kim_add_course_request_code = 1;

    private RealmResults<KimAssessment> assessments;
    private TextView gpaView;
    private TextView expectedGPAView;
    private FloatingActionButton fab;
    private RealmResults<KimCourse> courses;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentKimMyCourses = inflater.inflate(R.layout.fragment_kim_my_courses, container, false);

        fab = fragmentKimMyCourses.findViewById(R.id.kim_add_course_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), KimAddCourseActivity.class);
                myIntent.putExtra("method", "add");
                startActivityForResult(myIntent, kim_add_course_request_code);
            }
        });

        recyclerView = fragmentKimMyCourses.findViewById(R.id.kim_my_course_list_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        Realm realm = Realm.getDefaultInstance();
        courses = realm.where(KimCourse.class).findAll();

        gpaView = fragmentKimMyCourses.findViewById(R.id.kim_my_course_gpa_view);
        expectedGPAView = fragmentKimMyCourses.findViewById(R.id.kim_my_course_expected_gpa_view);
        gpaView.setText(String.format("GPA %.2f", KimCalculateGrade.calculateOverallGPA(new ArrayList<>(courses))));
        expectedGPAView.setText(String.format("Expected GPA %.2f", KimCalculateGrade.calculateOverallExpectedGPA(new ArrayList<>(courses))));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean showExpectedGPA = preferences.getBoolean("show_expected_gpa", true);
        if (showExpectedGPA) {
            expectedGPAView.setVisibility(View.VISIBLE);
        } else {
            expectedGPAView.setVisibility(View.INVISIBLE);
        }

        SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.d("SharedPreferences", key);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean showExpected = preferences.getBoolean("show_expected_gpa", true);
                if (showExpected) {
                    expectedGPAView.setVisibility(View.VISIBLE);
                } else {
                    expectedGPAView.setVisibility(View.INVISIBLE);
                }

                Realm realm = Realm.getDefaultInstance();
                RealmResults<KimCourse> courses = realm.where(KimCourse.class).findAll();

                myCourseAdapter.updateAdapter(new ArrayList<>(courses));
            }
        };
        preferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                gpaView.setText(String.format("GPA %.2f", KimCalculateGrade.calculateOverallGPA(new ArrayList<>(courses))));
                expectedGPAView.setText(String.format("Expected GPA %.2f", KimCalculateGrade.calculateOverallExpectedGPA(new ArrayList<>(courses))));

                RealmResults<KimCourse> courses = realm.where(KimCourse.class).findAll();
                myCourseAdapter.updateAdapter(new ArrayList<>(courses));
            }
        });

        assessments = realm.where(KimAssessment.class).findAll();
        assessments.addChangeListener(new RealmChangeListener<RealmResults<KimAssessment>>() {
            @Override
            public void onChange(RealmResults<KimAssessment> kimAssessments) {
                gpaView.setText(String.format("GPA %.2f", KimCalculateGrade.calculateOverallGPA(new ArrayList<>(courses))));
                expectedGPAView.setText(String.format("Expected GPA %.2f", KimCalculateGrade.calculateOverallExpectedGPA(new ArrayList<>(courses))));

                Realm realm = Realm.getDefaultInstance();
                RealmResults<KimCourse> courses = realm.where(KimCourse.class).findAll();

                myCourseAdapter.updateAdapter(new ArrayList<>(courses));
            }
        });

        myCourseAdapter = new KimMyCourseAdapter(this.getActivity(), new ArrayList<>(courses), recyclerView);
        recyclerView.setAdapter(myCourseAdapter);

        return fragmentKimMyCourses;
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean showExpected = preferences.getBoolean("show_expected_gpa", true);
        if (showExpected) {
            expectedGPAView.setVisibility(View.VISIBLE);
        } else {
            expectedGPAView.setVisibility(View.INVISIBLE);
        }

        Realm realm = Realm.getDefaultInstance();
        RealmResults<KimCourse> courses = realm.where(KimCourse.class).findAll();

        myCourseAdapter.updateAdapter(new ArrayList<>(courses));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == kim_add_course_request_code) {
//            if (resultCode == RESULT_OK) {
//                Log.d("MY COURSES", "UPDATING COURSES");
//
//                Realm realm = Realm.getDefaultInstance();
//                RealmResults<KimCourse> courses = realm.where(KimCourse.class).findAll();
//
//                myCourseAdapter.updateAdapter(new ArrayList<>(courses));
//            }
//        }
    }
}
