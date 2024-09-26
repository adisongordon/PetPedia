package com.petpedia.web.services;
import com.petpedia.web.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.petpedia.web.model.Users;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String getFirstUsername() {
        Users user = userRepository.findFirstByOrderByIdAsc();
        return user != null ? user.getUsername() : "Guest";
    }
}

