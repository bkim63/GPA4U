package com.bumjinkim.GPA4U;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class KimSettingsActivity extends AppCompatPreferenceActivity {

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            // For all other preferences, set the summary to the value's
            // simple string representation.
            preference.setSummary(stringValue);

            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        addPreferencesFromResource(R.xml.pref_general);

        this.getFragmentManager().beginTransaction().replace(android.R.id.content, new GeneralPreferenceFragment()).commit();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(KimSettingsActivity.this);

        final SwitchPreference soundNotificationsSwitch = (SwitchPreference) findPreference("all_notifications");
        soundNotificationsSwitch.setChecked(preferences.getBoolean("all_notifications", true));

        soundNotificationsSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(KimSettingsActivity.this);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putBoolean("all_notifications", soundNotificationsSwitch.isChecked());
                editor.apply();

                return false;
            }
        });

        final SwitchPreference lowGPASwitch = (SwitchPreference) findPreference("gpa_notifications");
        lowGPASwitch.setChecked(preferences.getBoolean("gpa_notifications", true));

        lowGPASwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(KimSettingsActivity.this);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putBoolean("gpa_notifications", lowGPASwitch.isChecked());
                editor.apply();

                return false;
            }
        });

        final SwitchPreference lowExpectedGPASwitch = (SwitchPreference) findPreference("expected_gpa_notifications");
        lowExpectedGPASwitch.setChecked(preferences.getBoolean("expected_gpa_notifications", true));

        lowExpectedGPASwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(KimSettingsActivity.this);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putBoolean("expected_gpa_notifications", lowExpectedGPASwitch.isChecked());
                editor.apply();

                return false;
            }
        });

        final SwitchPreference expectedGPASwitch = (SwitchPreference) findPreference("show_expected_gpa");
        expectedGPASwitch.setChecked(preferences.getBoolean("show_expected_gpa", true));

        expectedGPASwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(KimSettingsActivity.this);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putBoolean("show_expected_gpa", expectedGPASwitch.isChecked());
                editor.apply();

                return false;
            }
        });

        final SwitchPreference decimalPercentSwitch = (SwitchPreference) findPreference("show_decimal_percent");
        decimalPercentSwitch.setChecked(preferences.getBoolean("show_decimal_percent", true));

        decimalPercentSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(KimSettingsActivity.this);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putBoolean("show_decimal_percent", decimalPercentSwitch.isChecked());
                editor.apply();

                return false;
            }
        });

        final SwitchPreference creditSwitch = (SwitchPreference) findPreference("show_credits");
        creditSwitch.setChecked(preferences.getBoolean("show_credits", true));

        creditSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(KimSettingsActivity.this);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putBoolean("show_credits", creditSwitch.isChecked());
                editor.apply();

                return false;
            }
        });
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
//            bindPreferenceSummaryToValue(findPreference("your_key"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                getActivity().onBackPressed();
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), KimSettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), KimSettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
