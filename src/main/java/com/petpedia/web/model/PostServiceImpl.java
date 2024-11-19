package com.petpedia.web.model;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    @Autowired
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

    @Override
    public void addComment(Long postId, Comment comment) {
        Post post = getPostById(postId);
        if (post != null) {
            post.addComment(comment);
            comment.setPost(post);
            postRepository.save(post);
            commentRepository.save(comment);
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