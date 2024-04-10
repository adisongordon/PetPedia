package com.petpedia.web.home;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String welcome(@AuthenticationPrincipal UserDetails userDetails, Model model){
        var authenticated = userDetails != null;
        model.addAttribute("currentRoute", "home");
        model.addAttribute("isAuthenticated", authenticated);
        return "home";
    }
}
