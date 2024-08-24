package com.ubuntu.ubuntu_app.service;

import java.time.LocalDate;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ubuntu.ubuntu_app.Repository.BugRepository;
import com.ubuntu.ubuntu_app.configuration.MapperConverter;
import com.ubuntu.ubuntu_app.infra.errors.IllegalRewriteException;
import com.ubuntu.ubuntu_app.infra.errors.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.infra.statuses.ResponseMap;
import com.ubuntu.ubuntu_app.model.dto.BugDTO;
import com.ubuntu.ubuntu_app.model.dto.FixedBugDTO;
import com.ubuntu.ubuntu_app.model.dto.UnfixedBugDTO;
import com.ubuntu.ubuntu_app.model.entities.BugEntity;

import lombok.RequiredArgsConstructor;

@Lazy
@RequiredArgsConstructor
@Service
public class BugService {

    private final BugRepository bugRepository;

    public ResponseEntity<?> getAll() {
        var listOfBugs = bugRepository.findByFixedFalseOrderByDateAsc();
        if(listOfBugs.isEmpty()){
            throw new SqlEmptyResponse("There are no unfixed bugs");
        }
        return ResponseEntity.ok(listOfBugs.stream().map(f -> MapperConverter.generate().map(f, UnfixedBugDTO.class)).toList());
    }

    @Transactional
    public ResponseEntity<?> createBug(BugDTO bugDTO) {
        bugRepository.save(new BugEntity(bugDTO));
        return ResponseEntity.ok(ResponseMap.createResponse("Bug documented"));
    }

    @Transactional
    public ResponseEntity<?> updateBugStatus(Long id) {
        var optionalBug = bugRepository.findById(id);
        if(!optionalBug.isPresent()){
            throw new SqlEmptyResponse("Bug id doesnt match with any one on database");
        }
        var bugFromRepository = optionalBug.get();
        if(bugFromRepository.isFixed()){
            throw new IllegalRewriteException("Bug is already fixed");
        }
        bugFromRepository.setFixed(true);
        bugFromRepository.setFixedDate(LocalDate.now());
        bugRepository.save(bugFromRepository);
        return ResponseEntity.ok(ResponseMap.createResponse("Bug id #" + id + " mark as fixed"));
    }

    public ResponseEntity<?> getFixedBugs() {
        var fixedBugs = bugRepository.findByFixedTrueOrderByFixedDateDesc();
        if(fixedBugs.isEmpty()){
            throw new SqlEmptyResponse("There are not bugs to display");
        }
        return ResponseEntity.ok(fixedBugs.stream().map(fix -> MapperConverter.generate().map(fix, FixedBugDTO.class)).toList());
    }
}
