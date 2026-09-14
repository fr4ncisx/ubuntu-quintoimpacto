package com.ubuntu.ubuntu_app.infrastructure.microbusiness.entity;

import java.time.LocalDate;
import java.util.List;

import com.ubuntu.ubuntu_app.application.microbusiness.api.CreateMicrobusinessRequest;
import com.ubuntu.ubuntu_app.application.microbusiness.api.UpdateMicrobusinessRequest;
import com.ubuntu.ubuntu_app.infrastructure.category.entity.CategoryEntity;
import com.ubuntu.ubuntu_app.infrastructure.media.entity.ImageEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "microbusinesses")
public class MicrobusinessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 300)
    private String description;
    @Column(name = "more_info", length = 300)
    private String moreInfo;
    @Column(name = "created_at")
    private LocalDate createdDate;
    private String country;
    private String province;
    private String city;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
    private String subcategory;
    private boolean active;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "microbusiness_id",referencedColumnName ="id")
    private List<ImageEntity> images;
    @Column(nullable = true)
    private Boolean  mailed;

    public MicrobusinessEntity(CreateMicrobusinessRequest request, CategoryEntity category, List<ImageEntity> images) {
        this.name = request.name();
        this.description = request.description();
        this.moreInfo = request.moreInfo();
        this.createdDate = LocalDate.now();
        this.country = request.country();
        this.province = request.province();
        this.city = request.city();
        this.category = category;
        this.subcategory = request.subcategory();
        this.active = true;
        this.images = images;
        this.mailed = false;
    }

    public void edit(UpdateMicrobusinessRequest request, CategoryEntity category, List<ImageEntity> images) {
        this.name = request.name();
        this.description = request.description();
        this.moreInfo = request.moreInfo();
        this.country = request.country();
        this.province = request.province();
        this.city = request.city();
        this.category = category;
        this.subcategory = request.subcategory();
        this.images = images;
    }
}
