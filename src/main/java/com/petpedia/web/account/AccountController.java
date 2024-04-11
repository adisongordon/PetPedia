package com.petpedia.web.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {
    @RequestMapping("/sign-in")
    public String signIn() {return "sign-in";}
}
