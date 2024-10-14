package com.petpedia.web.controllers;

import com.petpedia.web.model.Post;
import com.petpedia.web.model.PostRepository;
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

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ForumController {

    @Autowired
    private UsersDetailsService usersDetailsService;
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/forum")
    public String forum(Model model) {
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "forum";
    }

    @PostMapping("/create-post")
    public String handleCreatePost(
            @RequestParam("username") String username,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Model model) {

        Post post = new Post();
        post.setUsername(username);
        post.setTitle(title);
        post.setContent(content);

        if (image != null && !image.isEmpty()) {
            String imageUrl = "/images/" + image.getOriginalFilename();
            post.setImageUrl(imageUrl);
            // TODO: Save the image to the filesystem or cloud storage.
        }

        postRepository.save(post);
        return "redirect:/forum";
    }
}

