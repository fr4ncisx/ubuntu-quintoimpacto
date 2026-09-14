package com.ubuntu.ubuntu_app.infrastructure.publication.entity;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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

    public PublicationViewEntity() {
    }

    public PublicationViewEntity(Long id, LocalDate clickDate, PublicationEntity publicationsEntity) {
        this.id = id;
        this.clickDate = clickDate;
        this.publicationsEntity = publicationsEntity;
    }

    public PublicationViewEntity(PublicationEntity publicationsEntity) {
        this.clickDate = LocalDate.now();
        this.publicationsEntity = publicationsEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getClickDate() {
        return clickDate;
    }

    public void setClickDate(LocalDate clickDate) {
        this.clickDate = clickDate;
    }

    public PublicationEntity getPublicationsEntity() {
        return publicationsEntity;
    }

    public void setPublicationsEntity(PublicationEntity publicationsEntity) {
        this.publicationsEntity = publicationsEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PublicationViewEntity that = (PublicationViewEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "PublicationViewEntity{id=" + id + ", clickDate=" + clickDate + "}";
    }
}
