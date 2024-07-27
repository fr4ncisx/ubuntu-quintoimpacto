package com.ubuntu.ubuntu_app.model.generator;

import java.util.Random;

public class LastNameGenerator {
    private static String lastName;

    public static String obtainRandomName() {
        create();
        return lastName;
    }

    private static void create() {
        Random rnd = new Random();
        int number = rnd.nextInt(1, 14);
        switch (number) {
            case 1:
                lastName = "Solis";
                break;
            case 2:
                lastName = "Delgado";
                break;
            case 3:
                lastName = "Martinez";
                break;
            case 4:
                lastName = "Altamirano";
                break;
            case 5:
                lastName = "Ortiz";
                break;
            case 6:
                lastName = "Oviedo";
                break;
            case 7:
                lastName = "Ledesma";
                break;
            case 8:
                lastName = "Quiroga";
                break;
            case 9:
                lastName = "Hernández";
                break;
            case 10:
                lastName = "Espinosa";
                break;
            case 11:
                lastName = "Jurado";
                break;
            case 12:
                lastName = "Núñez";
                break;
            case 13:
                lastName = "Medina";
                break;
        }
    }
}
