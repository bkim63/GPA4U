package com.bumjinkim.GPA4U;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class KimMainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = new KimMyCoursesFragment();

            switch (item.getItemId()) {
                case R.id.navigation_my_courses:
                    fragment = new KimMyCoursesFragment();
                    return true;
                case R.id.navigation_my_gpa:
                    fragment = new KimMyGPAFragment();
                    return true;
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_fragment, fragment);
            transaction.addToBackStack(null);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();

            return false;
        }
    };

    private int kim_settings_request_code = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_kim_main);

        setTitle(R.string.title_activity_kim_my_courses);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Settings").setIcon(R.drawable.ic_info_black_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Intent myIntent = new Intent(KimMainActivity.this, KimSettingsActivity.class);
//                myIntent.putExtra("method", "add");
//                myIntent.putExtra("course", getIntent().getExtras().getLong("course"));
                startActivityForResult(myIntent, kim_settings_request_code);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
