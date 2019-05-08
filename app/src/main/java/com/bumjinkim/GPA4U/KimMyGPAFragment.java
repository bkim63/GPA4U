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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;

public class KimMyGPAFragment extends Fragment {

    private int kim_show_settings_request_code = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kim_my_gpa, container, false);

        Realm realm = Realm.getDefaultInstance();
        final RealmResults<KimCourse> courses = realm.where(KimCourse.class).findAll();

        PieChart pieView = view.findViewById(R.id.pie_view);
        BarChart barView = view.findViewById(R.id.bar_view);

        if (courses.size() == 0) {
            pieView.setVisibility(View.INVISIBLE);
            barView.setVisibility(View.INVISIBLE);
        } else {
            pieView.setVisibility(View.VISIBLE);
            barView.setVisibility(View.VISIBLE);
        }

        Description descriptionBar = new Description();
        descriptionBar.setText("");

        pieView.setDescription(descriptionBar);

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (KimCourse course : courses) {
            entries.add(new PieEntry(Float.valueOf(String.valueOf(KimCalculateGrade.calculateCourseGPA(course, true))), course.name));
        }

        PieDataSet set = new PieDataSet(entries, "Courses");

        for (KimCourse course : courses) {
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            set.addColor(color);
        }

        PieData data = new PieData(set);
        pieView.setData(data);

        TextView textView = view.findViewById(R.id.text_view);
        textView.setText(String.format("GPA %.2f", KimCalculateGrade.calculateOverallGPA(new ArrayList<>(courses))));

        barView.setDescription(descriptionBar);

        ArrayList<String> courseNames = new ArrayList<>();
        for (KimCourse course : courses) {
            courseNames.add(course.name);
        }

        ArrayList<BarEntry> bottomTextList = new ArrayList<>();
        for (int i = 1; i <= courses.size(); i++) {
            bottomTextList.add(new BarEntry(i - 1, Float.valueOf(String.valueOf(KimCalculateGrade.calculateCourseGPA(courses.get(i - 1), true)))));
        }

        BarDataSet bottomTextListSet = new BarDataSet(bottomTextList, "Courses");
        for (KimCourse course : courses) {
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

            bottomTextListSet.addColor(color);
        }
        BarData barData = new BarData(bottomTextListSet);
        barView.setData(barData);
        barView.setFitBars(true);
        barView.setDrawValueAboveBar(false);
        barView.getAxisLeft().setValueFormatter(new PercentFormatter());
        barView.getAxisRight().setEnabled(false);
        barView.getAxisLeft().setAxisMinimum(0);

        barView.getXAxis().setValueFormatter(new IndexAxisValueFormatter(courseNames));
        barView.getXAxis().setCenterAxisLabels(false);
        barView.getXAxis().setLabelCount(courseNames.size());
        barView.getXAxis().setGranularityEnabled(false);

        return view;
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
