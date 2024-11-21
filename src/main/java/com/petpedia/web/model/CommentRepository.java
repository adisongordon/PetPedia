package com.petpedia.web.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CommentRepository provides CRUD operations for Comment entities.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
