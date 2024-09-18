package com.petpedia.web.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WikiDataRepository extends JpaRepository<WikiData, Long> {
    Optional<Users> findByBreed(String breed);
}
