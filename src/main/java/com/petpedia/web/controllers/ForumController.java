package com.petpedia.web.controllers;

import com.petpedia.web.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private UsersDetailsService usersDetailsService;
    private final ImageRepository imageRepository;
    private final PostRepository postRepository;
    private final PostService postService;
    private final CommentRepository commentRepository;

    @GetMapping
    public String forum(Model model) {
        List<Post> posts = postService.getAllPostsWithComments();
        posts.sort((p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));  // Sorting posts by timestamp in descending order
        model.addAttribute("posts", posts);
        return "forum";
    }

    @PostMapping("/create-post")
    public String handleCreatePost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Model model) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Post post = new Post();
        post.setUsername(currentUsername);
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

        postService.createPost(post);
        return "redirect:/forum";
    }

    @PostMapping("/{id}/like")
    public String likePost(@PathVariable Long id) {
        postService.likePost(id);
        return "redirect:/forum";  // Refresh the page after liking a post
    }

    @PostMapping("/{id}/comment")
    public String commentPost(@PathVariable Long id, @RequestParam String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Comment comment = new Comment();
        comment.setUsername(currentUsername);
        comment.setContent(content);

        postService.addComment(id, comment);
        return "redirect:/forum";
    }

    @GetMapping("/{category}")
    public String forumByCategory(@PathVariable String category, Model model) {
        List<Post> posts = postService.getPostsByCategory(category);
        posts.sort((p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));  // Sorting posts by timestamp in descending order
        model.addAttribute("posts", posts);
        return "forum";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute Post post) {
        postService.createPost(post);
        return "redirect:/forum";
    }

    @GetMapping("/dogs")
    public String getDogsForum(Model model) {
        List<Post> posts = postService.getAllPostsWithComments();
        posts.sort((p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));
        model.addAttribute("posts", posts);
        return "forum-dogs";
    }
    @GetMapping("/cats")
    public String getCatsForum(Model model) {
        List<Post> posts = postService.getAllPostsWithComments();
        posts.sort((p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));
        model.addAttribute("posts", posts);
        return "forum-cats";
    }
    @GetMapping("/birds")
    public String getBirdsForum(Model model) {
        List<Post> posts = postService.getAllPostsWithComments();
        posts.sort((p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));
        model.addAttribute("posts", posts);
        return "forum-birds";
    }
    @GetMapping("/small-mammals")
    public String getSmallMammalsForum(Model model) {
        List<Post> posts = postService.getAllPostsWithComments();
        posts.sort((p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));
        model.addAttribute("posts", posts);
        return "forum-small-mammals";
    }

    @GetMapping("/reptiles")
    public String getReptilesForum(Model model) {
        List<Post> posts = postService.getAllPostsWithComments();
        posts.sort((p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));
        model.addAttribute("posts", posts);
        return "forum-reptiles";
    }

    @GetMapping("/amphibians")
    public String getForumAmphibians(Model model) {
        List<Post> posts = postService.getAllPostsWithComments();
        posts.sort((p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));
        model.addAttribute("posts", posts);
        return "forum-amphibians";
    }
}