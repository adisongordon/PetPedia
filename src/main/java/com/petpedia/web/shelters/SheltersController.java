package com.petpedia.web.shelters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class SheltersController {

    @GetMapping("/shelters")
    public String Shelters() {
        return "shelters";
    }

    @GetMapping("/shelters/RuffHaven")
    public String getRuff() {
        return "shelters-RuffHaven";
    }
}
