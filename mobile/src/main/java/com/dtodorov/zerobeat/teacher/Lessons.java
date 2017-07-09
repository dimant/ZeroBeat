package com.dtodorov.zerobeat.teacher;

import java.util.ArrayList;

/**
 * Created by diman on 7/9/2017.
 */

public class Lessons
{
    private ArrayList<String> lessons;

    public Lessons()
    {
        this.lessons = new ArrayList<>();

        lessons.add("eishtmo");
        lessons.add("awjndb");
        lessons.add("uvgzkrpx");
        lessons.add("fclqy");
        lessons.add("12345");
        lessons.add("67890");
        lessons.add(".,?/");
    }

    public String getLesson(int i)
    {
        return lessons.get(i);
    }
}
