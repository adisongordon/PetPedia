package com.petpedia.web.controllers;

import com.petpedia.web.model.*;
import com.petpedia.web.model.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private UsersDetailsService usersDetailsService;
    @Autowired
    private ImageRepository imageRepository;
    private final PostRepository postRepository;
    private final PostService postService;
    private final CommentRepository commentRepository;

    @GetMapping
    public String forum(Model model) {
        List<Post> posts = postService.getAllPostsWithComments();
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
            String imageUrl = "/forum/user_images/" + image.hashCode();  // Ensure URL is properly prefixed
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

    @GetMapping("/home")
    public String forumHome(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "forum";
    }

    @GetMapping("/forum")
    public String viewForum(Model model) {
        List<Post> posts = postService.getAllPostsWithComments();
        model.addAttribute("posts", posts);
        return "forum";
    }

    @PostMapping("/{id}/like")
    public String likePost(@PathVariable Long id) {
        postService.likePost(id);
        return "redirect:/forum";  // Redirect to the forum page after handling the like
    }

    @PostMapping("/{id}/comment")
    public String commentPost(@PathVariable Long id, @RequestParam String username, @RequestParam String content) {
        Comment comment = new Comment();
        comment.setUsername(username);
        comment.setContent(content);
        postService.addComment(id, comment);
        return "redirect:/forum";
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

    @GetMapping("/dogs")
    public String getDogsForum(Model model) {
        List<Post> posts = postService.getAllPostsWithComments();
        model.addAttribute("posts", posts);
        return "forum-dogs";
    }
    @GetMapping("/cats")
    public String getCatsForum(Model model) {
        List<Post> posts = postService.getAllPostsWithComments();
        model.addAttribute("posts", posts);
        return "forum-cats";
    }
    @GetMapping("/birds")
    public String getBirdsForum(Model model) {
        List<Post> posts = postService.getAllPostsWithComments();
        model.addAttribute("posts", posts);
        return "forum-birds";
    }
    @GetMapping("/small-mammals")
    public String getSmallMammalsForum(Model model) {
        List<Post> posts = postService.getAllPostsWithComments();
        model.addAttribute("posts", posts);
        return "forum-small-mammals";
    }

    @GetMapping("/reptiles")
    public String getReptilesForum(Model model) {
        List<Post> posts = postService.getAllPostsWithComments();
        model.addAttribute("posts", posts);
        return "forum-reptiles";
    }

    @GetMapping("/amphibians")
    public String getForumAmphibians(Model model) {
        List<Post> posts = postService.getAllPostsWithComments();
        model.addAttribute("posts", posts);
        return "forum-amphibians";
    }
}