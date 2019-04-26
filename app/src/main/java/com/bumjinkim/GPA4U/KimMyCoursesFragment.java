package com.bumjinkim.GPA4U;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class KimMyCoursesFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private KimMyCourseAdapter myCourseAdapter;

    private String[] names = new String[]{"UIMA", "Intro to Psychology", "Abnormal Psychology", "Psychology", "Random Class", "Random Class", "Random Class", "Random Class"};
    private String[] grades = new String[]{"A", "A", "A", "A", "A", "A", "A", "A"};
    private int[] credits = new int[]{3, 2, 3, 3, 2, 3, 3, 3};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentKimMyCourses = inflater.inflate(R.layout.fragment_kim_my_courses, container, false);

        FloatingActionButton fab = fragmentKimMyCourses.findViewById(R.id.kim_add_course_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), KimAddCourseActivity.class);
                getActivity().startActivity(myIntent);
            }
        });

        recyclerView = fragmentKimMyCourses.findViewById(R.id.kim_my_course_list_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        myCourseAdapter = new KimMyCourseAdapter(this.getActivity(), names, grades, credits);
        recyclerView.setAdapter(myCourseAdapter);

        return fragmentKimMyCourses;
    }

}
