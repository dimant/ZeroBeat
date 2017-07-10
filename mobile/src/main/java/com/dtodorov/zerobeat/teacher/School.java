package com.dtodorov.zerobeat.teacher;

import android.content.res.Resources;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.dtodorov.zerobeat.Configuration;
import com.dtodorov.zerobeat.audio.morse.MorseTracker;
import com.dtodorov.zerobeat.audio.voice.VoiceTracker;

/**
 * Created by diman on 7/9/2017.
 */

public class School implements ISchool
{
    private boolean teaching;
    private Lessons lessons;
    private Teacher teacher;
    private MorseTracker morseTracker;
    private VoiceTracker voiceTracker;
    private AudioTrack track;
    private Configuration configuration;

    public School(
            Lessons lessons,
            Teacher teacher,
            MorseTracker morseTracker,
            VoiceTracker voiceTracker,
            Configuration configuration
    )
    {
        this.lessons = lessons;
        this.teacher = teacher;
        this.morseTracker = morseTracker;
        this.voiceTracker = voiceTracker;
        this.configuration = configuration;
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

        int lesson = 0;

        group = teacher.intro(lessons.getLesson(lesson));

        sbuffer = morseTracker.track(group);
        track.write(sbuffer, 0, sbuffer.length);

        bbuffer = voiceTracker.track(group);
        track.write(bbuffer, 0, bbuffer.length);

        teaching = true;
        while(teaching)
        {
            group = teacher.group(lessons.getLesson(lesson));

            sbuffer = morseTracker.track(group);
            track.write(sbuffer, 0, sbuffer.length);

            bbuffer = voiceTracker.track(group);
            track.write(bbuffer, 0, bbuffer.length);
        }
    }
}
