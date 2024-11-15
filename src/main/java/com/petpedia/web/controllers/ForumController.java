package com.petpedia.web.controllers;

import com.petpedia.web.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
@RequiredArgsConstructor
public class ForumController {

    @Autowired
    private UsersDetailsService usersDetailsService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ImageRepository imageRepository;

    private final PostService postService;

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
            Model model) throws IOException {

        Post post = new Post();
        post.setUsername(username);
        post.setTitle(title);
        post.setContent(content);

        if (image != null && !image.isEmpty()) {
            String imageUrl = "/user_images/" + image.hashCode();
            post.setImageUrl(imageUrl);
            imageRepository.save(Image.builder()
                    .name(String.valueOf(image.hashCode()))
                    .type(image.getContentType())
                    .image(image.getBytes())
                    .build());
        }

        postRepository.save(post);
        return "redirect:/forum";
    }

    @GetMapping("/user_images/{image}")
    public ResponseEntity<byte[]> getImage(@PathVariable String image) {
        Optional<Image> optImg = imageRepository.findByName(image);
        System.out.println(image);
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

    @GetMapping
    public String forumHome(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "forum";
    }

    /*
        TODO:
            Enable the ability to like posts,
            currently this feature is not working
     */
    @PostMapping("/like/{id}")
    public String likePost(@PathVariable Long id) {
        postService.likePost(id);
        return "redirect:/forum";  // Refresh the page after liking a post
    }

    @GetMapping("/{category}")
    public String forumByCategory(@PathVariable String category, Model model) {
        List<Post> posts = postService.getPostsByCategory(category);
        model.addAttribute("posts", posts);
        return "forum";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute Post post) {
        postService.createPost(post);
        return "redirect:/forum";
    }

    @GetMapping("/forum/dogs")
    public String getDogsForum(Model model) {
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "forum-dogs";
    }
    @GetMapping("/forum/cats")
    public String getCatsForum(Model model) {
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "forum-cats";
    }
    @GetMapping("/forum/birds")
    public String getBirdsForum(Model model) {
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "forum-birds";
    }
    @GetMapping("/forum/small-mammals")
    public String getSmallMammalsForum(Model model) {
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "forum-small-mammals";
    }

    @GetMapping("/forum/reptiles")
    public String getReptilesForum(Model model) {
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "forum-reptiles";
    }

    @GetMapping("/forum/amphibians")
    public String getForumAmphibians(Model model) {
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "forum-amphibians";
    }
}

