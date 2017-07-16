package com.dtodorov.zerobeat;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import be.rijckaert.tim.animatedvector.FloatingMusicActionButton;


public class MainActivity extends AppCompatActivity
{
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private MainController mainController;
    private IEventDispatcher eventDispatcher;
    private Configuration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final StringResolver stringResolver = new StringResolver(getResources());
        eventDispatcher = new EventDispatcher();

        configuration = new Configuration();
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

        FloatingMusicActionButton buttonMusic = (FloatingMusicActionButton) findViewById(R.id.button_music);
        buttonMusic.setOnMusicFabClickListener(new FloatingMusicActionButton.OnMusicFabClickListener()
        {
            @Override
            public void onClick(@NotNull View view)
            {
                mainController.fire(MainController.Trigger.Play, null);
            }
        });

        final ListView listView = (ListView) findViewById(R.id.lessons);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eventDispatcher.emit(MainController.ChangeLesson, position);
            }
        });

        ImageButton buttonSettings = (ImageButton) findViewById(R.id.button_settings);
        buttonSettings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
