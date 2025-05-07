package com.ubuntu.ubuntu_app.infra.date;

import lombok.Getter;

import java.time.LocalDate;

public class GlobalDate {

    private GlobalDate(){}

    @Getter
    private static final int MONTH = LocalDate.now().getMonthValue();
    @Getter
    private static final int YEAR = LocalDate.now().getYear();
}
