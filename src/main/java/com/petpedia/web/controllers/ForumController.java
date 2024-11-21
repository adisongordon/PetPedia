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

/**
 * ForumController is a Spring MVC Controller that handles HTTP requests related to the forum functionalities.
 * It manages the operations such as displaying posts, creating posts, liking posts, and adding comments to posts.
 */
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

    /**
     * Handles GET requests for the forum page.
     *
     * @param model the Model object that holds the data to be displayed in the view
     * @return the view name to be rendered
     */
    @GetMapping
    public String forum(Model model) {
        List<Post> posts = postService.getAllPostsWithComments();
        posts.sort((p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));  // Sorting posts by timestamp in descending order
        model.addAttribute("posts", posts);
        return "forum";
    }

    /**
     * Handles the creation of a new post.
     *
     * @param title The title of the post.
     * @param content The content of the post.
     * @param image An optional image associated with the post.
     * @param category The category of the post.
     * @param model The model to hold attributes for the view.
     * @return A redirect URL to the forum page.
     * @throws IOException If an error occurs while processing the image.
     */
    @PostMapping("/create-post")
    public String handleCreatePost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("category") String category,
            Model model) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Post post = new Post();
        post.setUsername(currentUsername);
        post.setTitle(title);
        post.setContent(content);
        post.setCategory(category);

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

    /**
     * Handles the liking of a post by its ID.
     *
     * @param id the ID of the post to be liked
     * @return a redirect string to refresh the forum page
     */
    @PostMapping("/{id}/like")
    public String likePost(@PathVariable Long id) {
        postService.likePost(id);
        return "redirect:/forum";  // Refresh the page after liking a post
    }

    /**
     * Adds a comment to an existing post and redirects to the forum.
     *
     * @param id the ID of the post to which the comment is to be added
     * @param content the content of the comment
     * @return a redirect URL to the forum page
     */
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

    /**
     * Handles HTTP GET requests to retrieve and display posts belonging to a specific category.
     *
     * @param category The category of the posts to retrieve.
     * @param model The model to which the list of posts will be added.
     * @return The name of the view to render, which in this case is "forum".
     */
    @GetMapping("/{category}")
    public String getPostsByCategory(@PathVariable("category") String category, Model model) {
        List<Post> posts = postService.getPostsByCategory(category);
        posts.sort((p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));  // Sorting posts by timestamp in descending order
        model.addAttribute("posts", posts);
        return "forum";
    }

    /**
     * Handles the creation of a new forum post and redirects to the forum page.
     *
     * @param post the Post object containing the details of the post to be created
     * @return a redirect string to the forum page
     */
    @PostMapping("/create")
    public String createPost(@ModelAttribute Post post) {
        postService.createPost(post);
        return "redirect:/forum";
    }

    /**
     * Handles the GET request for the dogs forum page.
     *
     * @param model the model object used to pass attributes to the view.
     * @return the view name for the dogs forum page.
     */
    @GetMapping("/dogs")
    public String getDogsForum(Model model) {
        List<Post> posts = postService.getPostsByCategory("dogs");
        posts.sort((p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));
        model.addAttribute("posts", posts);
        return "forum-dogs";
    }

    /**
     * Handles the request to retrieve and display the forum page for the "cats" category.
     *
     * @param model the model object used to pass attributes to the view.
     * @return the name of the view template to be rendered, in this case "forum-cats".
     */
    @GetMapping("/cats")
    public String getCatsForum(Model model) {
        List<Post> posts = postService.getPostsByCategory("cats");
        posts.sort((p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));
        model.addAttribute("posts", posts);
        return "forum-cats";
    }

    /**
     * Handles HTTP GET requests for the "/birds" endpoint.
     * This method fetches posts categorized under "birds", sorts them by the timestamp in descending order,
     * and adds them to the model to be displayed on the "forum-birds" view page.
     *
     * @param model the model that holds attributes for rendering views
     * @return the name of the view template to be rendered, in this case, "forum-birds"
     */
    @GetMapping("/birds")
    public String getBirdsForum(Model model) {
        List<Post> posts = postService.getPostsByCategory("birds");
        posts.sort((p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));
        model.addAttribute("posts", posts);
        return "forum-birds";
    }

    /**
     * Handles the HTTP GET request for fetching posts related to small mammals
     * and displaying them in the forum view. Retrieves posts categorized under
     * "small-mammals", sorts them by the timestamp in descending order, and
     * adds them to the provided model.
     *
     * @param model the Model object that holds the model attributes for the view.
     * @return the name of the view template to be rendered, which is "forum-small-mammals".
     */
    @GetMapping("/small-mammals")
    public String getSmallMammalsForum(Model model) {
        List<Post> posts = postService.getPostsByCategory("small-mammals");
        posts.sort((p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));
        model.addAttribute("posts", posts);
        return "forum-small-mammals";
    }

    /**
     * Retrieves the reptiles forum page with posts categorized as "reptiles".
     *
     * @param model the model to which the sorted list of posts will be added
     * @return the name of the view to render ("forum-reptiles")
     */
    @GetMapping("/reptiles")
    public String getReptilesForum(Model model) {
        List<Post> posts = postService.getPostsByCategory("reptiles");
        posts.sort((p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));
        model.addAttribute("posts", posts);
        return "forum-reptiles";
    }

    /**
     * Retrieves a list of posts categorized under 'amphibians', sorts them by timestamp
     * in descending order, and adds them to the model.
     *
     * @param model the model to which the list of amphibian posts will be added
     * @return the view name 'forum-amphibians'
     */
    @GetMapping("/amphibians")
    public String getForumAmphibians(Model model) {
        List<Post> posts = postService.getPostsByCategory("amphibians");
        posts.sort((p1, p2) -> p2.getTimestamp().compareTo(p1.getTimestamp()));
        model.addAttribute("posts", posts);
        return "forum-amphibians";
    }
}