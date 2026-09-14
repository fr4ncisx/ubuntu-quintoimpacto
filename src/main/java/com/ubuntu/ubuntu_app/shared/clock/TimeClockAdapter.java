package com.ubuntu.ubuntu_app.shared.clock;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.ubuntu.ubuntu_app.shared.clock.ClockPort;

@Component
public class TimeClockAdapter implements ClockPort {

    @Override
    public int currentMonth() {
        return LocalDate.now().getMonthValue();
    }

    @Override
    public int currentYear() {
        return LocalDate.now().getYear();
    }
}
