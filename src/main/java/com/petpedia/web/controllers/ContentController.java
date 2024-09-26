package com.petpedia.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String Wiki() {
        return "wiki";
    }

    @GetMapping("/wiki/dogs")
    public String getDogs() {
        return "wiki-dogs";
    }

    @GetMapping("/wiki/cats")
    public String getCats() {
        return "wiki-cats";
    }

    @GetMapping("/shelters")
    public String Shelters() {
        return "shelters";
    }

    @GetMapping("/shelters/RuffHaven")
    public String getRuff() {
        return "shelters-RuffHaven";
    }
    @GetMapping("/quiz")
    public String quiz() {return "quiz";}
    @GetMapping("/forum")
    public String forum() {return "forum";}
    @GetMapping("/map")
    public String map() {return "map";}

    @GetMapping("/profile")
    public String profile() {return "profile";}

    /*
    @RequestMapping("/create-post")
    public String createPost() {return "create-post";}
     */

    @GetMapping("/create-post")
    public String showCreatePostPage(Model model) {
        String username = usersDetailsService.getFirstUsername();
        model.addAttribute("username", username);
        return "create-post"; // This should map to your create-post.html page
    }

    // TODO add API endpoints for retrieving wiki data from database
    /*
    e.g.
    @GetMapping("/api/wiki-data")
    public Map<String, String> wikiData() {
        Map<String, String> m = new HashMap<String, String>();
        // retrieve database info and add to hash map
        return m;
    }
     */


}
