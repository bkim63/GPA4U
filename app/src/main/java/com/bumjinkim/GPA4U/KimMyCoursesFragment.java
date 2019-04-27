package com.bumjinkim.GPA4U;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class KimMyCoursesFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private KimMyCourseAdapter myCourseAdapter;
    private int kim_add_course_request_code = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentKimMyCourses = inflater.inflate(R.layout.fragment_kim_my_courses, container, false);

        FloatingActionButton fab = fragmentKimMyCourses.findViewById(R.id.kim_add_course_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), KimAddCourseActivity.class);
                startActivityForResult(myIntent, kim_add_course_request_code);
            }
        });

        recyclerView = fragmentKimMyCourses.findViewById(R.id.kim_my_course_list_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        TinyDB tinyDB = new TinyDB(getContext());
        ArrayList<Object> courses = tinyDB.getListObject("courses", KimCourse.class);
        ArrayList<Object> weights = tinyDB.getListObject("weights", KimWeight.class);
        ArrayList<Object> assessments = tinyDB.getListObject("assessments", KimAssessment.class);

        myCourseAdapter = new KimMyCourseAdapter(this.getActivity(), courses, assessments, weights, recyclerView);
        recyclerView.setAdapter(myCourseAdapter);

        return fragmentKimMyCourses;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == kim_add_course_request_code) {
            if (resultCode == RESULT_OK) {
                TinyDB tinyDB = new TinyDB(getContext());
                ArrayList<Object> courses = tinyDB.getListObject("courses", KimCourse.class);

                myCourseAdapter.updateAdapter(courses);
            }
        }
    }
}
