package com.petpedia.web.account;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public String addUser(UsersJoinRequestDto requestDto) {
        return usersRepository.save(requestDto.toEntity(passwordEncoder)).getUsername();
    }
}
