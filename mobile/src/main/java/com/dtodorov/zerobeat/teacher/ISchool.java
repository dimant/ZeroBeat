package com.dtodorov.zerobeat.teacher;

import com.dtodorov.zerobeat.models.LessonModel;

import java.util.ArrayList;

/**
 * Created by diman on 7/9/2017.
 */

public interface ISchool extends Runnable
{
    ArrayList<LessonModel> getLessons();

    void setLesson(int lesson);

    int getLesson();

    boolean isPlaying();

    void stop();

    void play();
}
