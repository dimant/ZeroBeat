package com.dtodorov.zerobeat.audio.voice;

import android.content.res.Resources;

import com.dtodorov.zerobeat.Configuration;
import com.dtodorov.zerobeat.R;
import com.dtodorov.zerobeat.audio.morse.SignalGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by diman on 7/9/2017.
 */

public class PhoneticTracker
{
    private Resources res;
    private HashMap<Character, Integer> resourceMap;

    private byte[] buffer;
    private int offset;

    public PhoneticTracker(Resources res)
    {
        this.res = res;
        resourceMap = new HashMap<>();
        resourceMap.put('a', R.raw.alpha);
        resourceMap.put('b', R.raw.bravo);
        resourceMap.put('c', R.raw.charlie);
        resourceMap.put('d', R.raw.delta);
        resourceMap.put('e', R.raw.echo);
        resourceMap.put('f', R.raw.foxtrot);
        resourceMap.put('g', R.raw.golf);
        resourceMap.put('h', R.raw.hotel);
        resourceMap.put('i', R.raw.india);
        resourceMap.put('j', R.raw.juliet);
        resourceMap.put('k', R.raw.kilo);
        resourceMap.put('l', R.raw.lima);
        resourceMap.put('m', R.raw.mike);
        resourceMap.put('n', R.raw.november);
        resourceMap.put('o', R.raw.oscar);
        resourceMap.put('p', R.raw.papa);
        resourceMap.put('q', R.raw.quebec);
        resourceMap.put('r', R.raw.romeo);
        resourceMap.put('s', R.raw.sierra);
        resourceMap.put('t', R.raw.tango);
        resourceMap.put('u', R.raw.uniform);
        resourceMap.put('v', R.raw.victor);
        resourceMap.put('w', R.raw.whiskey);
        resourceMap.put('x', R.raw.xray);
        resourceMap.put('y', R.raw.yankee);
        resourceMap.put('z', R.raw.zulu);
        resourceMap.put('0', R.raw.zero);
        resourceMap.put('1', R.raw.one);
        resourceMap.put('2', R.raw.two);
        resourceMap.put('3', R.raw.three);
        resourceMap.put('4', R.raw.four);
        resourceMap.put('5', R.raw.five);
        resourceMap.put('6', R.raw.six);
        resourceMap.put('7', R.raw.seven);
        resourceMap.put('8', R.raw.eight);
        resourceMap.put('9', R.raw.nine);
        resourceMap.put('.', R.raw.period);
        resourceMap.put(',', R.raw.coma);
        resourceMap.put('?', R.raw.question);
        resourceMap.put('/', R.raw.slash);
    }

    public void writeSilence(int ms)
    {
        long samples = ms * (Configuration.SAMPLING_RATE / 1000);
        int i;
        int j;
        byte sample = 0; // silence

        for(i = 0; i < samples; i++)
        {
            for(j = 0; j < Configuration.CHANNELS; j++)
            {
                if(buffer != null)
                    buffer[offset] = sample;
                offset++;
            }
        }
    }

    public void readResource(int resource)
    {
        try
        {
            InputStream inStream = res.openRawResource(resource);
            inStream.skip(44); // skip the wav header
            int len = inStream.available();

            if(buffer != null)
                inStream.read(buffer, offset, len);
            offset += len;

            inStream.close();
        }
        catch (IOException e)
        {

        }
    }

    private void encode(char[] chars)
    {
        for(char c : chars)
        {
            if(resourceMap.containsKey(c))
            {
                int r = resourceMap.get(c);
                readResource(r);
                writeSilence(100);
            }
        }
    }

    public byte[] track(String text, int trailingSilenceMs)
    {
        char[] chars = text.toCharArray();

        this.buffer = null;
        this.offset = 0;
        encode(chars);
        writeSilence(trailingSilenceMs);

        this.buffer = new byte[offset];
        this.offset = 0;
        encode(chars);
        writeSilence(trailingSilenceMs);

        return buffer;
    }
}
