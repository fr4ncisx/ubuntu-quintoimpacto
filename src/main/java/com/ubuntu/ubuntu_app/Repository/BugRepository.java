package com.ubuntu.ubuntu_app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ubuntu.ubuntu_app.model.entities.BugEntity;

@Repository
public interface BugRepository extends JpaRepository<BugEntity, Long>{

}
