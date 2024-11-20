package com.petpedia.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.petpedia.web.model.UsersDetailsService;

@Controller
@RequiredArgsConstructor
public class ContentController {
    @Autowired
    private UsersDetailsService usersDetailsService;

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

    @GetMapping("/wiki")
    public String Wiki() { return "wiki"; }

    @GetMapping("/wiki/birds")
    public String getBirds() {
        return "wiki-birds";
    }

    @GetMapping("/wiki/cats")
    public String getCats() {
        return "wiki-cats";
    }

    @GetMapping("/wiki/dogs")
    public String getDogs() {
        return "wiki-dogs";
    }

    @GetMapping("/wiki/fish")
    public String getFish() {
        return "wiki-fish";
    }

    @GetMapping("/wiki/horses")
    public String getHorses() {
        return "wiki-horses";
    }

    @GetMapping("/wiki/rabbits")
    public String getRabbits() {
        return "wiki-rabbits";
    }

    @GetMapping("/wiki/turtles")
    public String getTurtles() {
        return "wiki-turtles";
    }

    @GetMapping("/shelters")
    public String Shelters() {
        return "shelters";
    }

    @GetMapping("/shelters/RuffHaven")
    public String getRuff() {
        return "shelters-RuffHaven";
    }

    @GetMapping("/shelters/RuffHaven/foster")
    public String getFoster() {
        return "shelters-RuffHaven-foster";
    }

    @GetMapping("/shelters/RuffHaven/donate")
    public String getDonate() {
        return "shelters-RuffHaven-donate";
    }

    @GetMapping("/shelters/RuffHaven/volunteer")
    public String getVolunteer() {
        return "shelters-RuffHaven-volunteer";
    }

    @GetMapping("/quiz")
    public String quiz() {return "quiz";}

    @GetMapping("/map")
    public String map() {return "map";}

    @GetMapping("/profile")
    public String profile() {return "profile";}

    @GetMapping("/create-post")
    public String showCreatePostPage(Model model) {
        String username = usersDetailsService.getLoggedInUsername();
        model.addAttribute("username", username);
        return "create-post"; // This should map to your create-post.html page
    }

    @GetMapping("/profile-my-posts")
    public String profileMyPosts() {return "profile-my-posts";}

}
