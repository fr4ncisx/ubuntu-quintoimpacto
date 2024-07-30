package com.ubuntu.ubuntu_app.service;

import com.ubuntu.ubuntu_app.Repository.UserRepository;
import com.ubuntu.ubuntu_app.configuration.MapperConverter;
import com.ubuntu.ubuntu_app.infra.errors.SQLemptyDataException;
import com.ubuntu.ubuntu_app.infra.statuses.ResponseMap;
import com.ubuntu.ubuntu_app.model.dto.UserDto;
import com.ubuntu.ubuntu_app.model.dto.UserFetchDTO;
import com.ubuntu.ubuntu_app.model.dto.UserUpdateDTO;
import com.ubuntu.ubuntu_app.model.entities.UserEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> registerUser(UserDto userDto) {
        UserEntity user = new UserEntity(userDto);
        userRepository.save(user);
        var jsonResponse = ResponseMap.createResponse("Creado exitosamente");
        return new ResponseEntity<>(jsonResponse, HttpStatus.CREATED);
    }
    
    public ResponseEntity<?> updateUser(UserUpdateDTO userDto, Long id) {
        Optional<UserEntity> userObtained = userRepository.findById(id);
        if (userObtained.isPresent()) {
            userObtained.get().editUser(userDto);
            var jsonResponse = ResponseMap.createResponse("Usuario Modificado exitosamente");
            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        }
        throw new SQLemptyDataException("El usuario no existe en la base de datos");
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
            throw new SQLemptyDataException("El usuario no existe en la base de datos");
        }
    }

    public ResponseEntity<?> findAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        List<UserFetchDTO> responseDTO = users.stream().map(userEntity -> MapperConverter.generate()
        .map(userEntity, UserFetchDTO.class))
        .collect(Collectors.toList());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

}
