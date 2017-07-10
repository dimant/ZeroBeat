package com.dtodorov.zerobeat.audio.morse;

/**
 * Created by diman on 7/8/2017.
 */

public class SignalGenerator implements ISignalGenerator
{
    // dot duration in ms for the word PARIS at 20 wpm is 50 ms
    // from this: dot duration = 20 * 50 / desired wpm
    private static final double DOT_STANDARD = 20 * 50;

    private int dotSamples;
    private int dashSamples;

    private int sample_rate;
    private int channels;
    private int freqHz;

    private short[] buffer;
    int offset;

    public SignalGenerator(int wpm, int freqHz, int sample_rate, int channels)
    {
        this.sample_rate = sample_rate;
        this.channels = channels;
        this.freqHz = freqHz;

        double samples_per_ms = sample_rate / 1000.0;

        double dotDuration = DOT_STANDARD / wpm;
        double dashDuration = 3 * dotDuration;

        this.dotSamples = (int) Math.round(samples_per_ms * dotDuration);
        this.dashSamples = (int) Math.round(samples_per_ms * dashDuration);
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
            sample = (short)(Math.sin(2 * Math.PI * i / (this.sample_rate / this.freqHz)) * 0x7FFF);

            for(j = 0; j < channels; j++)
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
            for(j = 0; j < channels; j++)
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
        writeSignal(this.buffer, this.dashSamples);
    }

    @Override
    public void writeDot()
    {
        writeSignal(this.buffer, this.dotSamples);
    }

    @Override
    public void writeSymbolSpace()
    {
        writeSilence(this.buffer, this.dotSamples);
    }

    @Override
    public void writeWordSpace()
    {
        writeSilence(this.buffer, 2 * this.dashSamples);
    }
}
