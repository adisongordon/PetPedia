package com.petpedia.web.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


/**
 * The Users class represents a user entity in the system.
 * It contains essential information such as the user's username, email, password, and an associated profile image.
 * Each user can also own multiple pets, represented by the UserPet class.
 *
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<UserPet> pets;

}
