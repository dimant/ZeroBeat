package com.dtodorov.zerobeat.morse;

import java.util.HashMap;

/**
 * Created by diman on 7/9/2017.
 */

public class Encoder
{
    private ISignalGenerator signalGenerator;
    private HashMap<Character, String> alphabet;

    public Encoder(ISignalGenerator signalGenerator)
    {
        this.signalGenerator = signalGenerator;

        alphabet = new HashMap<>();
        alphabet.put('a', ".-");
        alphabet.put('b', "-...");
        alphabet.put('c', "-.-.");
        alphabet.put('d', "-..");
        alphabet.put('e', ".");
        alphabet.put('f', "..-.");
        alphabet.put('g', "--.");
        alphabet.put('h', "....");
        alphabet.put('i', "..");
        alphabet.put('j', ".---");
        alphabet.put('k', "-.-");
        alphabet.put('l', ".-..");
        alphabet.put('m', "--");
        alphabet.put('n', "-.");
        alphabet.put('o', "---");
        alphabet.put('p', ".--.");
        alphabet.put('q', "--.-");
        alphabet.put('r', ".-.");
        alphabet.put('s', "...");
        alphabet.put('t', "-");
        alphabet.put('u', "..-");
        alphabet.put('v', "...-");
        alphabet.put('w', ".--");
        alphabet.put('x', "-..-");
        alphabet.put('y', "-.--");
        alphabet.put('z', "--..");
        alphabet.put('0', "-----");
        alphabet.put('1', ".----");
        alphabet.put('2', "..---");
        alphabet.put('3', "...--");
        alphabet.put('4', "....-");
        alphabet.put('5', ".....");
        alphabet.put('6', "-....");
        alphabet.put('7', "--...");
        alphabet.put('8', "---..");
        alphabet.put('9', "----.");
        alphabet.put('.', ".-.-.-");
        alphabet.put(',', "--..--");
        alphabet.put('?', "..--..");
        alphabet.put('/', "-..-.");

        // 	SK End of contact
        //  KN Invitation
        //  AR New Page
        //  BT New Paragraph
    }

    public void encode(String text)
    {
        for(char c : text.toCharArray())
        {
            if(Character.compare(c, ' ') == 0)
            {
                signalGenerator.writeWordSpace();
            }
            else
            {
                String code = alphabet.get(c);
                if(code != null)
                {
                    for(char morse : code.toCharArray())
                    {
                        switch(morse)
                        {
                            case '-':
                                signalGenerator.writeDash();
                                break;
                            case '.':
                                signalGenerator.writeDot();
                                break;
                        }
                        signalGenerator.writeSymbolSpace();
                    }
                    signalGenerator.writeSymbolSpace();
                    signalGenerator.writeSymbolSpace();
                }
            }
        }
    }
}
