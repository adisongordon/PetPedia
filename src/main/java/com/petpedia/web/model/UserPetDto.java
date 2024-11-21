package com.petpedia.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a user’s pet.
 * This class contains information about the pet such as
 * the pet's unique identifier, name, species, age, health issues,
 * and a URL link to an image of the pet.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPetDto {
    private Long id;
    private String name;
    private String species;
    private int age;
    private String healthIssues;
    private String imageUrl;
}