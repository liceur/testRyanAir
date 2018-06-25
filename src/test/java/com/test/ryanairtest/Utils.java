package com.test.ryanairtest;

import java.nio.charset.Charset;
import java.util.Random;

public class Utils {

    public static String generateString() {
        byte[] array = new byte[3]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));

        return generatedString;
    }
}
