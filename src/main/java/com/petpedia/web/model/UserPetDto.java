package com.petpedia.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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