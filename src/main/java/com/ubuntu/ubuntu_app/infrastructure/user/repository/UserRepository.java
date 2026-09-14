package com.ubuntu.ubuntu_app.infrastructure.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.infrastructure.user.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByEmail(String email);


    @Query(value = "select email from users WHERE subscribed=true",nativeQuery = true)
    List<String> findAdminEmails();
}
