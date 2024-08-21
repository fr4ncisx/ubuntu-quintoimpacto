package com.ubuntu.ubuntu_app.model.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.ubuntu.ubuntu_app.model.dto.BugDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Bugs")
public class BugEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 1000)
    private String description;
    private String category;
    private LocalDate date;
    private boolean fixed;
    
    public BugEntity(BugDTO bugDTO) {
        this.description = bugDTO.getDescription();
        this.category = bugDTO.getCategory();
        this.date = LocalDateTime.now().atZone(ZoneId.systemDefault()).toLocalDate();        
        this.fixed = false;
    }

    
}
