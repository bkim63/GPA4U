package com.bumjinkim.GPA4U;

import java.util.Random;

public class KimRandomCourseID {

    public static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static Random RANDOM = new Random();

    public static String randomString(int size) {
        StringBuilder stringBuilder = new StringBuilder(size);

        for (int i = 0; i < size; i++) {
            stringBuilder.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return stringBuilder.toString();
    }
}
