package com.ubuntu.ubuntu_app.Repository;

import com.ubuntu.ubuntu_app.model.entities.ChatbotResponseEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatbotRepository extends JpaRepository<ChatbotResponseEntity,Long> {   
    
}
