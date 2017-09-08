package com.dtodorov.zerobeat.audio.morse;

/**
 * Created by diman on 7/9/2017.
 */

interface ISignalGenerator
{
    void writeDash();

    void writeDot();

    void writeSymbolSpace();

    void writeLetterSpace();
}
