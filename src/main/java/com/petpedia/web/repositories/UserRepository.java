package com.petpedia.web.repositories;

import com.petpedia.web.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    // Custom query to find the first user
    Users findFirstByOrderByIdAsc();
}

