package com.wafflestudio.babble.common.utils;

import java.util.Random;

public class RandomUtils {

    private static final int ASCII_ZERO = 48;
    private static final int ASCII_NINE = 57;
    private static final int ASCII_UPPER_A = 65;
    private static final int ASCII_UPPER_Z = 90;
    private static final int ASCII_LOWER_A = 97;
    private static final int ASCII_LOWER_Z = 122;
    private static final Random random = new Random();

    public static String generateRandomAlphanumericString(int length) {
        return random.ints(ASCII_ZERO, ASCII_LOWER_Z + 1)
            .filter(i -> (i <= ASCII_NINE || i >= ASCII_UPPER_A) && (i <= ASCII_UPPER_Z || i >= ASCII_LOWER_A))
            .limit(length)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }
}
