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
    private final AudioTrack track;
    private Lessons lessons;
    private Teacher teacher;
    private MorseTracker morseTracker;
    private PhoneticTracker phoneticTracker;
    private int lesson;

    public School(
            Lessons lessons,
            Teacher teacher,
            MorseTracker morseTracker,
            PhoneticTracker phoneticTracker
    )
    {
        this.lessons = lessons;
        this.teacher = teacher;
        this.morseTracker = morseTracker;
        this.phoneticTracker = phoneticTracker;

        int count = Configuration.SAMPLING_RATE * Configuration.CHANNELS * 60 / 10; // enough space for one PARIS at 10wpm
        track = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                Configuration.SAMPLING_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8),
                AudioTrack.MODE_STREAM);
    }

    @Override
    public ArrayList<String> getLessons()
    {
        return lessons.getLessons();
    }

    @Override
    public void setLesson(int lesson)
    {
        track.flush();
        this.lesson = lesson;
    }

    @Override
    public int getLesson()
    {
        return this.lesson;
    }

    @Override
    public void stop()
    {
        if(track.getPlayState() != AudioTrack.PLAYSTATE_STOPPED)
        {
            track.stop();
            track.flush();
        }
    }

    @Override
    public boolean isPlaying()
    {
        return track.getPlayState() == AudioTrack.PLAYSTATE_PLAYING;
    }

    @Override
    public void play()
    {
        if(track.getPlayState() != AudioTrack.PLAYSTATE_PLAYING)
        {
            track.play();
        }
    }

    @Override
    public void run()
    {
        String group;
        short[] sbuffer;
        byte[] bbuffer;

        group = teacher.intro(lessons.getLesson(lesson));
        if(Thread.currentThread().isInterrupted())
            return;

        sbuffer = morseTracker.track(group);
        if(Thread.currentThread().isInterrupted())
            return;

        track.write(sbuffer, 0, sbuffer.length);
        if(Thread.currentThread().isInterrupted())
            return;

        bbuffer = phoneticTracker.track(group);
        if(Thread.currentThread().isInterrupted())
            return;

        track.write(bbuffer, 0, bbuffer.length);
        if(Thread.currentThread().isInterrupted())
            return;

        while(true)
        {
            group = teacher.group(lessons.getLesson(lesson));
            if(Thread.currentThread().isInterrupted())
                return;

            sbuffer = morseTracker.track(group);
            if(Thread.currentThread().isInterrupted())
                return;

            track.write(sbuffer, 0, sbuffer.length);
            if(Thread.currentThread().isInterrupted())
                return;

            bbuffer = phoneticTracker.track(group);
            if(Thread.currentThread().isInterrupted())
                return;

            track.write(bbuffer, 0, bbuffer.length);
            if(Thread.currentThread().isInterrupted())
                return;
        }
    }
}
