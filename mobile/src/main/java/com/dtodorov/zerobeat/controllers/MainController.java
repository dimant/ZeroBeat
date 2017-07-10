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
    private enum State
    {
        Idle,
        Playing
    };

    public enum Trigger
    {
        Play
    };

    private StateMachine<State, Trigger> stateMachine;

    public MainController(
            IStringResolver stringResolver,
            IEventDispatcher eventDispatcher,
            final ISchool school)
    {
        this.stateMachine = new StateMachine<State, Trigger>(State.Idle);

        Action homeAction = new Action() {
            @Override
            public void doIt()
            {
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
                        Thread t = new Thread(school);
                        t.start();
                    }
                })
                .permit(Trigger.Play, State.Idle);
    }

    public void fire(Trigger trigger) {
        if(stateMachine.canFire(trigger)) {
            stateMachine.fire(trigger);
        }
    }
}
