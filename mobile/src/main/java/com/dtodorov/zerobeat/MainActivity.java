package com.dtodorov.zerobeat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dtodorov.androlib.eventdispatcher.EventDispatcher;
import com.dtodorov.androlib.eventdispatcher.IEventDispatcher;
import com.dtodorov.androlib.services.StringResolver;
import com.dtodorov.zerobeat.audio.morse.MorseTracker;
import com.dtodorov.zerobeat.audio.morse.SignalGenerator;
import com.dtodorov.zerobeat.audio.voice.VoiceTracker;
import com.dtodorov.zerobeat.controllers.MainController;
import com.dtodorov.zerobeat.teacher.Lessons;
import com.dtodorov.zerobeat.teacher.School;
import com.dtodorov.zerobeat.teacher.Teacher;


public class MainActivity extends AppCompatActivity
{
    private MainController mainController;
    private IEventDispatcher eventDispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StringResolver stringResolver = new StringResolver(getResources());
        eventDispatcher = new EventDispatcher();

        Configuration configuration = new Configuration();
        configuration.setWpm(15);
        configuration.setSamplingRate(44100);
        configuration.setFrequency(701);
        configuration.setGroupSize(5);
        configuration.setChannels(1);

        mainController = new MainController(
                new StringResolver(getResources()),
                new EventDispatcher(),
                new School(
                        new Lessons(),
                        new Teacher(configuration),
                        new MorseTracker(new SignalGenerator(configuration)),
                        new VoiceTracker(getResources()),
                        configuration));

        Button buttonPlay = (Button) findViewById(R.id.play);
        buttonPlay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mainController.fire(MainController.Trigger.Play);
            }
        });
    }
}
