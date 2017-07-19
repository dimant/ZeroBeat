package com.dtodorov.zerobeat.audio.morse;

import com.dtodorov.zerobeat.Configuration;

/**
 * Created by diman on 7/8/2017.
 */

public class SignalGenerator implements ISignalGenerator
{
    // dot duration in ms for the word PARIS at 20 wpm is 50 ms
    // from this: dot duration = 20 * 50 / desired wpm
    private static final double DOT_STANDARD = 20 * 50;

    private ISignalGeneratorConfiguration configuration;

    private short[] buffer;
    int offset;

    public SignalGenerator(ISignalGeneratorConfiguration configuration)
    {
        this.configuration = configuration;
    }

    public double getDotDuration()
    {
        return DOT_STANDARD / configuration.getWpm();
    }

    public double getDashDuration()
    {
        return 3 * getDotDuration();
    }

    public int getDotSamples()
    {
        return (int) Math.round(Configuration.SAMPLING_RATE * getDotDuration() / 1000);
    }

    public int getDashSamples()
    {
        return (int) Math.round(Configuration.SAMPLING_RATE * getDashDuration() / 1000);
    }

    public void setBuffer(short[] buffer)
    {
        this.buffer = buffer;
        this.offset = 0;
    }

    public int getWrittenCount()
    {
        return this.offset;
    }

    public void writeSignal(short[] data, long samples)
    {
        int i;
        int j;
        short sample;

        for(i = 0; i < samples; i++)
        {
            sample = (short)(Math.sin(2 * Math.PI * i / (Configuration.SAMPLING_RATE / configuration.getFrequency())) * 0x7FFF);

            for(j = 0; j < Configuration.CHANNELS; j++)
            {
                if(data != null)
                    data[offset] = sample;
                offset++;
            }
        }
    }

    public void writeSilence(short[] data, long samples)
    {
        int i;
        int j;
        short sample = 0;

        for(i = 0; i < samples; i++)
        {
            for(j = 0; j < Configuration.CHANNELS; j++)
            {
                if(data != null)
                    data[offset] = sample;
                offset++;
            }
        }
    }

    @Override
    public void writeDash()
    {
        writeSignal(this.buffer, getDashSamples());
    }

    @Override
    public void writeDot()
    {
        writeSignal(this.buffer, getDotSamples());
    }

    @Override
    public void writeSymbolSpace()
    {
        writeSilence(this.buffer, getDotSamples());
    }

    @Override
    public void writeWordSpace()
    {
        writeSilence(this.buffer, 2 * getDashSamples());
    }
}
