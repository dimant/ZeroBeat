package com.dtodorov.zerobeat;

import android.app.Application;

import com.dtodorov.androlib.eventdispatcher.EventDispatcher;
import com.dtodorov.androlib.eventdispatcher.IEventDispatcher;
import com.dtodorov.androlib.services.StringResolver;
import com.dtodorov.zerobeat.audio.NoiseMixer;
import com.dtodorov.zerobeat.audio.morse.MorseTracker;
import com.dtodorov.zerobeat.audio.morse.SignalGenerator;
import com.dtodorov.zerobeat.audio.PhoneticTracker;
import com.dtodorov.zerobeat.controllers.PlayController;
import com.dtodorov.zerobeat.teacher.Lessons;
import com.dtodorov.zerobeat.teacher.School;
import com.dtodorov.zerobeat.teacher.Teacher;

/**
 * Created by diman on 7/18/2017.
 */

public class ZeroBeatApplication extends Application
{
    private PlayController playController;
    private IEventDispatcher eventDispatcher;
    private Configuration configuration;
    private StringResolver stringResolver;

    public IEventDispatcher getEventDispatcher()
    {
        if(eventDispatcher == null)
        {
            Initialize();
        }

        return eventDispatcher;
    }

    public PlayController getPlayController()
    {
        if(playController == null)
        {
            Initialize();
        }

        return playController;
    }

    public Configuration getConfiguration()
    {
        if(configuration == null)
        {
            Initialize();
        }

        return configuration;
    }

    public StringResolver getStringResolver()
    {
        if(stringResolver == null)
        {
            Initialize();
        }

        return stringResolver;
    }

    private void Initialize()
    {
        eventDispatcher =   new EventDispatcher();
        configuration =     new Configuration();
        stringResolver =    new StringResolver(getResources());
        playController =    new PlayController(
                eventDispatcher,
                new School(
                        new Lessons(),
                        getResources().obtainTypedArray(R.array.lesson_titles),
                        new Teacher(configuration),
                        new MorseTracker(new SignalGenerator(configuration)),
                        new PhoneticTracker(getResources()),
                        new NoiseMixer(),
                        configuration));
    }
}
