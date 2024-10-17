package com.petpedia.web.controllers;

import com.petpedia.web.model.Post;
import com.petpedia.web.model.PostRepository;
import com.petpedia.web.model.PostService;
import com.petpedia.web.model.UsersDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ForumController {

    @Autowired
    private UsersDetailsService usersDetailsService;
    @Autowired
    private PostRepository postRepository;

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

