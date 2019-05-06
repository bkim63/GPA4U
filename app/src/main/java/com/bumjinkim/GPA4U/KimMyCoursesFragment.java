package com.bumjinkim.GPA4U;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;

public class KimMyCoursesFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private KimMyCourseAdapter myCourseAdapter;
    private int kim_add_course_request_code = 1;

    private RealmResults<KimAssessment> assessments;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentKimMyCourses = inflater.inflate(R.layout.fragment_kim_my_courses, container, false);

        FloatingActionButton fab = fragmentKimMyCourses.findViewById(R.id.kim_add_course_button);
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
        final RealmResults<KimCourse> courses = realm.where(KimCourse.class).findAll();

        final TextView gpaView = fragmentKimMyCourses.findViewById(R.id.kim_my_course_gpa_view);
        final TextView expectedGPAView = fragmentKimMyCourses.findViewById(R.id.kim_my_course_expected_gpa_view);
        gpaView.setText(String.format("GPA %.2f", KimCalculateGrade.calculateOverallGPA(new ArrayList<>(courses))));
        expectedGPAView.setText(String.format("Expected GPA %.2f", KimCalculateGrade.calculateOverallExpectedGPA(new ArrayList<>(courses))));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        boolean expected = preferences.getBoolean("expected", true);
        if (expected) {
            expectedGPAView.setVisibility(View.VISIBLE);
        } else {
            expectedGPAView.setVisibility(View.INVISIBLE);
        }

        SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(KimMyCoursesFragment.this.getContext());
                boolean expected = preferences.getBoolean("expected", true);
                if (expected) {
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

                if (KimCalculateGrade.calculateOverallGPA(new ArrayList<>(courses)) < 3.00) {
                    Intent intent = new Intent(getContext(), KimMyCoursesFragment.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());

                    builder.setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setTicker("GPA4U")
                            .setContentTitle("GPA is low right now.")
                            .setContentText("GPA is currently below 3.00. Study until hip gets ripped off.")
                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                            .setContentIntent(contentIntent)
                            .setContentInfo("Info");

                    NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1, builder.build());
                }

                if (KimCalculateGrade.calculateOverallExpectedGPA(new ArrayList<>(courses)) < 3.00) {
                    Intent intent = new Intent(getContext(), KimMyCoursesFragment.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());

                    builder.setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setTicker("GPA4U")
                            .setContentTitle("Expected GPA is low right now.")
                            .setContentText("Expected GPA is currently below 3.00. Study until hip gets ripped off.")
                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                            .setContentIntent(contentIntent)
                            .setContentInfo("Info");

                    NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1, builder.build());
                }
            }
        });

        assessments = realm.where(KimAssessment.class).findAll();
        assessments.addChangeListener(new RealmChangeListener<RealmResults<KimAssessment>>() {
            @Override
            public void onChange(RealmResults<KimAssessment> kimAssessments) {
                gpaView.setText(String.format("GPA %.2f", KimCalculateGrade.calculateOverallGPA(new ArrayList<>(courses))));
                expectedGPAView.setText(String.format("Expected GPA %.2f", KimCalculateGrade.calculateOverallExpectedGPA(new ArrayList<>(courses))));

                if (KimCalculateGrade.calculateOverallGPA(new ArrayList<>(courses)) < 3.00) {
                    Intent intent = new Intent(getContext(), KimMyCoursesFragment.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());

                    builder.setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setTicker("GPA4U")
                            .setContentTitle("GPA is low right now.")
                            .setContentText("GPA is currently below 3.00. Study until hip gets ripped off.")
                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                            .setContentIntent(contentIntent)
                            .setContentInfo("Info");

                    NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1, builder.build());
                }

                if (KimCalculateGrade.calculateOverallGPA(new ArrayList<>(courses)) < 3.00) {
                    Intent intent = new Intent(getContext(), KimMyCoursesFragment.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());

                    builder.setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setTicker("GPA4U")
                            .setContentTitle("Expected GPA is low right now.")
                            .setContentText("Expected GPA is currently below 3.00. Study until hip gets ripped off.")
                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                            .setContentIntent(contentIntent)
                            .setContentInfo("Info");

                    NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1, builder.build());
                }

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == kim_add_course_request_code) {
            if (resultCode == RESULT_OK) {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<KimCourse> courses = realm.where(KimCourse.class).findAll();

                myCourseAdapter.updateAdapter(new ArrayList<>(courses));
            }
        }
    }
}
