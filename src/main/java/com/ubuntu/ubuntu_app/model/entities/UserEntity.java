package com.ubuntu.ubuntu_app.model.entities;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ubuntu.ubuntu_app.infra.errors.IllegalParameterException;
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
    @Column(length = 300, nullable = true)
    private String imagen;
    @Column(nullable = true)
    private Boolean suscribed;

    /**
     * <p>Se usa solo para crear el seeder</p>
     */
    public UserEntity(String nombre, String apellido, String email, UserRole rol, String telefono, String imagen) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rol = rol;
        activo = true;
        this.telefono = telefono;
        this.imagen = imagen;
        suscribed = true;
    }

    /**
     * DEPRECATED
     * <p>Se usa solo para el endpoint /register</p>
     */
    public UserEntity(UserDto userDto) {
        nombre = userDto.getNombre();
        apellido = userDto.getApellido();
        email = userDto.getEmail();
        activo = true;
        rol = UserRole.ADMIN;
        telefono = RandomPhoneGenerator.create();
        suscribed = true;
    }

    /**
     * <p>
     * Se usa para crear cuentas en base de datos desde google payload
     * </p>
     * 
     * @param UserGoogleDTO user
     * 
     */
    public UserEntity(UserGoogleDTO newLocalUser) {
        nombre = newLocalUser.getName();
        apellido = newLocalUser.getLastName();
        email = newLocalUser.getEmail();
        activo = true;
        rol = UserRole.USER;
        telefono = RandomPhoneGenerator.create();
        imagen = newLocalUser.getProfileImg();
        suscribed = true;
    }

    public void editUser(UserUpdateDTO userDto) {
            nombre = userDto.getNombre();     
            apellido = userDto.getApellido();       
            telefono = userDto.getTelefono();
            if(userDto.isSuscribed() == getSuscribed()){
                throw new IllegalParameterException("No se puede cambiar el estado de suscripción porque es el mismo");
            }
            this.imagen = userDto.getImagen();
            suscribed = userDto.isSuscribed();
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
