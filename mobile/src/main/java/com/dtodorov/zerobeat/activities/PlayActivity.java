package com.dtodorov.zerobeat.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.dtodorov.androlib.eventdispatcher.IEventDispatcher;
import com.dtodorov.androlib.eventdispatcher.IEventListener;
import com.dtodorov.zerobeat.R;
import com.dtodorov.zerobeat.ZeroBeatApplication;
import com.dtodorov.zerobeat.adapters.LessonsAdapter;
import com.dtodorov.zerobeat.models.LessonModel;
import com.dtodorov.zerobeat.controllers.PlayController;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity
{
    public static final String COURSE_LEVEL_KEY = "courseLevelKey";

    private ZeroBeatApplication app;
    private ToggleButton buttonMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        app = (ZeroBeatApplication) getApplication();
        app.getConfiguration().loadConfiguration(app);

        Intent intent = getIntent();
        app.getConfiguration().setCourseLevel(intent.getStringExtra(PlayActivity.COURSE_LEVEL_KEY));

        final IEventDispatcher eventDispatcher = app.getEventDispatcher();
        final PlayController playController = app.getPlayController();

        final ListView listView = (ListView) findViewById(R.id.lessons);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eventDispatcher.emit(PlayController.ChangeLesson, position);
            }
        });

        eventDispatcher.register(PlayController.ShowLessons, new IEventListener() {
            @Override
            public void callback(Object param) {
                ArrayList<LessonModel> lessons = (ArrayList<LessonModel>) param;
                LessonsAdapter adapter = new LessonsAdapter(PlayActivity.this, lessons, app.getStringResolver());
                listView.setAdapter(adapter);
            }
        });
        playController.showLessons();

        buttonMusic = (ToggleButton) findViewById(R.id.button_music);
        buttonMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(buttonMusic.isPressed())
                {
                    eventDispatcher.emit(PlayController.OnMusicButtonPressed, isChecked);
                }
            }
        });

        eventDispatcher.register(PlayController.SetMusicButton, new IEventListener()
        {
            @Override
            public void callback(Object param)
            {
                Boolean isChecked = (Boolean) param;
                buttonMusic.setChecked(isChecked);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                app.getEventDispatcher().emit(PlayController.OnLeavingActivity, null);
                Intent intent = new Intent(PlayActivity.this, SettingsActivity.class);
                startActivityForResult(intent, SettingsActivity.REQUEST_CODE_PREFERENCES);
                return true;
            case android.R.id.home:
                app.getEventDispatcher().emit(PlayController.OnLeavingActivity, null);
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
                app.getConfiguration().loadConfiguration(app);
                break;
        }
    }
}
