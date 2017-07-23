package com.dtodorov.zerobeat.controllers;

import com.dtodorov.androlib.eventdispatcher.IEventDispatcher;
import com.dtodorov.androlib.eventdispatcher.IEventListener;
import com.dtodorov.androlib.services.IStringResolver;
import com.dtodorov.zerobeat.activities.PlayActivity;
import com.dtodorov.zerobeat.models.LessonModel;
import com.dtodorov.zerobeat.teacher.ISchool;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.delegates.Action;

import java.util.ArrayList;

/**
 * Created by diman on 7/8/2017.
 */

public class PlayController
{
    public static final String ShowLessons = "showLessons";
    public static final String ChangeLesson = "changeLesson";
    public static final String SetMusicButton = "setMusicButton";
    public static final String OnMusicButtonPressed = "onMusicButtonPressed";
    public static final String OnLeavingActivity = "onLeavingActivity";
    public static final String SetNowPlaying = "setNowPlaying";

    private ISchool school;
    private IEventDispatcher eventDispatcher;
    private Thread schoolThread;

    public PlayController(
            final IEventDispatcher eventDispatcher,
            final ISchool school)
    {
        this.eventDispatcher = eventDispatcher;
        this.school = school;

        eventDispatcher.emit(PlayController.ShowLessons, school.getLessons());
        stop();

        eventDispatcher.register(PlayController.OnMusicButtonPressed, new IEventListener()
        {
            @Override
            public void callback(Object param)
            {
                Boolean isChecked = (Boolean) param;
                if(isChecked)
                {
                    play();
                }
                else
                {
                    stop();
                }
            }
        });

        eventDispatcher.register(PlayController.OnLeavingActivity, new IEventListener()
        {
            @Override
            public void callback(Object param)
            {
                stop();
                eventDispatcher.emit(PlayController.SetMusicButton, false);
            }
        });

        eventDispatcher.register(PlayController.ChangeLesson, new IEventListener()
        {
            @Override
            public void callback(Object param)
            {
                Integer i = (Integer) param;

                if(i == school.getLesson())
                {
                    if(isPlaying())
                    {
                        stop();
                        eventDispatcher.emit(PlayController.SetMusicButton, false);
                    }
                    else
                    {
                        play();
                        eventDispatcher.emit(PlayController.SetMusicButton, true);
                    }
                }
                else
                {
                    if(isPlaying())
                    {
                        stop();
                    }
                    else
                    {
                        eventDispatcher.emit(PlayController.SetMusicButton, true);
                    }
                    eventDispatcher.emit(PlayController.SetNowPlaying, school.getLessonAt(i));
                    school.setLesson((Integer) param);
                    play();
                }
            }
        });
    }

    public boolean isPlaying()
    {
        return schoolThread != null && schoolThread.isAlive();
    }

    public void stop()
    {
        if(isPlaying())
        {
            schoolThread.interrupt();
            school.stop();
        }
    }

    public void play()
    {
        stop();
        school.play();
        schoolThread = new Thread(school);
        schoolThread.start();
    }

    public void showLessons()
    {
        eventDispatcher.emit(PlayController.ShowLessons, school.getLessons());
    }

    public void showNowPlayingAt(int i)
    {
        eventDispatcher.emit(PlayController.SetNowPlaying, school.getLessonAt(i));
    }
}
