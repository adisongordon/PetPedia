package com.petpedia.web.model;

import java.util.List;

/**
 * Service interface for managing posts.
 */
public interface PostService {
    List<Post> getAllPosts();
    Post getPostById(Long id);
    List<Post> getPostsByCategory(String category);
    void createPost(Post post);
    void deletePost(Long id);
    void likePost(Long id);
    Post removeLike(Long id);
    void addComment(Long postId, Comment comment);
    void deleteComment(Long id, Long commentId);
    List<Comment> getCommentsByPostId(Long id);
    List<Post> getAllPostsWithComments();
}
