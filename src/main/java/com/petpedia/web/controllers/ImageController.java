package com.petpedia.web.controllers;

import com.petpedia.web.model.Image;
import com.petpedia.web.model.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;

    @GetMapping("/user_images/{image}")
    public ResponseEntity<byte[]> getImage(@PathVariable String image) {
        Optional<Image> optImg = imageRepository.findByName(image);
        if (optImg.isPresent()) {
            Image img = optImg.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(img.getType()));
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(img.getImage());
        } else {
            return ResponseEntity
                    .status(404)
                    .body(new byte[0]);
        }
    }
}