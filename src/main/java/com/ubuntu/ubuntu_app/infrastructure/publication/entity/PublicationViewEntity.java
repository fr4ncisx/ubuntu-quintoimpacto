package com.ubuntu.ubuntu_app.infrastructure.publication.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "publication_views")
public class PublicationViewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "viewed_at")
    private LocalDate clickDate;
    @ManyToOne
    @JoinColumn(name = "publication_id")
    private PublicationEntity publicationsEntity;

    public PublicationViewEntity(PublicationEntity publicationsEntity) {
        this.clickDate = LocalDate.now();
        this.publicationsEntity = publicationsEntity;
    }    
}
