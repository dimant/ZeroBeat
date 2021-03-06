package com.dtodorov.zerobeat.models;

import com.dtodorov.zerobeat.Configuration;

/**
 * Created by diman on 7/16/2017.
 */

public class CardModel
{
    private int imageId;
    private int titleId;
    private int subtitleId;
    private Configuration.CourseLevel course;

    public CardModel(int imageId, int titleId, int subtitleId, Configuration.CourseLevel course)
    {
        this.imageId = imageId;
        this.titleId = titleId;
        this.subtitleId = subtitleId;
        this.course = course;
    }

    public int getImageId()
    {
        return imageId;
    }

    public int getTitle()
    {
        return titleId;
    }

    public int getSubtitle()
    {
        return subtitleId;
    }

    public Configuration.CourseLevel getCourseLevel()
    {
        return course;
    }
}
