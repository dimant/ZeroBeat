package com.dtodorov.zerobeat;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import de.mrapp.android.preference.NumberPickerPreference;
import de.mrapp.android.preference.SeekBarPreference;

public class SettingsActivity extends AppCompatActivity
{

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

    public static int getWpm(Activity context)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return Math.round(sharedPref.getFloat(context.getString(R.string.seekbar_wpm_key), 15));
    }

    public static int getFrequency(Activity context)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return Math.round(sharedPref.getFloat(context.getString(R.string.seekbar_frequency_key), 701));
    }

    public static int getGroupSize(Activity context)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getInt(context.getString(R.string.numberpicker_group_size_key), 5);
    }

    public static class ZeroBeatPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            Activity context = getActivity();

            SeekBarPreference wpmPreference =
                    (SeekBarPreference) findPreference(getString(R.string.seekbar_wpm_key));
            wpmPreference.setMaxValue(60);
            wpmPreference.setMinValue(10);
            wpmPreference.setValue(getWpm(context));

            SeekBarPreference frequencyPreference =
                    (SeekBarPreference) findPreference(getString(R.string.seekbar_frequency_key));
            frequencyPreference.setMaxValue(800);
            frequencyPreference.setMinValue(600);
            frequencyPreference.setValue(getFrequency(context));

            NumberPickerPreference groupSizePreference =
                    (NumberPickerPreference) findPreference(getString(R.string.numberpicker_group_size_key));
            groupSizePreference.setMaxNumber(7);
            groupSizePreference.setMinNumber(2);
            groupSizePreference.setNumber(getGroupSize(context));
        }
    }
}
