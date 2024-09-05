package com.petpedia.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class WikiController {

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
}
