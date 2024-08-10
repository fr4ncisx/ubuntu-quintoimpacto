package com.ubuntu.ubuntu_app.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ubuntu.ubuntu_app.Repository.BugRepository;
import com.ubuntu.ubuntu_app.infra.errors.IllegalRewriteException;
import com.ubuntu.ubuntu_app.infra.errors.SQLemptyDataException;
import com.ubuntu.ubuntu_app.infra.statuses.ResponseMap;
import com.ubuntu.ubuntu_app.model.dto.BugDTO;
import com.ubuntu.ubuntu_app.model.entities.BugEntity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BugService {

    private final BugRepository bugRepository;

    public ResponseEntity<?> getAll() {
        var listOfBugs = bugRepository.findAll();
        if(listOfBugs.isEmpty()){
            throw new SQLemptyDataException("No bugs are documented");
        }
        return ResponseEntity.ok(listOfBugs);
    }

    public ResponseEntity<?> createBug(BugDTO bugDTO) {
        bugRepository.save(new BugEntity(bugDTO));
        return ResponseEntity.ok(ResponseMap.createResponse("Bug documented"));
    }

    public ResponseEntity<?> updateBugStatus(Long id) {
        var optionalBug = bugRepository.findById(id);
        if(!optionalBug.isPresent()){
            throw new SQLemptyDataException("Bug id doesnt match with any one on database");
        }
        var bugFromRepository = optionalBug.get();
        if(bugFromRepository.isFixed()){
            throw new IllegalRewriteException("Bug is already fixed");
        }
        bugFromRepository.setFixed(true);
        bugRepository.save(bugFromRepository);
        return ResponseEntity.ok(ResponseMap.createResponse("Bug id #" + id + " mark as fixed"));
    }
}
