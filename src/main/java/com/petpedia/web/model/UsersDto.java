package com.petpedia.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a user in the system.
 * This class encapsulates user-related information including the user's id, username,
 * email, password, image URL, and a list of pets associated with the user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String imageUrl;
    private List<UserPetDto> pets;
}
