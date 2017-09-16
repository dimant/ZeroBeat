package com.dtodorov.zerobeat.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dtodorov.zerobeat.Configuration;
import com.dtodorov.zerobeat.R;
import com.dtodorov.zerobeat.models.CardModel;
import com.dtodorov.zerobeat.adapters.CardsAdapter;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lvCards = (ListView) findViewById(R.id.listview_courses);
        final CardsAdapter adapter = new CardsAdapter(this);

        lvCards.setAdapter(adapter);
        adapter.addAll(
                new CardModel(R.mipmap.telegraph_boy, R.string.cardview_beginner_title, R.string.cardview_beginner_summary, Configuration.CourseLevel.Beginner),
                new CardModel(R.mipmap.telegraph_lesson, R.string.cardview_intermediate_title, R.string.cardview_intermediate_summary, Configuration.CourseLevel.Intermediate),
                new CardModel(R.mipmap.marine_field_telegraph, R.string.cardview_advanced_title, R.string.cardview_advanced_summary, Configuration.CourseLevel.Advanced),
                new CardModel(R.mipmap.sending_practice, R.string.cardview_sending_practice_title, R.string.cardview_sending_practice_summary, Configuration.CourseLevel.SendingPractice));

        lvCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CardModel model = adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                intent.putExtra(PlayActivity.COURSE_LEVEL_KEY, model.getCourseLevel().name());
                startActivity(intent);
            }
        });
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
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
