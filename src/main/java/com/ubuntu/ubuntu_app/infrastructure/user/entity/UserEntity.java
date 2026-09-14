package com.ubuntu.ubuntu_app.infrastructure.user.entity;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ubuntu.ubuntu_app.application.user.api.RegisterUserRequest;
import com.ubuntu.ubuntu_app.application.user.api.GoogleUserProfile;
import com.ubuntu.ubuntu_app.application.user.api.UpdateUserRequest;
import com.ubuntu.ubuntu_app.application.user.UserRole;
import com.ubuntu.ubuntu_app.shared.support.RandomPhoneGenerator;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean active;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private String phone;
    @Column(length = 600, nullable = true)
    private String image;
    @Column(nullable = true)
    private Boolean subscribed;

    /**
     * <p>Se usa solo para crear el seeder</p>
     */
    public UserEntity(String firstName, String lastName, String email, UserRole role, String phone, String image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        active = true;
        this.phone = phone;
        this.image = image;
        subscribed = true;
    }

    /**
     * DEPRECATED
     * <p>Se usa solo para el endpoint /register</p>
     */
    public UserEntity(RegisterUserRequest userDto) {
        firstName = userDto.firstName();
        lastName = userDto.lastName();
        email = userDto.email();
        active = true;
        role = UserRole.ADMIN;
        phone = RandomPhoneGenerator.create();
        subscribed = true;
    }

    /**
     * <p>
     * Se usa para crear cuentas en base de datos desde google payload
     * </p>
     *
     * @param GoogleUserProfile user
     *
     */
    public UserEntity(GoogleUserProfile newLocalUser) {
        firstName = newLocalUser.firstName();
        lastName = newLocalUser.lastName();
        email = newLocalUser.email();
        active = true;
        role = UserRole.USER;
        phone = RandomPhoneGenerator.create();
        image = newLocalUser.profileImage();
        subscribed = true;
    }

    public void updateFrom(UpdateUserRequest userDto) {
            firstName = userDto.firstName();
            lastName = userDto.lastName();
            phone = userDto.phone();
            subscribed = userDto.subscribed();
            this.image = userDto.image();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
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
        return active;
    }
}
