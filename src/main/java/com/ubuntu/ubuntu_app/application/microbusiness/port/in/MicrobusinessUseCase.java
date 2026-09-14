package com.ubuntu.ubuntu_app.application.microbusiness.port.in;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ubuntu.ubuntu_app.application.microbusiness.api.MicrobusinessCategorySummary;
import com.ubuntu.ubuntu_app.application.microbusiness.api.CreateMicrobusinessRequest;
import com.ubuntu.ubuntu_app.application.microbusiness.api.UpdateMicrobusinessRequest;
import com.ubuntu.ubuntu_app.application.microbusiness.api.NearbyMicrobusinessResponse;
import com.ubuntu.ubuntu_app.application.microbusiness.api.MicrobusinessSummary;

public interface MicrobusinessUseCase {

    void create(CreateMicrobusinessRequest request);

    void update(UpdateMicrobusinessRequest request, Long id);

    List<MicrobusinessSummary> findByName(String name);

    Page<MicrobusinessSummary> findByName(String name, Pageable pageable);

    List<MicrobusinessCategorySummary> findAll(String category);

    Page<MicrobusinessCategorySummary> findAll(String category, Pageable pageable);

    void setVisibility(Long id, boolean enable);

    void delete(Long id);

    List<MicrobusinessSummary> findAllActive();

    Page<MicrobusinessSummary> findAllActive(Pageable pageable);

    Long countByCurrentMonth();

    Map<String, Long> countByCategoryCurrentMonth();

    List<CreateMicrobusinessRequest> findUnmailed();

    List<MicrobusinessSummary> findByActive(boolean active);

    Page<MicrobusinessSummary> findByActive(boolean active, Pageable pageable);

    List<NearbyMicrobusinessResponse> findNearby(double lat, double lon);
}
