package com.dtodorov.zerobeat.teacher;

import android.content.res.TypedArray;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.dtodorov.zerobeat.Configuration;
import com.dtodorov.zerobeat.audio.NoiseMixer;
import com.dtodorov.zerobeat.audio.morse.MorseTracker;
import com.dtodorov.zerobeat.audio.PhoneticTracker;
import com.dtodorov.zerobeat.models.LessonModel;

import java.util.ArrayList;

/**
 * Created by diman on 7/9/2017.
 */

public class School implements ISchool
{
    private final AudioTrack track;
    private final NoiseMixer noiseMixer;
    ArrayList<LessonModel> models;
    private Lessons lessons;
    private Teacher teacher;
    private MorseTracker morseTracker;
    private PhoneticTracker phoneticTracker;
    private int lesson;
    private Configuration configuration;
    private float phoneticNoiseRatio;

    public School(
            Lessons lessons,
            TypedArray lessonTitles,
            Teacher teacher,
            MorseTracker morseTracker,
            PhoneticTracker phoneticTracker,
            NoiseMixer noiseMixer,
            Configuration configuration
    )
    {
        this.lessons = lessons;
        this.teacher = teacher;
        this.morseTracker = morseTracker;
        this.phoneticTracker = phoneticTracker;
        this.noiseMixer = noiseMixer;
        this.configuration = configuration;

        int count = Configuration.SAMPLING_RATE * Configuration.CHANNELS * 60 / 10; // enough space for one PARIS at 10wpm
        track = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                Configuration.SAMPLING_RATE,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                count * (Short.SIZE / 8),
                AudioTrack.MODE_STREAM);

        models = new ArrayList<>();
        ArrayList<String> strLessons = lessons.getLessons();

        for(int i = 0; i < strLessons.size(); i++)
        {
            LessonModel model = new LessonModel();
            model.position = i+1;
            model.title = lessonTitles.getString(i);
            model.description = strLessons.get(i);
            models.add(model);
        }

        phoneticNoiseRatio = 4.0f;
    }

    @Override
    public ArrayList<LessonModel> getLessons()
    {
        return models;
    }

    @Override
    public LessonModel getLessonAt(int i)
    {
        return models.get(i);
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

    public void beginnerLesson()
    {
        String group;
        short[] sbuffer;
        byte[] bbuffer;

        while(true)
        {
            // get lesson
            group = teacher.repeatedSymbol(lessons.getLesson(lesson));
            if(Thread.currentThread().isInterrupted())
                return;

            // spell in morse
            sbuffer = morseTracker.track(group);
            if(Thread.currentThread().isInterrupted())
                return;

            noiseMixer.addNoise(sbuffer, configuration.getNoiseLevel());
            if(Thread.currentThread().isInterrupted())
                return;

            track.write(sbuffer, 0, sbuffer.length);
            if(Thread.currentThread().isInterrupted())
                return;

            // spell phonetically
            bbuffer = phoneticTracker.track(group.substring(0, 1), 400);
            if(Thread.currentThread().isInterrupted())
                return;

            noiseMixer.addNoise(bbuffer, configuration.getNoiseLevel() / phoneticNoiseRatio);
            if(Thread.currentThread().isInterrupted())
                return;

            track.write(bbuffer, 0, bbuffer.length);
            if(Thread.currentThread().isInterrupted())
                return;
        }

    }


    public void intermediateLesson()
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

        noiseMixer.addNoise(sbuffer, configuration.getNoiseLevel());
        if(Thread.currentThread().isInterrupted())
            return;

        track.write(sbuffer, 0, sbuffer.length);
        if(Thread.currentThread().isInterrupted())
            return;

        bbuffer = phoneticTracker.track(group, 500);
        if(Thread.currentThread().isInterrupted())
            return;

        noiseMixer.addNoise(bbuffer, configuration.getNoiseLevel() / phoneticNoiseRatio);
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

            noiseMixer.addNoise(sbuffer, configuration.getNoiseLevel());
            if(Thread.currentThread().isInterrupted())
                return;

            track.write(sbuffer, 0, sbuffer.length);
            if(Thread.currentThread().isInterrupted())
                return;

            bbuffer = phoneticTracker.track(group, 250);
            if(Thread.currentThread().isInterrupted())
                return;

            noiseMixer.addNoise(bbuffer, configuration.getNoiseLevel() / phoneticNoiseRatio);
            if(Thread.currentThread().isInterrupted())
                return;

            track.write(bbuffer, 0, bbuffer.length);
            if(Thread.currentThread().isInterrupted())
                return;
        }
    }

    public void advancedLesson()
    {
        String group;
        short[] sbuffer;

        group = teacher.intro(lessons.getLesson(lesson));
        if(Thread.currentThread().isInterrupted())
            return;

        sbuffer = morseTracker.track(group);
        if(Thread.currentThread().isInterrupted())
            return;

        noiseMixer.addNoise(sbuffer, configuration.getNoiseLevel());
        if(Thread.currentThread().isInterrupted())
            return;

        track.write(sbuffer, 0, sbuffer.length);
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

            noiseMixer.addNoise(sbuffer, configuration.getNoiseLevel());
            if(Thread.currentThread().isInterrupted())
                return;

            track.write(sbuffer, 0, sbuffer.length);
            if(Thread.currentThread().isInterrupted())
                return;
        }
    }

    public void sendingPracticeLesson()
    {
        String group;
        short[] sbuffer;
        short[] pauseBuffer;

        while(true)
        {
            group = teacher.group(lessons.getLesson(lesson));
            if(Thread.currentThread().isInterrupted())
                return;

            sbuffer = morseTracker.track(group);
            if(Thread.currentThread().isInterrupted())
                return;

            noiseMixer.addNoise(sbuffer, configuration.getNoiseLevel());
            if(Thread.currentThread().isInterrupted())
                return;

            track.write(sbuffer, 0, sbuffer.length);
            if(Thread.currentThread().isInterrupted())
                return;

            pauseBuffer = new short[sbuffer.length * 2];
            noiseMixer.addNoise(pauseBuffer, configuration.getNoiseLevel());
            if(Thread.currentThread().isInterrupted())
                return;

            track.write(pauseBuffer, 0, sbuffer.length);
            if(Thread.currentThread().isInterrupted())
                return;
        }
    }

    @Override
    public void run()
    {
        switch(configuration.getCourseLevel())
        {
            case Beginner:
                beginnerLesson();
                break;
            case Intermediate:
                intermediateLesson();
                break;
            case Advanced:
                advancedLesson();
                break;
            case SendingPractice:
                sendingPracticeLesson();
            default:
                break;
        }
    }
}
