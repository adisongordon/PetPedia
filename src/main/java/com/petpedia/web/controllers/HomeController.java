package com.petpedia.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String Index() {
        return "home";
    }
    @GetMapping("/home")
    public String Home() {
        return "home";
    }

    @GetMapping("/req/login")
    public String logIn() {return "login";}

    @GetMapping("/req/signup")
    public String signUp() {return "signup";}

}
