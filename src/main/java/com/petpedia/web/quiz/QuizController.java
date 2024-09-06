package com.petpedia.web.quiz;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class QuizController {
    @RequestMapping("/quiz")
    public String quiz() {return "quiz";}
}
