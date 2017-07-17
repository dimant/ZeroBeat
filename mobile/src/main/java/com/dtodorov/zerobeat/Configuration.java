package com.dtodorov.zerobeat;

import com.dtodorov.zerobeat.audio.morse.ISignalGeneratorConfiguration;
import com.dtodorov.zerobeat.teacher.ITeacherConfiguration;

/**
 * Created by diman on 7/9/2017.
 */

public class Configuration implements ISignalGeneratorConfiguration, ITeacherConfiguration
{
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

    public void setWpm(int wpm)
    {
        this.wpm = wpm;
    }

    public void setSamplingRate(int samplingRate)
    {
        this.samplingRate = samplingRate;
    }

    public void setFrequency(int frequency)
    {
        this.frequency = frequency;
    }

    public void setGroupSize(int groupSize)
    {
        this.groupSize = groupSize;
    }

    public void setChannels(int channels)
    {
        this.channels = channels;
    }

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
    public int getSamplingRate()
    {
        return samplingRate;
    }

    @Override
    public int getFrequency()
    {
        return frequency;
    }

    @Override
    public int getChannels()
    {
        return channels;
    }
}
