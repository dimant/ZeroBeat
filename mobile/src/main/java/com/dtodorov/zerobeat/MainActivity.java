package com.dtodorov.zerobeat;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.AudioTrack;

import com.dtodorov.zerobeat.morse.MorseTracker;
import com.dtodorov.zerobeat.teacher.Lessons;
import com.dtodorov.zerobeat.teacher.Teacher;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int sample_rate = 44100;
        int count = sample_rate * 10 * 2; // 10 seconds of stereo should be enough for now
        String group;
        short[] buffer;
        AudioTrack track = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                sample_rate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8),
                AudioTrack.MODE_STATIC);

        Lessons lessons = new Lessons();
        Teacher teacher = new Teacher(lessons.getLesson(0), 5);
        MorseTracker morseTracker = new MorseTracker(13, 701, 44100);

        group = teacher.intro();
        buffer = morseTracker.track(group);
        track.write(buffer, 0, buffer.length);
        track.play();

        int i;
        for(i = 0; i < 20; i++)
        {
            group = teacher.group();
            buffer = morseTracker.track(group);
            track.write(buffer, 0, buffer.length);
        }
    }
}
