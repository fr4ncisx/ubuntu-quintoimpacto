package com.ubuntu.ubuntu_app.model.entities;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ubuntu.ubuntu_app.configuration.MapperConverter;
import com.ubuntu.ubuntu_app.model.dto.UserDto;
import com.ubuntu.ubuntu_app.model.dto.UserGoogleDTO;
import com.ubuntu.ubuntu_app.model.dto.UserUpdateDTO;
import com.ubuntu.ubuntu_app.model.enums.UserRole;
import com.ubuntu.ubuntu_app.model.generator.RandomPhoneGenerator;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Usuarios")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private boolean activo;
    @Enumerated(EnumType.STRING)
    private UserRole rol;
    private String telefono;

    public UserEntity(String nombre, String apellido, String email, UserRole rol, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rol = rol;
        activo = true;
        this.telefono = telefono;
    }

    public UserEntity(UserDto userDto) {
        nombre = userDto.getNombre();
        apellido = userDto.getApellido();
        email = userDto.getEmail();
        activo = true;
        rol = UserRole.ADMIN;
        telefono = RandomPhoneGenerator.create();
    }

    public UserEntity(UserGoogleDTO newLocalUser) {
        nombre = newLocalUser.getName();
        apellido = newLocalUser.getLastName();
        email = newLocalUser.getEmail();
        activo = true;
        rol = UserRole.USER;
        telefono = RandomPhoneGenerator.create();
    }

    public UserEntity editUser(UserUpdateDTO userDto) {
        if (!userDto.getNombre().isBlank()) {
            nombre = userDto.getNombre();
        }
        if (!userDto.getApellido().isBlank()) {
            apellido = userDto.getApellido();
        }
        if (!userDto.getEmail().isBlank()) {
            email = userDto.getEmail();
        }
        if (!userDto.getTelefono().isBlank()) {
            telefono = userDto.getTelefono();
        }
        if (userDto.getRol() != null) {
            rol = userDto.getRol();
        }
        activo = userDto.isActivo();
        return MapperConverter.generate().map(userDto, UserEntity.class);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    @Deprecated(forRemoval = true)
    public String getPassword() {
        return null;
    }

    @Override
    @Deprecated(forRemoval = true)
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return activo;
    }
}
