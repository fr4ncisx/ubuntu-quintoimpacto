package com.ubuntu.ubuntu_app.infrastructure.microbusiness.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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

@Entity
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
    @JoinColumn(name = "microbusiness_id", referencedColumnName = "id")
    private List<ImageEntity> images;
    @Column(nullable = true)
    private Boolean mailed;

    public MicrobusinessEntity() {
    }

    public MicrobusinessEntity(Long id, String name, String description, String moreInfo, LocalDate createdDate,
            String country, String province, String city, CategoryEntity category, String subcategory,
            boolean active, List<ImageEntity> images, Boolean mailed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.moreInfo = moreInfo;
        this.createdDate = createdDate;
        this.country = country;
        this.province = province;
        this.city = city;
        this.category = category;
        this.subcategory = subcategory;
        this.active = active;
        this.images = images;
        this.mailed = mailed;
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
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

    public Boolean getMailed() {
        return mailed;
    }

    public void setMailed(Boolean mailed) {
        this.mailed = mailed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MicrobusinessEntity that = (MicrobusinessEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MicrobusinessEntity{id=" + id + ", name='" + name + "', country='" + country + "', province='"
                + province + "', city='" + city + "', subcategory='" + subcategory + "', active=" + active
                + ", mailed=" + mailed + "}";
    }
}
