package com.petpedia.web.controllers;

import com.petpedia.web.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("/images/{image}")
    public ResponseEntity<byte[]> getImage(@RequestParam String image) {
        Optional<Image> optImg = imageRepository.findByName(image);
        if (optImg.isPresent()) {
            Image img = optImg.get();
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.valueOf(img.getType()))
                    .body(img.getImage());
        } else {
            return ResponseEntity
                    .status(404)
                    .body(new byte[0]);
        }
    }
}
