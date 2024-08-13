package com.ubuntu.ubuntu_app.infra.date;

import java.time.LocalDate;

public class GlobalDate {
    private static int currentMonth = LocalDate.now().getMonthValue();
    private static int currentYear = LocalDate.now().getYear();

    public static int getCurrentMonth() {
        return currentMonth;
    }

    public static int getCurrentYear() {
        return currentYear;
    }

}
