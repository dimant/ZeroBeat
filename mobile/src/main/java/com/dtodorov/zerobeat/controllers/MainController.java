package com.dtodorov.zerobeat.controllers;

import com.dtodorov.androlib.eventdispatcher.IEventDispatcher;
import com.dtodorov.androlib.eventdispatcher.IEventListener;
import com.dtodorov.androlib.services.IStringResolver;
import com.dtodorov.zerobeat.teacher.ISchool;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.delegates.Action;

/**
 * Created by diman on 7/8/2017.
 */

public class MainController
{
    public static final String ShowLessons = "showLessons";
    public static final String ChangeLesson = "changeLesson";
    public static final String PlayLesson = "playLesson";
    public static final String PauseLesson = "pauseLesson";
    public static final String StopLesson = "stopLesson";

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
    private Object triggerParam;
    private Thread schoolThread;

    public MainController(
            IStringResolver stringResolver,
            final IEventDispatcher eventDispatcher,
            final ISchool school)
    {
        this.stateMachine = new StateMachine<State, Trigger>(State.Idle);

        Action homeAction = new Action() {
            @Override
            public void doIt()
            {
                eventDispatcher.emit(MainController.ShowLessons, school.getLessons());
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

                    }
                })
                .permit(Trigger.Stop, State.Idle);

        homeAction.doIt();

        eventDispatcher.register(MainController.ChangeLesson, new IEventListener()
        {
            @Override
            public void callback(Object param)
            {
                school.setLesson((Integer) param);
            }
        });

        eventDispatcher.register(MainController.PlayLesson, new IEventListener()
        {
            @Override
            public void callback(Object param)
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
        });

        eventDispatcher.register(MainController.PauseLesson, new IEventListener()
        {
            @Override
            public void callback(Object param)
            {
                school.pause();
            }
        });

        eventDispatcher.register(MainController.StopLesson, new IEventListener()
        {
            @Override
            public void callback(Object param)
            {
                if(schoolThread != null && schoolThread.isAlive())
                {
                    schoolThread.interrupt();
                }
                schoolThread = null;
                school.stop();
            }
        });
    }

    public void fire(Trigger trigger, Object triggerParam) {
        this.triggerParam = triggerParam;
        if(stateMachine.canFire(trigger)) {
            stateMachine.fire(trigger);
        }
    }
}
