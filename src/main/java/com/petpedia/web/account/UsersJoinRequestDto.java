package com.petpedia.web.account;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class UsersJoinRequestDto {
    private String username;
    private String password;

    @Builder
    public UsersJoinRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Users toEntity(PasswordEncoder passwordEncoder) {
        return Users.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build();
    }
}
