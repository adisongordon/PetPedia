package com.petpedia.web.map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MapController {
    @RequestMapping("/map")
    public String map() {return "map";}
}
