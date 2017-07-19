package com.dtodorov.zerobeat;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dtodorov.zerobeat.activities.SettingsActivity;
import com.dtodorov.zerobeat.audio.morse.ISignalGeneratorConfiguration;
import com.dtodorov.zerobeat.teacher.ITeacherConfiguration;

/**
 * Created by diman on 7/9/2017.
 */

public class Configuration implements ISignalGeneratorConfiguration, ITeacherConfiguration
{
    public static final int SAMPLING_RATE = 44100;
    public static final int CHANNELS = 1;

    public enum Course
    {
        Beginner,
        Intermediate,
        Advanced
    }

    private int wpm;
    private int samplingRate;
    private int frequency;
    private int groupSize;
    private int channels;

    @Override
    public int getGroupSize()
    {
        return this.groupSize;
    }

    @Override
    public int getWpm()
    {
        return wpm;
    }

    @Override
    public int getFrequency()
    {
        return frequency;
    }

    public void loadConfiguration(Application context)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        wpm = Math.round(sharedPref.getFloat(context.getString(R.string.seekbar_wpm_key), 15));

        frequency = Math.round(sharedPref.getFloat(context.getString(R.string.seekbar_frequency_key), 701));

        groupSize = sharedPref.getInt(context.getString(R.string.numberpicker_group_size_key), 5);
    }
}
