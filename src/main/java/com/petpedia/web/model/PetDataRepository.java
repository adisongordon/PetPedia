package com.petpedia.web.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface PetDataRepository extends JpaRepository<Pet, Long> {
    List<Pet> findBySpecies(String species);
    Optional<Pet> findByBreed(String breed);
    boolean existsByBreed(String breed);
}
