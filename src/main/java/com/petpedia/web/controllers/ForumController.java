package com.petpedia.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class ForumController {
    @RequestMapping("/forum")
    public String forum() {return "forum";}
}
