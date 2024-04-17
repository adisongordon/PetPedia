package com.petpedia.web.wiki;

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
}
