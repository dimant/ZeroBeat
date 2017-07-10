package com.dtodorov.zerobeat.teacher;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.dtodorov.zerobeat.Configuration;
import com.dtodorov.zerobeat.audio.morse.MorseTracker;
import com.dtodorov.zerobeat.audio.voice.PhoneticTracker;

import java.util.ArrayList;

/**
 * Created by diman on 7/9/2017.
 */

public class School implements ISchool
{
    private boolean teaching;
    private Lessons lessons;
    private Teacher teacher;
    private MorseTracker morseTracker;
    private PhoneticTracker phoneticTracker;
    private Configuration configuration;
    private int lesson;

    public School(
            Lessons lessons,
            Teacher teacher,
            MorseTracker morseTracker,
            PhoneticTracker phoneticTracker,
            Configuration configuration
    )
    {
        this.lessons = lessons;
        this.teacher = teacher;
        this.morseTracker = morseTracker;
        this.phoneticTracker = phoneticTracker;
        this.configuration = configuration;
    }

    @Override
    public ArrayList<String> getLessons()
    {
        return lessons.getLessons();
    }

    @Override
    public void setLesson(int lesson)
    {
        this.lesson = lesson;
    }

    public void start()
    {

    }

    @Override
    public void stop()
    {
        teaching = false;
    }

    @Override
    public void run()
    {
        String group;
        short[] sbuffer;
        byte[] bbuffer;
        int count = configuration.getSamplingRate() * 60 / configuration.getWpm(); // enough space for one PARIS
        AudioTrack track = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                configuration.getSamplingRate(),
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8),
                AudioTrack.MODE_STREAM);
        track.play();

        group = teacher.intro(lessons.getLesson(lesson));

        sbuffer = morseTracker.track(group);
        track.write(sbuffer, 0, sbuffer.length);

        bbuffer = phoneticTracker.track(group);
        track.write(bbuffer, 0, bbuffer.length);

        teaching = true;
        while(teaching)
        {
            group = teacher.group(lessons.getLesson(lesson));

            sbuffer = morseTracker.track(group);
            track.write(sbuffer, 0, sbuffer.length);

            bbuffer = phoneticTracker.track(group);
            track.write(bbuffer, 0, bbuffer.length);
        }
    }
}
