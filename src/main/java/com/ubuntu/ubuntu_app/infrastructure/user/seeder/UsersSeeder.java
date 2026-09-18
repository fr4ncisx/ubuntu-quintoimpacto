package com.ubuntu.ubuntu_app.infrastructure.user.seeder;

import java.util.Arrays;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.ubuntu.ubuntu_app.infrastructure.user.repository.UserRepository;
import com.ubuntu.ubuntu_app.infrastructure.user.entity.UserEntity;
import com.ubuntu.ubuntu_app.application.user.UserRole;
import com.ubuntu.ubuntu_app.shared.support.RandomPhoneGenerator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Order(3)
@ConditionalOnProperty(name = "app.seeding.enabled", havingValue = "true", matchIfMissing = true)
public class UsersSeeder implements CommandLineRunner {
    
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            loadUsers();
        }        
    }

    private void loadUsers() {
        List<UserEntity> listOfUsers = Arrays.asList(
            new UserEntity("Ubuntu", "Administracion", "semilleroubuntu.dev@gmail.com", UserRole.ADMIN, RandomPhoneGenerator.create(), null),
            new UserEntity("Usuario", "Test", "user.test@example.com", UserRole.USER, RandomPhoneGenerator.create(), null),
            new UserEntity("Teby", "Ortiz", "tebyortiz888@gmail.com", UserRole.ADMIN, RandomPhoneGenerator.create(), null),
            new UserEntity("Camila", "Arce", "arce.b.camila@gmail.com", UserRole.ADMIN, RandomPhoneGenerator.create(), null),
            new UserEntity("Santiago", "perea", "santi2004perea@gmail.com", UserRole.ADMIN, RandomPhoneGenerator.create(), null),
            new UserEntity("Ivan", "Sanchez", null, UserRole.ADMIN, RandomPhoneGenerator.create(), null)
        );
        userRepository.saveAll(listOfUsers);
    }
}
