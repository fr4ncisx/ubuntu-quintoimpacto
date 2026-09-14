package com.ubuntu.ubuntu_app.infrastructure.contact.adapter.out;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.ubuntu.ubuntu_app.application.contact.port.out.ContactRequestRepositoryPort;
import com.ubuntu.ubuntu_app.infrastructure.contact.entity.ContactRequestEntity;
import com.ubuntu.ubuntu_app.infrastructure.contact.repository.ContactRequestRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ContactRequestJpaAdapter implements ContactRequestRepositoryPort {

    private final ContactRequestRepository repository;

    @Override
    public ContactRequestEntity save(ContactRequestEntity request) {
        return repository.save(request);
    }

    @Override
    public List<ContactRequestEntity> findByReviewedTrue() {
        return repository.findByReviewedTrue();
    }

    @Override
    public List<ContactRequestEntity> findByReviewedFalse() {
        return repository.findByReviewedFalse();
    }

    @Override
    public Page<ContactRequestEntity> findByReviewedTrue(Pageable pageable) {
        return repository.findByReviewedTrue(pageable);
    }

    @Override
    public Page<ContactRequestEntity> findByReviewedFalse(Pageable pageable) {
        return repository.findByReviewedFalse(pageable);
    }

    @Override
    public Optional<ContactRequestEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Long countByReviewedAndMonth(boolean reviewed, int month, int year) {
        return repository.findByStatisticsContact(reviewed, month, year);
    }
}
