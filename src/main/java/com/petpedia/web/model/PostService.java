package com.petpedia.web.model;

import com.petpedia.web.model.Post;
import java.util.List;
import java.util.Optional;

public interface PostService {
    List<Post> getAllPosts();
    Optional<Post> getPostById(Long id);
    List<Post> getPostsByCategory(String category);
    Post createPost(Post post);
    void deletePost(Long id);
    Post likePost(Long id);
}
