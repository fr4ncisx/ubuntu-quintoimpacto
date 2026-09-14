package com.ubuntu.ubuntu_app.infrastructure.publication.entity;

import java.time.LocalDate;
import java.util.List;

import com.ubuntu.ubuntu_app.application.publication.api.CreatePublicationRequest;
import com.ubuntu.ubuntu_app.application.publication.api.UpdatePublicationRequest;
import com.ubuntu.ubuntu_app.infrastructure.media.entity.ImageEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "publications")
public class PublicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 2000)
    private String description;
    @Column(name = "created_at")
    private LocalDate date;
    private boolean active; 
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "publication_id",referencedColumnName = "id")
    private List<ImageEntity> images;

    /**
     * Creates a publication using dto and a converted List of ImageEntity in service
     * @param publicationsDTO
     * @param imagenes
     */
    public PublicationEntity(CreatePublicationRequest publicationsDTO, List<ImageEntity> images) {
        this.title = publicationsDTO.title();
        this.description = publicationsDTO.description();
        this.date = LocalDate.now();
        this.active = true;
        this.images = images;
    }

    /**
     * Update the publication by receiving dto and list of img if required
     * @param publicationsDTO
     * @param imageEntity
     */
    public void edit(UpdatePublicationRequest publicationsDTO, List<ImageEntity> images){
        this.title = publicationsDTO.title();
        this.description = publicationsDTO.description();
        this.images = images;
    }
}