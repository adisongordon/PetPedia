package com.petpedia.web.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UsersRepository interface provides methods for performing CRUD operations
 * on Users entities in the database.
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    Users findFirstByOrderByIdAsc();

    // Fetch users with their pets eagerly
    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.pets WHERE u.username = :username")
    Optional<Users> findByUsernameWithPets(@Param("username") String username);
}
