package com.petpedia.web.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_pet")
public class UserPet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String species;
    private int age;
    @Column(name = "health_issues")
    private String healthIssues;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "picture_id", referencedColumnName = "id")
    private Image picture;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users owner;
}
