package com.dtodorov.zerobeat;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dtodorov.zerobeat.activities.SettingsActivity;
import com.dtodorov.zerobeat.audio.morse.ISignalGeneratorConfiguration;
import com.dtodorov.zerobeat.teacher.ISchoolConfiguration;
import com.dtodorov.zerobeat.teacher.ITeacherConfiguration;

/**
 * Created by diman on 7/9/2017.
 */

public class Configuration implements ISignalGeneratorConfiguration, ITeacherConfiguration, ISchoolConfiguration
{
    public static final int SAMPLING_RATE = 44100;
    public static final int CHANNELS = 1;

    public enum CourseLevel
    {
        Beginner,
        Intermediate,
        Advanced,
        SendingPractice
    }

    private int wpm;
    private int farnsworthWpm;
    private int frequency;
    private int groupSize;
    private float noiseLevel;
    private CourseLevel courseLevel;

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
    public int getFarnsworthWpm() { return farnsworthWpm; }

    @Override
    public int getFrequency()
    {
        return frequency;
    }

    @Override
    public float getNoiseLevel()
    {
        return noiseLevel;
    }

    @Override
    public CourseLevel getCourseLevel()
    {
        return courseLevel;
    }

    public void setCourseLevel(String courseLevelString)
    {
        courseLevel = CourseLevel.valueOf(courseLevelString);
    }

    public void loadConfiguration(Application context)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        wpm = sharedPref.getInt(context.getString(R.string.numberpicker_wpm_key), 15);

        farnsworthWpm = sharedPref.getInt(context.getString(R.string.numberpicker_farnsworth_wpm_key), 5);

        frequency = Math.round(sharedPref.getFloat(context.getString(R.string.seekbar_frequency_key), 701));

        groupSize = sharedPref.getInt(context.getString(R.string.numberpicker_group_size_key), 5);

        noiseLevel = sharedPref.getFloat(context.getString(R.string.seekbar_noise_key), 0.0f);
    }
}
