package com.bumjinkim.GPA4U;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;

public class KimMyGPAFragment extends Fragment {

    private int kim_show_settings_request_code = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentGPA = inflater.inflate(R.layout.fragment_kim_my_gpa, container, false);

        Realm realm = Realm.getDefaultInstance();
        final RealmResults<KimCourse> courses = realm.where(KimCourse.class).findAll();

//        TextView gpaView = fragmentGPA.findViewById(R.id.kim_my_course_gpa_view);
//        TextView expectedGPAView = fragmentGPA.findViewById(R.id.kim_my_course_expected_gpa_view);
//        gpaView.setText(String.format("GPA %.2f", KimCalculateGrade.calculateOverallGPA(new ArrayList<KimCourse>(courses))));
//        expectedGPAView.setText(String.format("Expected GPA %.2f", KimCalculateGrade.calculateOverallExpectedGPA(new ArrayList<KimCourse>(courses))));

        return fragmentGPA;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == kim_show_settings_request_code) {
            if (resultCode == RESULT_OK) {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<KimCourse> courses = realm.where(KimCourse.class).findAll();
            }
        }
    }
}
