package com.ubuntu.ubuntu_app.service;

import com.ubuntu.ubuntu_app.Repository.UserRepository;
import com.ubuntu.ubuntu_app.configuration.MapperConverter;
import com.ubuntu.ubuntu_app.infra.errors.IllegalParameterException;
import com.ubuntu.ubuntu_app.infra.errors.SqlEmptyResponse;
import com.ubuntu.ubuntu_app.infra.statuses.ResponseMap;
import com.ubuntu.ubuntu_app.model.dto.UserDto;
import com.ubuntu.ubuntu_app.model.dto.UserFetchDTO;
import com.ubuntu.ubuntu_app.model.dto.UserUpdateDTO;
import com.ubuntu.ubuntu_app.model.entities.UserEntity;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Lazy
@RequiredArgsConstructor
@Service
public class UserService {    

    private final UserRepository userRepository;

    public ResponseEntity<?> registerUser(UserDto userDto) {
        UserEntity user = new UserEntity(userDto);
        userRepository.save(user);
        var jsonResponse = ResponseMap.createResponse("Creado exitosamente");
        return new ResponseEntity<>(jsonResponse, HttpStatus.CREATED);
    }
    
    @Transactional
    public ResponseEntity<?> updateUser(UserUpdateDTO userDto, String email) {
        Optional<UserEntity> userObtained = userRepository.findByEmail(email);
        if (!userObtained.isPresent()) {
            throw new SqlEmptyResponse("El usuario no existe en la base de datos");
        }
        if(userObtained.get().getSuscribed() == userDto.isSuscribed()){
            throw new IllegalParameterException("User is already unsuscribed");
        }
        userObtained.get().editUser(userDto);
        var jsonResponse = ResponseMap.createResponse("Usuario Modificado exitosamente");
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }

    public ResponseEntity<?> deactivateUser(Long idUserToDeactivate) {
        Optional<UserEntity> user = userRepository.findById(idUserToDeactivate);
        if (user.isPresent()) {
            var userFound = user.get();
            if (userFound.isActivo() != false) {
                user.get().setActivo(false);
                userRepository.save(user.get());
                var jsonResponse = ResponseMap.createResponse("Usuario desactivado exitosamente");
                return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
            } else {
                var jsonResponse = ResponseMap.createResponse("El usuario no se puede desactivar porque ya está desactivado");
                return new ResponseEntity<>(jsonResponse, HttpStatus.CONFLICT);
            }
        } else {
            throw new SqlEmptyResponse("El usuario no existe en la base de datos");
        }
    }

    public ResponseEntity<?> findAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        List<UserFetchDTO> responseDTO = users.stream().map(userEntity -> MapperConverter.generate()
        .map(userEntity, UserFetchDTO.class))
        .collect(Collectors.toList());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    public String [] findAllEmails(){
        List<String> adminEmail = userRepository.findUsersEmail();
        return adminEmail.toArray(new String[0]);
    }

    public ResponseEntity<?> getUserByEmail(String email) {
        var userFound = userRepository.findByEmail(email);
        if(!userFound.isPresent()){
            throw new SqlEmptyResponse("No user found with email: " + email);
        }
        var jsonResponse = MapperConverter.generate().map(userFound.get(), UserUpdateDTO.class);
        return ResponseEntity.ok(jsonResponse);
    }

}
