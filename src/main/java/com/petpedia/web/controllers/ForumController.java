package com.petpedia.web.controllers;

import com.petpedia.web.model.UsersDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class ForumController {
    @Autowired
    private UsersDetailsService usersDetailsService;

    @PostMapping("/create-post")
    public String handleCreatePost(
            @RequestParam("username") String username,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("image") MultipartFile image,
            Model model) {

        // Create a new Post entity
        Post post = new Post();
        post.setUsername(username);
        post.setTitle(title);
        post.setContent(content);

        // Optionally handle image upload logic
        if (!image.isEmpty()) {
            // Save the image (to file system or cloud storage) and set the image URL in the Post object
        }

        // Save the post to the database
        postRepository.save(post);

        // Redirect to the forum page after successful post creation
        return "redirect:/forum";
    }

}
