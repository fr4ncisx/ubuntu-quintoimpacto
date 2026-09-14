package com.ubuntu.ubuntu_app.application.user.service;

import com.ubuntu.ubuntu_app.application.user.port.in.UserUseCase;
import com.ubuntu.ubuntu_app.infrastructure.user.repository.UserRepository;
import com.ubuntu.ubuntu_app.shared.error.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.shared.api.ResponseMap;
import com.ubuntu.ubuntu_app.application.user.api.RegisterUserRequest;
import com.ubuntu.ubuntu_app.application.user.api.UserResponse;
import com.ubuntu.ubuntu_app.application.user.api.UpdateUserRequest;
import com.ubuntu.ubuntu_app.infrastructure.user.adapter.mapper.UserMapper;
import com.ubuntu.ubuntu_app.infrastructure.user.entity.UserEntity;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements UserUseCase {    

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public Map<String, String> register(RegisterUserRequest request) {
        UserEntity user = new UserEntity(request);
        userRepository.save(user);
        return ResponseMap.createResponse("Creado exitosamente");
    }
    
    @Transactional
    public Map<String, String> update(UpdateUserRequest request, String email) {
        verifyCanModifyUser(email);
        Optional<UserEntity> userObtained = userRepository.findByEmail(email);
        if (!userObtained.isPresent()) {
            throw new SqlEmptyResponse("El usuario no existe en la base de datos");
        }
        userObtained.get().updateFrom(request);
        return ResponseMap.createResponse("Usuario Modificado exitosamente");
    }

    private void verifyCanModifyUser(String targetEmail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
        Object principal = authentication != null ? authentication.getPrincipal() : null;
        String callerEmail = principal instanceof UserEntity user ? user.getEmail() : null;
        if (!isAdmin && (callerEmail == null || !callerEmail.equalsIgnoreCase(targetEmail))) {
            throw new AccessDeniedException("No autorizado a modificar otro usuario");
        }
    }

    @Transactional
    public Map<String, String> updateById(Long id, UpdateUserRequest request) {
        Optional<UserEntity> userObtained = userRepository.findById(id);
        if (!userObtained.isPresent()) {
            throw new SqlEmptyResponse("El usuario no existe en la base de datos");
        }
        verifyCanModifyUser(userObtained.get().getEmail());
        userObtained.get().updateFrom(request);
        return ResponseMap.createResponse("Usuario Modificado exitosamente");
    }

    @Transactional
    public Map<String, String> deactivate(Long idUserToDeactivate) {
        Optional<UserEntity> user = userRepository.findById(idUserToDeactivate);
        if (!user.isPresent()) {
            throw new SqlEmptyResponse("El usuario no existe en la base de datos");
        }
        var userFound = user.get();
        if (!userFound.isActive()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El usuario no se puede desactivar porque ya está desactivado");
        }
        userFound.setActive(false);
        userRepository.save(userFound);
        return ResponseMap.createResponse("Usuario desactivado exitosamente");
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(userMapper::toFetchDto)
        .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toFetchDto);
    }

    public String [] findAdminEmails(){
        List<String> adminEmails = userRepository.findAdminEmails();
        return adminEmails.toArray(new String[0]);
    }

    @Transactional(readOnly = true)
    public UpdateUserRequest findByEmail(String email) {
        var userFound = userRepository.findByEmail(email);
        if(!userFound.isPresent()){
            throw new SqlEmptyResponse("No user found with email: " + email);
        }
        return userMapper.toUpdateDto(userFound.get());
    }

}
