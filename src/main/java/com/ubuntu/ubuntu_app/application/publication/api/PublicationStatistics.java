package com.ubuntu.ubuntu_app.application.publication.api;

import java.time.LocalDate;

public record PublicationStatistics(
        String title,
        LocalDate date,
        Long visualizations) {
}
