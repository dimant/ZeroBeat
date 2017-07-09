package com.dtodorov.zerobeat.morse;

import android.media.AudioTrack;

/**
 * Created by diman on 7/9/2017.
 */

public interface ITracker
{
    short[] track(String text);
}
