package com.dtodorov.zerobeat.teacher;

import java.util.Random;

/**
 * Created by diman on 7/9/2017.
 */

public class Teacher implements ITeacher
{
    private char[] lesson;
    private int groupSize;
    private Random rng;

    public Teacher(String lesson, int groupSize)
    {
        this.lesson = lesson.toCharArray();
        rng = new Random(System.currentTimeMillis());
    }

    @Override
    public String intro()
    {
        return Stringer.join(lesson, " ");
    }

    @Override
    public String group()
    {
        int i, idx;
        StringBuilder builder = new StringBuilder(groupSize + 1);

        for(i = 0; i < groupSize; i++)
        {
            builder.append(
                    lesson[
                            rng.nextInt(lesson.length - 1)]);
        }
        builder.append(' ');

        return builder.toString();
    }
}
