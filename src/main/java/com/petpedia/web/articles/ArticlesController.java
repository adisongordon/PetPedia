package com.petpedia.web.articles;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ArticlesController {

    @GetMapping("/articles")
    public String Articles() {
        return "articles";
    }
}
