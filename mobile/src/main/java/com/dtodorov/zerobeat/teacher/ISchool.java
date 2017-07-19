package com.dtodorov.zerobeat.teacher;

import java.util.ArrayList;

/**
 * Created by diman on 7/9/2017.
 */

public interface ISchool extends Runnable
{
    ArrayList<String> getLessons();

    void setLesson(int lesson);

    int getLesson();

    boolean isPlaying();

    void stop();

    void play();
}
