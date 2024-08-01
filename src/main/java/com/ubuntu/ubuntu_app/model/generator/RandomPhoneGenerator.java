package com.ubuntu.ubuntu_app.model.generator;

import java.util.Random;

public class RandomPhoneGenerator {

    public static String create() {
        return generatePhone();
    }

    private static String generatePhone() {
        Random rnd = new Random();
        Long codigoArea = rnd.nextLong(300L, 501L);
        return "+54 9 " + String.valueOf(codigoArea) + " "
                + String.valueOf(rnd.nextLong(1111L, 9999L) + " " + String.valueOf(rnd.nextLong(1111L, 9999L)));
    }
}
