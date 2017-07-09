package com.dtodorov.zerobeat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.AudioTrack;
import android.media.AudioFormat;
import android.media.AudioManager;

import com.dtodorov.zerobeat.morse.MorseTracker;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MorseTracker tracker = new MorseTracker(15, 768, 44100);
        AudioTrack track = tracker.generate("sos sos sos a a a ab ab ab");
        track.play();
    }

    private AudioTrack generateTone(double freqHz, int durationMs)
    {
        double sample_size = 44100.0 * 2.0;
        int count = (int)(sample_size * (durationMs / 1000.0)) & ~1;
        short[] samples = new short[count];
        double spacing = sample_size / 2;
        boolean signal = true;
        for(int i = 0; i < count; i += 2){
            short sample = 0;

            if(i % (int) spacing == 0)
                signal = !signal;

            if(signal)
                sample = (short)(Math.sin(2 * Math.PI * i / (44100.0 / freqHz)) * 0x7FFF);
            else
                sample = 0;

            samples[i + 0] = sample;
            samples[i + 1] = sample;
        }
        AudioTrack track = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                44100,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8),
                AudioTrack.MODE_STATIC);
        track.write(samples, 0, count);
        return track;
    }
}
