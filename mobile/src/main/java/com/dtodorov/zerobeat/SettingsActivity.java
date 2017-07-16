package com.dtodorov.zerobeat;

import android.app.ActionBar;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

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

    public static class ZeroBeatPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            SeekBarPreference wpmPreference =
                    (SeekBarPreference) findPreference(getString(R.string.seekbar_wpm_key));
            wpmPreference.setMaxValue(60);
            wpmPreference.setMinValue(10);
            wpmPreference.setValue(15);

            SeekBarPreference frequencyPreference =
                    (SeekBarPreference) findPreference(getString(R.string.seekbar_frequency_key));
            frequencyPreference.setMaxValue(800);
            frequencyPreference.setMinValue(600);
            frequencyPreference.setValue(701);

        }
    }
}
