package com.dtodorov.zerobeat.audio.morse;

/**
 * Created by diman on 7/9/2017.
 */

public class MorseTracker
{
    private Encoder encoder;
    private SignalGenerator signalGenerator;

    public MorseTracker(SignalGenerator signalGenerator)
    {
        this.signalGenerator = signalGenerator;

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
