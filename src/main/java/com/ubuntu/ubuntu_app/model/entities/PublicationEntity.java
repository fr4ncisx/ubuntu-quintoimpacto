package com.ubuntu.ubuntu_app.model.entities;

import java.time.LocalDate;
import java.util.List;

import com.ubuntu.ubuntu_app.model.dto.PublicationRequestDTO;

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
@Table(name = "Publicaciones")
public class PublicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 2000)
    private String description;
    @Column(name = "fecha_creacion")
    private LocalDate date;
    private boolean active; 
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_publicacion",referencedColumnName = "id")
    private List<ImageEntity> imagenes;

    /**
     * Creates a publication using dto and a converted List of ImageEntity in service
     * @param publicationsDTO
     * @param imagenes
     */
    public PublicationEntity(PublicationRequestDTO publicationsDTO, List<ImageEntity> imagenes) {
        this.title = publicationsDTO.getTitle();
        this.description = publicationsDTO.getDescription();
        this.date = LocalDate.now();
        this.active = true;
        this.imagenes = imagenes;
    }

    /**
     * Update the publication by receiving dto and list of img if required
     * @param publicationsDTO
     * @param imageEntity
     */
    public void edit(PublicationRequestDTO publicationsDTO, List<ImageEntity> imageEntity){
        this.title = publicationsDTO.getTitle();
        this.description = publicationsDTO.getDescription();
        this.imagenes = imageEntity;
    }
}