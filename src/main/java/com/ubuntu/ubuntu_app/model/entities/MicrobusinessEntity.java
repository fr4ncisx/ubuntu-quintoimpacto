package com.ubuntu.ubuntu_app.model.entities;

import java.time.LocalDate;
import java.util.List;
import com.ubuntu.ubuntu_app.model.dto.MicrobusinessDTO;

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
@Table(name = "Microemprendimientos")
public class MicrobusinessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @Column(length = 300)
    private String descripcion;
    @Column(name = "mas_informacion", length = 300)
    private String masInformacion;
    @Column(name = "fecha_creacion")
    private LocalDate fecha;
    private String pais;
    private String provincia;
    private String ciudad;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_categoria")
    private CategoryEntity categoria;
    private String subcategoria;
    private boolean activo;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_micro",referencedColumnName ="id")
    private List<ImageEntity> imagenes;
    @Column(nullable = true)
    private Boolean  enviadoPorMail;

    public MicrobusinessEntity(MicrobusinessDTO microbusinessDTO, CategoryEntity categoria, List<ImageEntity> imagenes) {
        this.nombre = microbusinessDTO.getNombre();
        this.descripcion = microbusinessDTO.getDescripcion();
        this.masInformacion = microbusinessDTO.getMasInformacion();
        this.fecha = LocalDate.now();
        this.pais = microbusinessDTO.getPais();
        this.provincia = microbusinessDTO.getProvincia();
        this.ciudad = microbusinessDTO.getCiudad();
        this.categoria = categoria;
        this.subcategoria = microbusinessDTO.getSubcategoria();
        this.activo = true;
        this.imagenes = imagenes;
        this.enviadoPorMail = false;
    }

    public void edit(MicrobusinessDTO microbusinessDTO, CategoryEntity categoria, List<ImageEntity> imagenes) {
        this.nombre = microbusinessDTO.getNombre();
        this.descripcion = microbusinessDTO.getDescripcion();
        this.masInformacion = microbusinessDTO.getMasInformacion();
        this.pais = microbusinessDTO.getPais();
        this.provincia = microbusinessDTO.getProvincia();
        this.ciudad = microbusinessDTO.getCiudad();
        this.categoria = categoria;
        this.subcategoria = microbusinessDTO.getSubcategoria();
        this.imagenes = imagenes;
    }
}
