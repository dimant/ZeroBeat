package com.dtodorov.zerobeat.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.dtodorov.zerobeat.Configuration;
import com.dtodorov.zerobeat.R;
import com.dtodorov.zerobeat.ZeroBeatApplication;

import de.mrapp.android.preference.NumberPickerPreference;
import de.mrapp.android.preference.SeekBarPreference;

public class SettingsActivity extends AppCompatActivity
{
    public static final int REQUEST_CODE_PREFERENCES = 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new ZeroBeatPreferenceFragment()).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class ZeroBeatPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            ZeroBeatApplication app = (ZeroBeatApplication) getActivity().getApplication();
            Configuration configuration = app.getConfiguration();
            configuration.loadConfiguration(app);

            NumberPickerPreference wpmPreference =
                    (NumberPickerPreference) findPreference(getString(R.string.numberpicker_wpm_key));
            wpmPreference.setMaxNumber(60);
            wpmPreference.setMinNumber(10);
            wpmPreference.setNumber(configuration.getWpm());

            NumberPickerPreference farnsworthWpmPreference =
                    (NumberPickerPreference) findPreference(getString(R.string.numberpicker_farnsworth_wpm_key));
            farnsworthWpmPreference.setMaxNumber(60);
            farnsworthWpmPreference.setMinNumber(5);
            farnsworthWpmPreference.setNumber(configuration.getFarnsworthWpm());

            SeekBarPreference frequencyPreference =
                    (SeekBarPreference) findPreference(getString(R.string.seekbar_frequency_key));
            frequencyPreference.setMaxValue(800);
            frequencyPreference.setMinValue(600);
            frequencyPreference.setValue(configuration.getFrequency());

            NumberPickerPreference groupSizePreference =
                    (NumberPickerPreference) findPreference(getString(R.string.numberpicker_group_size_key));
            groupSizePreference.setMaxNumber(7);
            groupSizePreference.setMinNumber(2);
            groupSizePreference.setNumber(configuration.getGroupSize());

            SeekBarPreference noisePreference =
                    (SeekBarPreference) findPreference(getString(R.string.seekbar_noise_key));
            noisePreference.setMaxValue(100);
            noisePreference.setMinValue(0);
            noisePreference.setValue(configuration.getNoiseLevel());
        }
    }
}
