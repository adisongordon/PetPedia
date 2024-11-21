package com.petpedia.web.model;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.Optional;

/**
 * Service to manage user details and authentication-related operations.
 * Implementations include retrieving user details by username, getting the first registered username,
 * and fetching the username of the currently logged-in user.
 */
@AllArgsConstructor
@Service
public class UsersDetailsService implements UserDetailsService {
    @Autowired
    private UsersRepository repository;

    /**
     * Loads the user details by the given username.
     *
     * @param username the username identifying the user whose data is required.
     * @return the fully populated user details.
     * @throws UsernameNotFoundException if the user could not be found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> users = repository.findByUsername(username);
        if (users.isPresent()) {
            var userObj = users.get();
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .build();
        }else{
            throw new UsernameNotFoundException(username);
        }
    }

    /**
     * Retrieves the username of the first registered user in the system.
     * If no users are found, it returns "Guest".
     *
     * @return the username of the first registered user or "Guest" if no user exists.
     */
    public String getFirstUsername() {
        Users user = repository.findFirstByOrderByIdAsc();
        return user != null ? user.getUsername() : "Guest";
    }

    /**
     * Retrieves the username of the currently logged-in user.
     * If no authentication information is available or the user is not authenticated,
     * it returns "Guest".
     *
     * @return the username of the currently logged-in user or "Guest" if not authenticated.
     */
    public String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "Guest";
    }
}
