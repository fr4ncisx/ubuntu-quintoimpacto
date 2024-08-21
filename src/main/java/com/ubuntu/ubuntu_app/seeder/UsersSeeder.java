package com.ubuntu.ubuntu_app.seeder;

import java.util.Arrays;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ubuntu.ubuntu_app.Repository.UserRepository;
import com.ubuntu.ubuntu_app.model.entities.UserEntity;
import com.ubuntu.ubuntu_app.model.enums.UserRole;
import com.ubuntu.ubuntu_app.model.generator.RandomPhoneGenerator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UsersSeeder implements CommandLineRunner{
    
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0){
            loadUsers();
        }        
    }

    @Transactional
    private void loadUsers(){
        List<UserEntity> listOfUsers = Arrays.asList(
            new UserEntity("Ubuntu", "Administracion", "semilleroubuntu.dev@gmail.com", UserRole.ADMIN, RandomPhoneGenerator.create(), null),
            new UserEntity("Teby", "Ortiz", "tebyortiz888@gmail.com", UserRole.ADMIN, RandomPhoneGenerator.create(), null),
            new UserEntity("Camila", "Arce", "arce.b.camila@gmail.com", UserRole.ADMIN, RandomPhoneGenerator.create(), null),
            new UserEntity("Santiago", "perea", "santi2004perea@gmail.com", UserRole.ADMIN, RandomPhoneGenerator.create(), null),
            new UserEntity("Ivan", "Sanchez", null, UserRole.ADMIN, RandomPhoneGenerator.create(), null)
        );
        userRepository.saveAll(listOfUsers);
    }
}
