package com.ubuntu.ubuntu_app.model.entities;

import com.ubuntu.ubuntu_app.model.dto.PaisDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Pais")
public class PaisEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_pais")
    private List<ProvinciaEntity> provincias;    

    public PaisEntity(String nombre, List<ProvinciaEntity> provincias) {
        this.nombre = nombre;
        this.provincias = provincias;
    }

    public PaisEntity(PaisDto paisDto){
        this.nombre = paisDto.getNombre();
    }
}
