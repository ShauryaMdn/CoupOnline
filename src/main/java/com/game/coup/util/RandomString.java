package com.game.coup.util;

import java.security.SecureRandom;

public class RandomString {

    private static final String digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom random = new SecureRandom();

    public static String getRandomString() {
        StringBuilder stringBuilder = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            stringBuilder.append(digits.charAt(random.nextInt(digits.length())));
        }
        return stringBuilder.toString();
    }
}
