package com.ubuntu.ubuntu_app.application.user.port.in;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ubuntu.ubuntu_app.application.user.api.RegisterUserRequest;
import com.ubuntu.ubuntu_app.application.user.api.UserResponse;
import com.ubuntu.ubuntu_app.application.user.api.UpdateUserRequest;

public interface UserUseCase {

    Map<String, String> register(RegisterUserRequest userDto);

    Map<String, String> update(UpdateUserRequest userDto, String email);

    Map<String, String> updateById(Long id, UpdateUserRequest userDto);

    Map<String, String> deactivate(Long idUserToDeactivate);

    List<UserResponse> findAll();

    Page<UserResponse> findAll(Pageable pageable);

    UpdateUserRequest findByEmail(String email);

    String[] findAdminEmails();
}
