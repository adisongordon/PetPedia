package com.petpedia.web.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final UsersService usersService;
    @GetMapping("/sign-in")
    public String signIn() {return "sign-in";}

    @GetMapping("/sign-up")
    public String signUp() {return "sign-up";}

    @PostMapping("/sign-up")
    public String userJoin(@ModelAttribute UsersJoinRequestDto requestDto) {
        usersService.addUser(requestDto);
        return "sign-in";
    }
}
