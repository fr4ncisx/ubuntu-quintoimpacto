package com.ubuntu.ubuntu_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ubuntu.ubuntu_app.model.dto.BugDTO;
import com.ubuntu.ubuntu_app.service.BugService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequiredArgsConstructor
@RestController
@RequestMapping("/bug")
public class BugController {

    private final BugService bugService;

    @GetMapping("/find")
    public ResponseEntity<?> getAllBugs() {
        return bugService.getAll();
    }
    @Transactional(readOnly = false)
    @PostMapping("/admin/create")
    public ResponseEntity<?> newBug(@Valid @RequestBody BugDTO bugDTO) {
        return bugService.createBug(bugDTO);
    }
    /*@Transactional(readOnly = false)
    @DeleteMapping("/admin/delete")
    public ResponseEntity<?> deleteBug() {
        return bugService.deleteBug();
    }*/
    @Transactional(readOnly = false)
    @PutMapping("/admin/hide")
    public ResponseEntity<?> fixedBug(@RequestParam Long id) {
        return bugService.updateBugStatus(id);
    }

}
