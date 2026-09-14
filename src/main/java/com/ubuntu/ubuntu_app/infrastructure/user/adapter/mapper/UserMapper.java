package com.ubuntu.ubuntu_app.infrastructure.user.adapter.mapper;

import org.mapstruct.Mapper;

import com.ubuntu.ubuntu_app.application.user.api.UserResponse;
import com.ubuntu.ubuntu_app.application.user.api.UpdateUserRequest;
import com.ubuntu.ubuntu_app.infrastructure.user.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toFetchDto(UserEntity entity);

    UpdateUserRequest toUpdateDto(UserEntity entity);
}
