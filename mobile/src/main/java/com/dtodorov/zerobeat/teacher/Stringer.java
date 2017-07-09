package com.dtodorov.zerobeat.teacher;

import java.util.List;

/**
 * Created by diman on 7/9/2017.
 */

public class Stringer
{
    public static String join(char[] chars, String delim) {

        StringBuilder sb = new StringBuilder();

        String loopDelim = "";

        for(char c : chars) {

            sb.append(loopDelim);
            sb.append(String.valueOf(c));

            loopDelim = delim;
        }

        return sb.toString();
    }
}
