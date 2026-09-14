package com.ubuntu.ubuntu_app.infrastructure.country.entity;

import com.ubuntu.ubuntu_app.infrastructure.province.entity.ProvinceEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "countries")
public class CountryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id")
    private List<ProvinceEntity> provinces;

    public CountryEntity(String name, List<ProvinceEntity> provinces) {
        this.name = name;
        this.provinces = provinces;
    }
}
