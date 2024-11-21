package com.petpedia.web.controllers;

import com.petpedia.web.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private final UsersRepository usersRepository;
    private final UserPetRepository userPetRepository;
    private final ImageRepository imageRepository;

    public ProfileController(UsersRepository usersRepository, UserPetRepository userPetRepository, ImageRepository imageRepository) {
        this.usersRepository = usersRepository;
        this.userPetRepository = userPetRepository;
        this.imageRepository = imageRepository;
    }

    @GetMapping
    public String showProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        Optional<Users> userOptional = usersRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
        } else {
            // Handle the case where the user is not found
            return "redirect:/login";
        }
        return "profile";
    }

    @PostMapping("/updateProfilePicture")
    public ResponseEntity<String> updateProfilePicture(@RequestParam("file") MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Users> optionalUser = usersRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();

            // Create and save the image
            Image profilePicture = new Image();
            profilePicture.setName(file.getOriginalFilename());
            profilePicture.setType(file.getContentType());
            profilePicture.setImage(file.getBytes());
            imageRepository.save(profilePicture);

            // Set the profile picture for the user
            user.setImage(profilePicture);
            usersRepository.save(user);

            return ResponseEntity.ok("Profile picture updated successfully");
        }
        return ResponseEntity.status(404).body("User not found");
    }

    @GetMapping("/picture")
    public ResponseEntity<byte[]> getProfilePicture(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        logger.debug("Fetching profile picture for user: {}", username);
        Optional<Users> userOptional = usersRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            Image userImage = user.getImage();

            if (userImage != null) {
                logger.debug("Profile picture found for user: {}", username);
                return createProfileImageResponse(userImage.getImage(), userImage.getType());
            }
        }

        logger.debug("No profile picture found for user: {}. Serving default avatar.", username);
        // Serve default avatar
        try (InputStream inputStream = getClass().getResourceAsStream("/static/images/default-avatar.png")) {
            if (inputStream != null) {
                byte[] defaultImageBytes = inputStream.readAllBytes();
                return createProfileImageResponse(defaultImageBytes, "image/png");
            } else {
                logger.error("Default avatar not found in the expected location.");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            logger.error("Error reading the default avatar image.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addPet")
    public ResponseEntity<Map<String, String>> addPet(
            @RequestParam("name") String name,
            @RequestParam("species") String species,
            @RequestParam("age") int age,
            @RequestParam("healthIssues") String healthIssues,
            @RequestParam("picture") MultipartFile picture) throws IOException {

        // Get authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Users> optionalUser = usersRepository.findByUsername(username);

        Map<String, String> response = new HashMap<>();

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();

            // Create and save the pet image
            Image petImage = new Image();
            petImage.setName(picture.getOriginalFilename());
            petImage.setType(picture.getContentType());
            petImage.setImage(picture.getBytes());

            // Create and save the pet
            UserPet pet = new UserPet();
            pet.setName(name);
            pet.setSpecies(species);
            pet.setAge(age);
            pet.setHealthIssues(healthIssues);
            pet.setPicture(petImage);
            pet.setOwner(user);

            userPetRepository.save(pet);

            response.put("message", "Pet added successfully");
            return ResponseEntity.ok(response);
        }

        response.put("message", "User not found");
        return ResponseEntity.status(404).body(response);
    }

    @GetMapping("/pets")
    public ResponseEntity<List<UserPetDto>> getPets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<Users> optionalUser = usersRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            List<UserPetDto> petDtos = user.getPets().stream().map(this::convertToUserPetDto).collect(Collectors.toList());
            return ResponseEntity.ok(petDtos);
        }

        return ResponseEntity.status(404).body(Collections.emptyList());
    }

    @GetMapping("/pet/picture/{petId}")
    public ResponseEntity<byte[]> getPetPicture(@PathVariable Long petId) {
        logger.debug("Fetching picture for petId: {}", petId);
        Optional<UserPet> petOptional = userPetRepository.findById(petId);

        if (petOptional.isPresent() && petOptional.get().getPicture() != null) {
            logger.debug("Pet picture found for petId: {}", petId);
            return createImageResponse(petOptional.get().getPicture().getImage());
        } else {
            logger.debug("No pet picture found for petId: {}", petId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @DeleteMapping("/deletePet/{petId}")
    public ResponseEntity<?> deletePet(@PathVariable Long petId, @AuthenticationPrincipal UserDetails userDetails) {
        logger.debug("Attempting to delete pet with ID: {}", petId);

        Optional<UserPet> petOptional = userPetRepository.findById(petId);
        if (petOptional.isPresent()) {
            userPetRepository.deleteById(petId);
            logger.debug("Pet with ID: {} deleted successfully", petId);
            return ResponseEntity.ok("Pet deleted successfully");
        } else {
            logger.debug("Pet with ID: {} not found", petId);
            return ResponseEntity.status(404).body("Pet not found");
        }
    }

    private UserPetDto convertToUserPetDto(UserPet pet) {
        String imageUrl = "/profile/pet/picture/" + pet.getId();
        return new UserPetDto(pet.getId(), pet.getName(), pet.getSpecies(), pet.getAge(), pet.getHealthIssues(), imageUrl);
    }

    private ResponseEntity<byte[]> createImageResponse(byte[] imageContent) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

    private ResponseEntity<byte[]> createProfileImageResponse(byte[] imageBytes, String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}