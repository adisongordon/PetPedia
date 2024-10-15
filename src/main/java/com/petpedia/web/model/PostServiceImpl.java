package com.petpedia.web.model;

import com.petpedia.web.model.Post;
import com.petpedia.web.model.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> getPostsByCategory(String category) {
        // Assuming title contains category for now (you can adjust this based on your needs)
        return postRepository.findAll().stream()
                .filter(post -> post.getTitle().toLowerCase().contains(category.toLowerCase()))
                .toList();
    }

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public Post likePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        post.setLikes(post.getLikes() + 1);
        return postRepository.save(post);
    }
}
