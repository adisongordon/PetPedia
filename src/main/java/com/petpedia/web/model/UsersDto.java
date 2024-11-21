package com.petpedia.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
