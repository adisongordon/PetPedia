package com.petpedia.web.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * The Comment class represents user comments associated with a specific post.
 * Each comment contains a reference to the post it belongs to, the username of the commenter,
 * the content of the comment, and a timestamp indicating when the comment was created.
 * The timestamp is automatically set to the current time when the comment is created.
 *
 * The class also provides a method to get a human-readable string representing the time elapsed
 * since the comment was created, such as "2 hours ago" or "Just now".
 *
 * It is annotated with JPA annotations to map the class to a database table.
 */
@Setter
@Getter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="post_id", nullable=false)
    private Post post;

    private String username;
    private String content;
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    // Getters and Setters

    public String getTimeAgo() {
        Duration duration = Duration.between(timestamp, LocalDateTime.now());
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (days > 0) {
            return days + " day" + (days > 1 ? "s" : "") + " ago";
        } else if (hours > 0) {
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        } else if (minutes > 0) {
            return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
        } else {
            return "Just now";
        }
    }
}