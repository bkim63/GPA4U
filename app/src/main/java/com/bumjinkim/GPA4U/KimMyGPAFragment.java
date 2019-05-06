package com.bumjinkim.GPA4U;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import im.dacer.androidcharts.BarView;
import im.dacer.androidcharts.PieHelper;
import im.dacer.androidcharts.PieView;
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

        PieView pieView = fragmentGPA.findViewById(R.id.pie_view);
        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<>();

        double totalGrades = 0.0;
        for (KimCourse course : courses) {
            totalGrades += Double.valueOf(course.grade);
        }

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        for (KimCourse course : courses) {
            pieHelperArrayList.add(new PieHelper(Float.valueOf(course.grade) / Float.valueOf(String.valueOf(totalGrades)), color));
        }

        pieView.setDate(pieHelperArrayList);
        pieView.selectedPie(2);
        pieView.showPercentLabel(true);

        TextView textView = fragmentGPA.findViewById(R.id.text_view);
        textView.setText(String.format("GPA %s", KimCalculateGrade.calculateOverallGPA(new ArrayList<>(courses))));

        BarView barView = fragmentGPA.findViewById(R.id.bar_view);

        ArrayList<String> bottomTextList = new ArrayList<>();
        ArrayList<Integer> dataList = new ArrayList<>();

        for (KimCourse course : courses) {
            bottomTextList.add(course.name);
            bottomTextList.add(course.grade);
        }
        barView.setBottomTextList(bottomTextList);
        barView.setDataList(dataList,100);

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
