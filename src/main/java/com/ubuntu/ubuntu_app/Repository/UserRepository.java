package com.ubuntu.ubuntu_app.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.model.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByEmail(String email);


    @Query(value = "select email from usuarios WHERE suscribed=true",nativeQuery = true)
    List<String> findUsersEmail();
}
