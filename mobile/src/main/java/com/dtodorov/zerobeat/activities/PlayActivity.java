package com.dtodorov.zerobeat.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dtodorov.androlib.eventdispatcher.EventDispatcher;
import com.dtodorov.androlib.eventdispatcher.IEventDispatcher;
import com.dtodorov.androlib.eventdispatcher.IEventListener;
import com.dtodorov.androlib.services.StringResolver;
import com.dtodorov.zerobeat.Configuration;
import com.dtodorov.zerobeat.R;
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


public class PlayActivity extends AppCompatActivity
{
    public static final String COURSE_LEVEL_KEY = "courseLevelKey";

    private MainController mainController;
    private IEventDispatcher eventDispatcher;
    private Configuration configuration;
    private FloatingMusicActionButton buttonMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final StringResolver stringResolver = new StringResolver(getResources());
        eventDispatcher = new EventDispatcher();

        configuration = new Configuration();
        loadConfiguration(configuration);

        final ListView listView = (ListView) findViewById(R.id.lessons);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eventDispatcher.emit(MainController.ChangeLesson, position);
            }
        });

        final School school = new School(
                new Lessons(),
                new Teacher(configuration),
                new MorseTracker(new SignalGenerator(configuration)),
                new PhoneticTracker(getResources()),
                configuration);

        eventDispatcher.register(MainController.ShowLessons, new IEventListener() {
            @Override
            public void callback(Object param) {
                ArrayList<String> lessons = (ArrayList<String>) param;
                LessonsAdapter adapter = new LessonsAdapter(PlayActivity.this, lessons, stringResolver);
                listView.setAdapter(adapter);
                listView.setItemChecked(school.getLesson(), true);
            }
        });

        mainController = new MainController(
                stringResolver,
                eventDispatcher,
                school);

        buttonMusic = (FloatingMusicActionButton) findViewById(R.id.button_music);
        buttonMusic.setOnMusicFabClickListener(new FloatingMusicActionButton.OnMusicFabClickListener()
        {
            @Override
            public void onClick(@NotNull View view)
            {
                mainController.fire(MainController.Trigger.Play, null);
            }
        });
    }

    private void loadConfiguration(Configuration configuration)
    {
        int wpm = SettingsActivity.getWpm(this);
        configuration.setWpm(wpm);

        int frequency = SettingsActivity.getFrequency(this);
        configuration.setFrequency(frequency);

        int groupSize = SettingsActivity.getGroupSize(this);
        configuration.setGroupSize(groupSize);

        configuration.setSamplingRate(44100);
        configuration.setChannels(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                mainController.fire(MainController.Trigger.Stop, null);
                buttonMusic.changeMode(FloatingMusicActionButton.Mode.PLAY_TO_STOP);
                Intent intent = new Intent(PlayActivity.this, SettingsActivity.class);
                startActivityForResult(intent, SettingsActivity.REQUEST_CODE_PREFERENCES);
                return true;
            case android.R.id.home:
                mainController.fire(MainController.Trigger.Stop, null);
                buttonMusic.changeMode(FloatingMusicActionButton.Mode.PLAY_TO_STOP);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        switch(requestCode)
        {
            case SettingsActivity.REQUEST_CODE_PREFERENCES:
                loadConfiguration(configuration);
                break;
        }
    }
}
