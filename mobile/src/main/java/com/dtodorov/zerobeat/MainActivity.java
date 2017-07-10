package com.dtodorov.zerobeat;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.dtodorov.androlib.eventdispatcher.EventDispatcher;
import com.dtodorov.androlib.eventdispatcher.IEventDispatcher;
import com.dtodorov.androlib.eventdispatcher.IEventListener;
import com.dtodorov.androlib.services.StringResolver;
import com.dtodorov.zerobeat.adapters.LessonsAdapter;
import com.dtodorov.zerobeat.audio.morse.MorseTracker;
import com.dtodorov.zerobeat.audio.morse.SignalGenerator;
import com.dtodorov.zerobeat.audio.voice.PhoneticTracker;
import com.dtodorov.zerobeat.controllers.MainController;
import com.dtodorov.zerobeat.teacher.Lessons;
import com.dtodorov.zerobeat.teacher.School;
import com.dtodorov.zerobeat.teacher.Teacher;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
{
    private MainController mainController;
    private IEventDispatcher eventDispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final StringResolver stringResolver = new StringResolver(getResources());
        eventDispatcher = new EventDispatcher();

        Configuration configuration = new Configuration();
        configuration.setWpm(15);
        configuration.setSamplingRate(44100);
        configuration.setFrequency(701);
        configuration.setGroupSize(5);
        configuration.setChannels(1);

        eventDispatcher.register(MainController.ShowLessons, new IEventListener() {
            @Override
            public void callback(Object param) {
                ArrayList<String> lessons = (ArrayList<String>) param;
                LessonsAdapter adapter = new LessonsAdapter(MainActivity.this, lessons, stringResolver);
                ListView listView = (ListView) findViewById(R.id.lessons);
                listView.setAdapter(adapter);
            }
        });

        mainController = new MainController(
                stringResolver,
                eventDispatcher,
                new School(
                        new Lessons(),
                        new Teacher(configuration),
                        new MorseTracker(new SignalGenerator(configuration)),
                        new PhoneticTracker(getResources()),
                        configuration));

        Button buttonPlay = (Button) findViewById(R.id.play);
        buttonPlay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                eventDispatcher.emit(MainController.PlayLesson, null);
            }
        });

        Button buttonPause = (Button) findViewById(R.id.pause);
        buttonPause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                eventDispatcher.emit(MainController.PauseLesson, null);
            }
        });

        Button buttonStop = (Button) findViewById(R.id.stop);
        buttonStop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                eventDispatcher.emit(MainController.StopLesson, null);
            }
        });

        final ListView listView = (ListView) findViewById(R.id.lessons);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eventDispatcher.emit(MainController.ChangeLesson, position);
            }
        });
    }
}
