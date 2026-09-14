package com.ubuntu.ubuntu_app.application.publication.port.in;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ubuntu.ubuntu_app.application.publication.api.PublicationResponse;
import com.ubuntu.ubuntu_app.application.publication.api.CreatePublicationRequest;
import com.ubuntu.ubuntu_app.application.publication.api.UpdatePublicationRequest;
import com.ubuntu.ubuntu_app.application.publication.api.PublicationStatistics;

public interface PublicationUseCase {

    void create(CreatePublicationRequest publicationsDTO);

    void update(UpdatePublicationRequest publicationsDTO, Long id);

    PublicationResponse findById(Long id);

    void setVisibility(Long id, boolean enable);

    void registerView(Long id);

    List<PublicationStatistics> getStatistics(Long limitSize);

    List<PublicationResponse> search(String publication);

    Page<PublicationResponse> search(String publication, Pageable pageable);

    List<PublicationResponse> findAll(boolean active);

    Page<PublicationResponse> findAll(boolean active, Pageable pageable);

    void delete(Long id);
}
