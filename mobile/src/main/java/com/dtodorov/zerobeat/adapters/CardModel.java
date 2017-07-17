package com.dtodorov.zerobeat.adapters;

/**
 * Created by diman on 7/16/2017.
 */

public class CardModel
{
    private int imageId;
    private int titleId;
    private int subtitleId;

    public CardModel(int imageId, int titleId, int subtitleId) {
        this.imageId = imageId;
        this.titleId = titleId;
        this.subtitleId = subtitleId;
    }

    public int getImageId() {
        return imageId;
    }

    public int getTitle() {
        return titleId;
    }

    public int getSubtitle() {
        return subtitleId;
    }
}
