package com.petpedia.web.articles;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class ArticlesController {

    @RequestMapping("/articles")
    public String Articles() {
        return "articles";
    }
}
