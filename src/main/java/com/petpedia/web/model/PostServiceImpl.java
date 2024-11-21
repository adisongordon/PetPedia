package com.petpedia.web.model;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of PostService interface for managing posts and their related operations.
 * Provides methods to create, read, update, and delete posts, as well as to manage comments and likes on posts.
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {


    private final PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    /**
     * Retrieves all posts from the repository.
     *
     * @return a List of all Post objects.
     */
    @Transactional(readOnly = true)
    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    /**
     *  Retrieves posts from the repository using the post ID
     * @param id the post id
     * @return returns the post with the required id or null
     */
    @Transactional(readOnly = true)
    @Override
    public Post getPostById(Long id) {
        return postRepository.findByIdWithComments(id).orElse(null);
    }

    /**
     * Retrieves posts from the repository by the category of the post
     * @param category the category of the post
     * @return returns the posts under the required category
     */
    @Transactional(readOnly = true)
    @Override
    public List<Post> getPostsByCategory(String category) {
        return postRepository.findByCategory(category);
    }

    /**
     * Creates posts
     * @param post the post that is created
     */
    @Override
    public void createPost(Post post) {
        postRepository.save(post);
    }

    /**
     * Deletes posts
     * @param id the post that is deleted
     */
    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    /**
     * Likes posts
     * @param postId the post id of the post that is liked
     */
    @Override
    public void likePost(Long postId) {
        Post post = getPostById(postId);
        if (post != null) {
            post.incrementLikes();
            postRepository.save(post);
        }
    }

    /**
     * Removes like from post
     * @param postId the ID of the post that the like is being removed from
     * @return returns the post with a decremented like
     */
    @Override
    public Post removeLike(Long postId) {
        Post post = getPostById(postId);
        if (post != null) {
            post.decrementLikes();
            postRepository.save(post);
        }
        return post;
    }

    /**
     * Deletes comment from post
     * @param id the ID of the post
     * @param commentId the ID of the comment
     */
    @Override
    public void deleteComment(Long id, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            comment.setPost(null);
            commentRepository.delete(comment);
        }
    }

    /**
     * Add comments to post
     * @param postId the ID of the post
     * @param comment the comment being left
     */
    @Transactional
    @Override
    public void addComment(Long postId, Comment comment) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            comment.setPost(post);
            commentRepository.save(comment); // Save comment first
            post.getComments().add(comment); // Then add it to the post's comment list
            postRepository.save(post); // Finally, save the post to persist changes
        }
    }

    /**
     * Retrieves comments from the repository based on post ID
     * @param postId the ID of the post
     * @return returns the comments on the post
     */
    @Transactional(readOnly = true)
    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        Post post = postRepository.findByIdWithComments(postId).orElse(null);
        if (post != null) {
            return post.getComments();
        }
        return null;
    }

    /**
     * Retrieves all posts with comments
     * @return returns all posts with comments
     */
    @Transactional(readOnly = true)
    @Override
    public List<Post> getAllPostsWithComments() {
        return postRepository.findAllWithComments();
    }
}