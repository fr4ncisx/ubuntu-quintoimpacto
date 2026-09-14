package com.ubuntu.ubuntu_app.application.publication.port.out;

import com.ubuntu.ubuntu_app.infrastructure.publication.entity.PublicationViewEntity;

public interface PublicationViewRepositoryPort {

    PublicationViewEntity save(PublicationViewEntity view);

    Long getClickCountActualMonth(Long id, int month, int year);
}
