package com.petpedia.web.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


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
