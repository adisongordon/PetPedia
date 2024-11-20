package com.petpedia.web.model;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {


    private final PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Post getPostById(Long id) {
        return postRepository.findByIdWithComments(id).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Post> getPostsByCategory(String category) {
        return postRepository.findAll().stream()
                .filter(post -> post.getTitle().toLowerCase().contains(category.toLowerCase()))
                .toList();
    }

    @Override
    public void createPost(Post post) {
        postRepository.save(post);
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public void likePost(Long postId) {
        Post post = getPostById(postId);
        if (post != null) {
            post.incrementLikes();
            postRepository.save(post);
        }
    }

    @Override
    public Post removeLike(Long postId) {
        Post post = getPostById(postId);
        if (post != null) {
            post.decrementLikes();
            postRepository.save(post);
        }
        return post;
    }

    @Override
    public void deleteComment(Long id, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            comment.setPost(null);
            commentRepository.delete(comment);
        }
    }

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

    @Transactional(readOnly = true)
    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        Post post = postRepository.findByIdWithComments(postId).orElse(null);
        if (post != null) {
            return post.getComments();
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Post> getAllPostsWithComments() {
        return postRepository.findAllWithComments();
    }
}