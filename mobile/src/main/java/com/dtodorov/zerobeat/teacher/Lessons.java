package com.dtodorov.zerobeat.teacher;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by diman on 7/9/2017.
 */

public class Lessons
{
    private ArrayList<String> lessons;

    public Lessons()
    {
        this.lessons = new ArrayList<>();

//        lessons.add("eishtmo");
//        lessons.add("awjndb");
//        lessons.add("uvgzkrpx");
//        lessons.add("fclqy");
//        lessons.add("12345");
//        lessons.add("67890");
//        lessons.add(".,?/");
        lessons.add("tean");
        lessons.add("teanois14");
        lessons.add("ois14rhd25");
        lessons.add("14rhd25luc");
        lessons.add("25lucmw36?");
        lessons.add("mw36?fy");
        lessons.add("36?fypg79/");
        lessons.add("pg79/bv");
        lessons.add("bvkj80");
        lessons.add("kj80xqz");

        Collections.unmodifiableList(lessons);
    }

    public String getLesson(int i)
    {
        return lessons.get(i);
    }

    public ArrayList<String> getLessons()
    {
        return lessons;
    }
}
