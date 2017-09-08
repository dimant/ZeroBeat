package com.dtodorov.zerobeat.teacher;

import java.util.Random;

/**
 * Created by diman on 7/9/2017.
 */

public class Teacher implements ITeacher
{
    private ITeacherConfiguration configuration;
    private Random rng;

    public Teacher(ITeacherConfiguration configuration)
    {
        this.configuration = configuration;
        rng = new Random(System.currentTimeMillis());
    }

    @Override
    public String intro(String lesson)
    {
        return Stringer.join(lesson.toCharArray(), " ") + " ";
    }

    @Override
    public String group(String lesson)
    {
        int i;
        StringBuilder builder = new StringBuilder(configuration.getGroupSize() + 1);

        for(i = 0; i < configuration.getGroupSize(); i++)
        {
            builder.append(
                    lesson.charAt(
                            rng.nextInt(lesson.length())));

        }
        builder.append(' ');

        return builder.toString();
    }

    @Override
    public String repeatedSymbol(String lesson)
    {
        int i, idx;
        StringBuilder builder = new StringBuilder(configuration.getGroupSize() + 1);

        idx = rng.nextInt(lesson.length());
        for(i = 0; i < configuration.getGroupSize(); i++)
        {
            builder.append(lesson.charAt(idx));
        }
        builder.append(' ');

        return builder.toString();
    }
}
