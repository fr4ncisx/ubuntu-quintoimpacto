package com.ubuntu.ubuntu_app.infrastructure.user.entity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ubuntu.ubuntu_app.application.user.UserRole;
import com.ubuntu.ubuntu_app.application.user.api.GoogleUserProfile;
import com.ubuntu.ubuntu_app.application.user.api.RegisterUserRequest;
import com.ubuntu.ubuntu_app.application.user.api.UpdateUserRequest;
import com.ubuntu.ubuntu_app.shared.support.RandomPhoneGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
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

    public UserEntity() {
    }

    public UserEntity(Long id, String firstName, String lastName, String email, boolean active, UserRole role,
            String phone, String image, Boolean subscribed) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.active = active;
        this.role = role;
        this.phone = phone;
        this.image = image;
        this.subscribed = subscribed;
    }

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

    public UserEntity(RegisterUserRequest userDto) {
        firstName = userDto.firstName();
        lastName = userDto.lastName();
        email = userDto.email();
        active = true;
        role = UserRole.ADMIN;
        phone = RandomPhoneGenerator.create();
        subscribed = true;
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserEntity that = (UserEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserEntity{id=" + id + ", firstName='" + firstName + "', lastName='" + lastName + "', email='"
                + email + "', active=" + active + ", role=" + role + ", subscribed=" + subscribed + "}";
    }
}
