package com.petpedia.web.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.id = :id")
    Optional<Post> findByIdWithComments(@Param("id") Long id);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments")
    List<Post> findAllWithComments();

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.category = :category")
    List<Post> findByCategory(@Param("category") String category);
}