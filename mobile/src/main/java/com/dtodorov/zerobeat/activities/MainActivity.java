package com.dtodorov.zerobeat.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.dtodorov.zerobeat.R;
import com.dtodorov.zerobeat.adapters.CardModel;
import com.dtodorov.zerobeat.adapters.CardsAdapter;
import com.dtodorov.zerobeat.controllers.MainController;

import be.rijckaert.tim.animatedvector.FloatingMusicActionButton;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lvCards = (ListView) findViewById(R.id.listview_courses);
        CardsAdapter adapter = new CardsAdapter(this);

        lvCards.setAdapter(adapter);
        adapter.addAll(
                new CardModel(R.mipmap.course_icon_1, R.string.cardview_beginner_title, R.string.cardview_beginner_summary),
                new CardModel(R.mipmap.course_icon_2, R.string.cardview_intermediate_title, R.string.cardview_intermediate_summary),
                new CardModel(R.mipmap.course_icon_3, R.string.cardview_advanced_title, R.string.cardview_advanced_summary));
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
