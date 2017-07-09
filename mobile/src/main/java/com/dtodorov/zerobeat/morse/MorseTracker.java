package com.dtodorov.zerobeat.morse;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by diman on 7/9/2017.
 */

public class MorseTracker implements ITracker
{
    private int wpm;
    private int freqHz;
    private int sample_rate;
    private Encoder encoder;
    private SignalGenerator signalGenerator;
    public MorseTracker(int wpm, int freqHz, int sample_rate)
    {
        this.wpm = wpm;
        this.freqHz = freqHz;
        this.sample_rate = sample_rate;

        this.signalGenerator = new SignalGenerator(
                wpm,
                freqHz,
                sample_rate,
                2); // Stereo

        this.encoder = new Encoder(this.signalGenerator);
    }

    @Override
    public short[] track(String text)
    {
        signalGenerator.setBuffer(null);
        encoder.encode(text);
        int count = signalGenerator.getWrittenCount();
        short[] buffer = new short[count];
        signalGenerator.setBuffer(buffer);
        encoder.encode(text);

        return buffer;
    }
}
