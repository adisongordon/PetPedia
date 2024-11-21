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

/**
 * Controller to handle profile-related requests.
 *
 * This controller provides endpoints for displaying the user profile,
 * updating the profile picture, adding pets, retrieving pets, and
 * handling pet images.
 *
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private final UsersRepository usersRepository;
    private final UserPetRepository userPetRepository;
    private final ImageRepository imageRepository;

    /**
     * Constructs a ProfileController with the specified repository dependencies.
     *
     * @param usersRepository the repository to manage user data
     * @param userPetRepository the repository to manage user pet data
     * @param imageRepository the repository to manage image data
     */
    public ProfileController(UsersRepository usersRepository, UserPetRepository userPetRepository, ImageRepository imageRepository) {
        this.usersRepository = usersRepository;
        this.userPetRepository = userPetRepository;
        this.imageRepository = imageRepository;
    }

    /**
     * Displays the profile of the currently authenticated user.
     *
     * @param userDetails the details of the authenticated user
     * @param model       the model object that holds the data for rendering the view
     * @return the name of the view to be rendered, either "profile" if the user is found or a redirection to the login page
     */
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

    /**
     * Updates the profile picture for the currently authenticated user.
     *
     * @param file the new profile picture file to be uploaded
     * @return ResponseEntity containing a success message if the update was successful,
     *         or a 404 status with an error message if the user was not found
     * @throws IOException if an error occurs reading the file
     */
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

    /**
     * Retrieves the profile picture for the currently authenticated user.
     * If the user has no profile picture, a default avatar image is served.
     *
     * @param userDetails the authenticated user's details
     * @return ResponseEntity containing the user's profile picture as a byte array
     *         and the appropriate content type, or an error status if the default
     *         avatar could not be found or read
     */
    @GetMapping("/picture")
    public ResponseEntity<byte[]> getProfilePicture(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        //logger.debug("Fetching profile picture for user: {}", username);
        Optional<Users> userOptional = usersRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            Image userImage = user.getImage();

            if (userImage != null) {
                //logger.debug("Profile picture found for user: {}", username);
                return createProfileImageResponse(userImage.getImage(), userImage.getType());
            }
        }

        //logger.debug("No profile picture found for user: {}. Serving default avatar.", username);
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

    /**
     * Adds a new pet for the authenticated user.
     *
     * @param name the name of the pet
     * @param species the species of the pet
     * @param age the age of the pet
     * @param healthIssues the health issues of the pet
     * @param picture the picture of the pet
     * @return a ResponseEntity containing a message indicating the result of the operation
     * @throws IOException if an I/O error occurs while processing the picture file
     */
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

    /**
     * Retrieves the list of pets associated with the authenticated user.
     *
     * @return ResponseEntity containing a list of UserPetDto objects representing the user's pets
     *         or a 404 status with an empty list if the user is not found.
     */
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

    /**
     * Fetches the picture of a pet based on the provided petId.
     *
     * @param petId the ID of the pet whose picture is to be fetched
     * @return ResponseEntity containing the byte array of the pet picture if found,
     *         otherwise returns a ResponseEntity with a NOT_FOUND status
     */
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

    /**
     * Deletes a pet based on its ID.
     *
     * @param petId the ID of the pet to be deleted
     * @param userDetails the details of the authenticated user
     * @return ResponseEntity indicating the outcome of the delete operation
     */
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

    /**
     * Retrieves the profile picture for a given user by username. If a profile picture
     * is found for the user, it is returned as a byte array within a ResponseEntity.
     * If no profile picture is found, a default avatar image is served instead.
     *
     * @param username the username of the user whose profile picture is to be retrieved
     * @return a ResponseEntity containing the user's profile picture byte array and its type,
     *         or a default avatar image if no profile picture is found for the user.
     */
    @GetMapping("/picture/{username}")
    public ResponseEntity<byte[]> getUserProfilePicture(@PathVariable String username) {
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

    /**
     * Converts a given UserPet entity to a UserPetDto.
     *
     * @param pet the UserPet entity to be converted
     * @return a UserPetDto containing the information from the given UserPet
     */
    private UserPetDto convertToUserPetDto(UserPet pet) {
        String imageUrl = "/profile/pet/picture/" + pet.getId();
        return new UserPetDto(pet.getId(), pet.getName(), pet.getSpecies(), pet.getAge(), pet.getHealthIssues(), imageUrl);
    }

    /**
     * Creates an HTTP response entity containing the given image content.
     *
     * @param imageContent the byte array representing the image content to be returned in the response body
     * @return a ResponseEntity object containing the image content with appropriate HTTP headers and status code
     */
    private ResponseEntity<byte[]> createImageResponse(byte[] imageContent) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageContent, headers, HttpStatus.OK);
    }

    /**
     * Creates an HTTP response entity that contains a profile image as a byte array.
     *
     * @param imageBytes the profile image data in byte array format
     * @param contentType the content type of the image (e.g., "image/jpeg", "image/png")
     * @return a ResponseEntity object containing the profile image and appropriate headers
     */
    private ResponseEntity<byte[]> createProfileImageResponse(byte[] imageBytes, String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}