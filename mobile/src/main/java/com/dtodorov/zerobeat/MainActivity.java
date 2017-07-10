package com.dtodorov.zerobeat;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.AudioTrack;

import com.dtodorov.zerobeat.audio.morse.MorseTracker;
import com.dtodorov.zerobeat.audio.voice.VoiceTracker;
import com.dtodorov.zerobeat.teacher.Lessons;
import com.dtodorov.zerobeat.teacher.Teacher;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int wpm = 15;
        int sample_rate = 44100;
        int count = sample_rate / 2; // enough space for one PARIS
        String group;
        short[] sbuffer;
        byte[] bbuffer;
        AudioTrack track = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                sample_rate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8),
                AudioTrack.MODE_STREAM);
        track.play();

        Lessons lessons = new Lessons();
        Teacher teacher = new Teacher(lessons.getLesson(0), 5);
        MorseTracker morseTracker = new MorseTracker(wpm, 701, 44100, 1);
        VoiceTracker voiceTracker = new VoiceTracker(getResources());

        group = teacher.intro();

        sbuffer = morseTracker.track(group);
        track.write(sbuffer, 0, sbuffer.length);

        bbuffer = voiceTracker.track(group);
        track.write(bbuffer, 0, bbuffer.length);

        int i;
        for(i = 0; i < 20; i++)
        {
            group = teacher.group();

            sbuffer = morseTracker.track(group);
            track.write(sbuffer, 0, sbuffer.length);

            bbuffer = voiceTracker.track(group);
            track.write(bbuffer, 0, bbuffer.length);
        }
    }
}
