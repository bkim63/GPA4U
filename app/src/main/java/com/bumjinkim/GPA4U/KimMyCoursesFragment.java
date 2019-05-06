package com.bumjinkim.GPA4U;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
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
    private NotificationManager notificationManager;

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

        boolean push = preferences.getBoolean("push", true);
        if (push) {
            sendGPAPush(courses);
            sendExpectedGPAPush(courses);
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
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(KimMyCoursesFragment.this.getContext());
                boolean push = preferences.getBoolean("push", true);
                if (push) {
                    sendGPAPush(courses);
                    sendExpectedGPAPush(courses);
                }

                gpaView.setText(String.format("GPA %.2f", KimCalculateGrade.calculateOverallGPA(new ArrayList<>(courses))));
                expectedGPAView.setText(String.format("Expected GPA %.2f", KimCalculateGrade.calculateOverallExpectedGPA(new ArrayList<>(courses))));
            }
        });

        assessments = realm.where(KimAssessment.class).findAll();
        assessments.addChangeListener(new RealmChangeListener<RealmResults<KimAssessment>>() {
            @Override
            public void onChange(RealmResults<KimAssessment> kimAssessments) {
                gpaView.setText(String.format("GPA %.2f", KimCalculateGrade.calculateOverallGPA(new ArrayList<>(courses))));
                expectedGPAView.setText(String.format("Expected GPA %.2f", KimCalculateGrade.calculateOverallExpectedGPA(new ArrayList<>(courses))));

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(KimMyCoursesFragment.this.getContext());
                boolean push = preferences.getBoolean("push", true);
                if (push) {
                    sendGPAPush(courses);
                    sendExpectedGPAPush(courses);
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

    private void sendGPAPush(RealmResults<KimCourse> courses) {
        if (KimCalculateGrade.calculateOverallGPA(new ArrayList<>(courses)) < 3.00) {
            int KIM_NOTIFICATION_ID = 0;

            String id = getContext().getString(R.string.kim_notification_channel_id);
            String title = getContext().getString(R.string.kim_notification_channel_title);

            Intent intent = null;
            PendingIntent pendingIntent = null;
            NotificationCompat.Builder builder = null;

            if (notificationManager == null) {
                notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = notificationManager.getNotificationChannel(id);

                if (notificationChannel == null) {
                    notificationChannel = new NotificationChannel(id, title, NotificationManager.IMPORTANCE_DEFAULT);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

                    notificationManager.createNotificationChannel(notificationChannel);
                }

                builder = new NotificationCompat.Builder(getContext(), id);
                intent = new Intent(getContext(), KimMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

                builder.setContentTitle("Low GPA")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentText(getContext().getString(R.string.app_name))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setTicker("Low GPA. Study until chair ripped off.")
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            } else {
                builder = new NotificationCompat.Builder(getContext(), id);
                intent = new Intent(getContext(), KimMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

                builder.setContentTitle("Low GPA")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentText(getContext().getString(R.string.app_name))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setTicker("Low GPA. Study until chair ripped off.")
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                        .setPriority(Notification.PRIORITY_HIGH);
            }
            Notification notification = builder.build();
            notificationManager.notify(KIM_NOTIFICATION_ID, notification);
        }
    }

    private void sendExpectedGPAPush(RealmResults<KimCourse> courses) {
        if (KimCalculateGrade.calculateOverallExpectedGPA(new ArrayList<>(courses)) < 3.00) {
            int KIM_NOTIFICATION_ID = 0;

            String id = getContext().getString(R.string.kim_notification_channel_id);
            String title = getContext().getString(R.string.kim_notification_channel_title);

            Intent intent = null;
            PendingIntent pendingIntent = null;
            NotificationCompat.Builder builder = null;

            if (notificationManager == null) {
                notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel notificationChannel = notificationManager.getNotificationChannel(id);

                if (notificationChannel == null) {
                    notificationChannel = new NotificationChannel(id, title, NotificationManager.IMPORTANCE_DEFAULT);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

                    notificationManager.createNotificationChannel(notificationChannel);
                }

                builder = new NotificationCompat.Builder(getContext(), id);

                intent = new Intent(getContext(), KimMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

                builder.setContentTitle("Low GPA")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentText(getContext().getString(R.string.app_name))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setTicker("Low Expected GPA. Study until chair ripped off.")
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            } else {
                builder = new NotificationCompat.Builder(getContext(), id);
                intent = new Intent(getContext(), KimMainActivity.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

                builder.setContentTitle("Low GPA")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentText(getContext().getString(R.string.app_name))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setTicker("Low Expected GPA. Study until chair ripped off.")
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                        .setPriority(Notification.PRIORITY_HIGH);
            }

            Notification notification = builder.build();
            notificationManager.notify(KIM_NOTIFICATION_ID, notification);
        }
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
