package com.dtodorov.zerobeat.controllers;

import com.dtodorov.androlib.eventdispatcher.IEventDispatcher;
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
                        Integer lesson = (Integer) triggerParam;
                        school.setLesson(lesson);
                        Thread t = new Thread(school);
                        t.start();
                    }
                })
                .permitReentry(Trigger.Play)
                .permit(Trigger.Stop, State.Idle);

        homeAction.doIt();
    }

    public void fire(Trigger trigger, Object triggerParam) {
        this.triggerParam = triggerParam;
        if(stateMachine.canFire(trigger)) {
            stateMachine.fire(trigger);
        }
    }
}
