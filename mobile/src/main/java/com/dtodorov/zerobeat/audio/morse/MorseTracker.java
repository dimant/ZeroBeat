package com.dtodorov.zerobeat.audio.morse;

/**
 * Created by diman on 7/9/2017.
 */

public class MorseTracker
{
    private int wpm;
    private int freqHz;
    private int sample_rate;
    private Encoder encoder;
    private SignalGenerator signalGenerator;
    public MorseTracker(int wpm, int freqHz, int sample_rate, int channels)
    {
        this.wpm = wpm;
        this.freqHz = freqHz;
        this.sample_rate = sample_rate;

        this.signalGenerator = new SignalGenerator(
                wpm,
                freqHz,
                sample_rate,
                channels); // mono

        this.encoder = new Encoder(this.signalGenerator);
    }

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
