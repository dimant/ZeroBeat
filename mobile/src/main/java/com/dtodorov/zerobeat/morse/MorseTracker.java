package com.dtodorov.zerobeat.morse;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

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

    public AudioTrack generate(String text)
    {
        signalGenerator.setBuffer(null);
        encoder.encode(text);
        int count = signalGenerator.getWrittenCount();
        short[] buffer = new short[count];
        signalGenerator.setBuffer(buffer);
        encoder.encode(text);

        AudioTrack track = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                sample_rate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8),
                AudioTrack.MODE_STATIC);
        track.write(buffer, 0, count);

        return track;
    }
}
