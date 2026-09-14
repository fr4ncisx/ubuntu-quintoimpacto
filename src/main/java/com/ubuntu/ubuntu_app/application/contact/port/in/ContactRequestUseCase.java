package com.ubuntu.ubuntu_app.application.contact.port.in;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ubuntu.ubuntu_app.application.contact.api.CreateContactRequest;
import com.ubuntu.ubuntu_app.application.contact.api.ContactRequestResponse;
import com.ubuntu.ubuntu_app.application.contact.api.ContactRequestSummary;

public interface ContactRequestUseCase {

    void create(CreateContactRequest requestMessage, Long microId);

    List<ContactRequestSummary> findReviewed();

    List<ContactRequestSummary> findUnreviewed();

    Page<ContactRequestSummary> findReviewed(Pageable pageable);

    Page<ContactRequestSummary> findUnreviewed(Pageable pageable);

    void markReviewed(Long id);

    ContactRequestResponse findById(Long id);

    Map<String, Long> countByStatistics();
}
