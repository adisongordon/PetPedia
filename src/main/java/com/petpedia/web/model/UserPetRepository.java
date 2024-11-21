package com.petpedia.web.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPetRepository extends JpaRepository<UserPet, Long> {
}
