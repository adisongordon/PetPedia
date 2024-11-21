package com.petpedia.web.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPetRepository extends JpaRepository<UserPet, Long> {
    List<UserPet> findByOwner(Users owner);
}
