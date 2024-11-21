package com.petpedia.web.controllers;

import com.petpedia.web.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
public class AccountController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/req/signup", consumes = "application/json")
    public UsersDto createUser(@RequestBody Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Users savedUser = usersRepository.save(user);

        String imageUrl = "/user_images/" + (savedUser.getImage() != null ? savedUser.getImage().getName() : "default.png");

        List<UserPetDto> pets = savedUser.getPets().stream()
                .map(this::convertToUserPetDto)
                .collect(Collectors.toList());

        return new UsersDto(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(),
                savedUser.getPassword(), imageUrl, pets);
    }

    // Helper method to convert UserPet to UserPetDto
    private UserPetDto convertToUserPetDto(UserPet pet) {
        String imageUrl = "/user_images/" + (pet.getPicture() != null ? pet.getPicture().getName() : "default.png");
        return new UserPetDto(pet.getId(), pet.getName(), pet.getSpecies(), pet.getAge(), pet.getHealthIssues(), imageUrl);
    }

}
