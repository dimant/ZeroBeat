package com.dtodorov.zerobeat.audio;

import java.util.Random;

/**
 * Created by diman on 7/23/2017.
 */

public class NoiseMixer
{
    private Random rng;
    public NoiseMixer()
    {
        rng = new Random(System.currentTimeMillis());
    }

    public void addNoise(short[] input, float level)
    {
        short sample;
        for(int i = 0; i < input.length; i++)
        {
            sample = (short) rng.nextInt();
            input[i] = (short) (input[i] * (1.0 - level) + sample * level);
        }
    }

    public void addNoise(byte[] input, float level)
    {
        byte sample;
        for(int i = 0; i < input.length; i++)
        {
            sample = (byte) rng.nextInt();
            input[i] = (byte) (input[i] * (1.0 - level) + sample * level);
        }
    }
}
