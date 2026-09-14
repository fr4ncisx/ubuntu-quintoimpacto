package com.ubuntu.ubuntu_app.infrastructure.user.adapter.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubuntu.ubuntu_app.application.user.UserRole;
import com.ubuntu.ubuntu_app.infrastructure.user.entity.UserEntity;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    private UserEntity entity() {
        UserEntity entity = new UserEntity();
        entity.setId(7L);
        entity.setFirstName("Nombre");
        entity.setLastName("Apellido");
        entity.setEmail("me@mail.com");
        entity.setActive(true);
        entity.setRole(UserRole.USER);
        entity.setPhone("123");
        entity.setImage("http://img");
        entity.setSubscribed(true);
        return entity;
    }

    @Test
    void mapsToFetchDto() {
        var dto = mapper.toFetchDto(entity());

        assertEquals(7L, dto.id());
        assertEquals("me@mail.com", dto.email());
        assertEquals(UserRole.USER, dto.role());
        assertTrue(dto.active());
    }

    @Test
    void mapsToUpdateDto() {
        var dto = mapper.toUpdateDto(entity());

        assertEquals("Nombre", dto.firstName());
        assertEquals("Apellido", dto.lastName());
        assertEquals("123", dto.phone());
        assertEquals("http://img", dto.image());
        assertTrue(dto.subscribed());
    }
}
