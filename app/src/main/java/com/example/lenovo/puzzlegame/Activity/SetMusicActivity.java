package com.example.lenovo.puzzlegame.Activity;

import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import com.example.lenovo.puzzlegame.Activity.AppCompatPreferenceActivity;
import android.util.Log;

import com.example.lenovo.puzzlegame.R;

import static com.example.lenovo.puzzlegame.Activity.HomeActivity.musictype;

public class SetMusicActivity extends AppCompatPreferenceActivity {

    private AppCompatDelegate mDelegate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_music);
        ListPreference test = (ListPreference)findPreference("example_list");
        test.setDefaultValue(-1);
//        test.setValue("-1");
        bindPreferenceSummaryToValue(findPreference("example_list"));
        setupActionBar();
    }
    @Override
        protected void onDestroy(){
        Log.w("进程销毁","设置音效页面activity销毁");
        super.onDestroy();

    }
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                Log.w("index","索引值----------"+index);

                HomeActivity.musictype=index;
                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);
                HomeActivity.soundPool.play(HomeActivity.soundMap.get(musictype), 1, 1, 0, 0, 1);

            }
            return true;
        }
    };

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

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}
