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

    private enum State
    {
        Idle,
        Playing
    };

    public enum Trigger
    {
        Play,
        Stop
    };

    private StateMachine<State, Trigger> stateMachine;
    private ISchool school;
    private IEventDispatcher eventDispatcher;
    private Thread schoolThread;

    public PlayController(
            final IEventDispatcher eventDispatcher,
            final ISchool school)
    {
        this.eventDispatcher = eventDispatcher;
        this.stateMachine = new StateMachine<>(State.Idle);
        this.school = school;

        Action homeAction = new Action() {
            @Override
            public void doIt()
            {
                eventDispatcher.emit(PlayController.ShowLessons, school.getLessons());
                if(schoolThread != null && schoolThread.isAlive())
                {
                    schoolThread.interrupt();
                }
                schoolThread = null;
                school.stop();
            }
        };

        stateMachine.configure(State.Idle)
                .onEntry(homeAction)
                .permit(Trigger.Play, State.Playing);

        stateMachine.configure(State.Playing)
                .onEntry(new Action() {
                    @Override
                    public void doIt()
                    {
                        if(schoolThread != null && schoolThread.isAlive())
                        {
                            schoolThread.interrupt();
                            school.stop();
                        }

                        school.play();
                        schoolThread = new Thread(school);
                        schoolThread.start();
                    }
                })
                .permit(Trigger.Play, State.Idle)
                .permit(Trigger.Stop, State.Idle);

        homeAction.doIt();

        eventDispatcher.register(PlayController.ChangeLesson, new IEventListener()
        {
            @Override
            public void callback(Object param)
            {
                boolean play = false;
                if(schoolThread != null && schoolThread.isAlive())
                {
                    schoolThread.interrupt();
                    school.stop();
                    play = true;
                }
                school.setLesson((Integer) param);
                if(play)
                {
                    school.play();
                    schoolThread = new Thread(school);
                    schoolThread.start();
                }
            }
        });
    }

    public int getLesson()
    {
        return school.getLesson();
    }

    public boolean isPlaying()
    {
        return school.isPlaying();
    }

    public void showLessons()
    {
        eventDispatcher.emit(PlayController.ShowLessons, school.getLessons());
    }

    public void fire(Trigger trigger) {
        if(stateMachine.canFire(trigger)) {
            stateMachine.fire(trigger);
        }
    }
}
