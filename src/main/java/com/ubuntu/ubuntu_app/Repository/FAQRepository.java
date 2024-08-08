package com.ubuntu.ubuntu_app.Repository;

import com.ubuntu.ubuntu_app.model.entities.FAQEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FAQRepository extends JpaRepository<FAQEntity,Long> {


}
