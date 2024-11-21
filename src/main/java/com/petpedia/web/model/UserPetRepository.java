package com.petpedia.web.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The UserPetRepository interface provides methods for performing CRUD operations
 * on UserPet entities in the database.
 */
public interface UserPetRepository extends JpaRepository<UserPet, Long> {
    List<UserPet> findByOwner(Users owner);
}
