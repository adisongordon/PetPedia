package com.petpedia.web.controllers;

import com.petpedia.web.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private final UsersRepository usersRepository;
    private final UserPetRepository userPetRepository;

    public ProfileController(UsersRepository usersRepository, UserPetRepository userPetRepository) {
        this.usersRepository = usersRepository;
        this.userPetRepository = userPetRepository;
    }

    @GetMapping
    public String showProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        Optional<Users> userOptional = usersRepository.findByUsernameWithPets(username);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
        } else {
            // Handle the case where the user is not found
            return "redirect:/login";
        }
        return "profile";
    }

    @PostMapping("/updateProfilePicture")
    public ResponseEntity<String> updateProfilePicture(@RequestParam("file")MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Users> optionalUser = usersRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.setProfilePicture(file.getBytes());
            usersRepository.save(user);
            return ResponseEntity.ok("Profile picture updated successfully");
        }
        return ResponseEntity.status(404).body("User not found");
    }

    @GetMapping("/picture")
    public ResponseEntity<byte[]> getProfilePicture(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Optional<Users> userOptional = usersRepository.findByUsername(username);

        if (userOptional.isPresent() && userOptional.get().getProfilePicture() != null) {
            byte[] imageContent = userOptional.get().getProfilePicture();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addPet")
    public ResponseEntity<String> addPet(
            @RequestParam("name") String name,
            @RequestParam("species") String species,
            @RequestParam("age") int age,
            @RequestParam("healthIssues") String healthIssues,
            @RequestParam("picture") MultipartFile picture) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Users> optionalUser = usersRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            UserPet pet = new UserPet();
            pet.setName(name);
            pet.setSpecies(species);
            pet.setAge(age);
            pet.setHealthIssues(healthIssues);
            pet.setPicture(picture.getBytes());
            pet.setOwner(user);

            userPetRepository.save(pet);
            return ResponseEntity.ok("Pet added successfully");
        }
        return ResponseEntity.status(404).body("User not found");
    }

    @GetMapping("/pets")
    public List<UserPet> getPets(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Optional<Users> userOptional = usersRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            return user.getPets();
        } else {
            return null;
        }
    }

    @GetMapping("/pet/picture/{petId}")
    public ResponseEntity<byte[]> getPetPicture(@PathVariable Long petId) {
        // Your logic to retrieve the picture for the given petId
        UserPet pet = userPetRepository.findById(petId).orElse(null);

        if (pet != null && pet.getPicture() != null) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(pet.getPicture());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
