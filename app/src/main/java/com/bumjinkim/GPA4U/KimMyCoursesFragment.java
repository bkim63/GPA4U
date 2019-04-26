package com.bumjinkim.GPA4U;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

        recyclerView = fragmentKimMyCourses.findViewById(R.id.kim_my_course_list_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        myCourseAdapter = new KimMyCourseAdapter(this.getActivity(), names, grades, credits);
        recyclerView.setAdapter(myCourseAdapter);

        return fragmentKimMyCourses;
    }

}
