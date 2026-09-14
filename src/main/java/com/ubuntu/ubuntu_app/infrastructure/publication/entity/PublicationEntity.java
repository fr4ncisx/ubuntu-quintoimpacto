package com.ubuntu.ubuntu_app.infrastructure.publication.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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
    @JoinColumn(name = "publication_id", referencedColumnName = "id")
    private List<ImageEntity> images;

    public PublicationEntity() {
    }

    public PublicationEntity(Long id, String title, String description, LocalDate date, boolean active,
            List<ImageEntity> images) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.active = active;
        this.images = images;
    }

    public PublicationEntity(CreatePublicationRequest publicationsDTO, List<ImageEntity> images) {
        this.title = publicationsDTO.title();
        this.description = publicationsDTO.description();
        this.date = LocalDate.now();
        this.active = true;
        this.images = images;
    }

    public void edit(UpdatePublicationRequest publicationsDTO, List<ImageEntity> images) {
        this.title = publicationsDTO.title();
        this.description = publicationsDTO.description();
        this.images = images;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<ImageEntity> getImages() {
        return images;
    }

    public void setImages(List<ImageEntity> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PublicationEntity that = (PublicationEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "PublicationEntity{id=" + id + ", title='" + title + "', date=" + date + ", active=" + active + "}";
    }
}
