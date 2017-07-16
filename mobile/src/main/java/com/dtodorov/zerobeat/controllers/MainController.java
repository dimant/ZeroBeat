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
    private ISchool school;
    private Thread schoolThread;
    private int lessonIndex;

    public MainController(
            IStringResolver stringResolver,
            final IEventDispatcher eventDispatcher,
            final ISchool school)
    {
        this.stateMachine = new StateMachine<State, Trigger>(State.Idle);
        this.school = school;

        Action homeAction = new Action() {
            @Override
            public void doIt()
            {
                eventDispatcher.emit(MainController.ShowLessons, school.getLessons());
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

        eventDispatcher.register(MainController.ChangeLesson, new IEventListener()
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
                lessonIndex = (Integer) param;
                school.setLesson(lessonIndex);
                if(play)
                {
                    school.play();
                    schoolThread = new Thread(school);
                    schoolThread.start();
                }
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
